package br.com.danielpadua.crstracker.restClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import br.com.danielpadua.crstracker.model.CrsInfoListener;
import br.com.danielpadua.crstracker.model.MainInfoListener;
import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.exchange.Exchange;
import br.com.danielpadua.crstracker.model.exchange.FXCBit;
import br.com.danielpadua.crstracker.model.pair.Pair;
import br.com.danielpadua.crstracker.model.pair.RealPair;
import br.com.danielpadua.crstracker.model.widget.WidgetInfoListener;
import br.com.danielpadua.crstracker.model.widget.WidgetTracker;
import br.com.danielpadua.crstracker.restClient.jsonParser.CrsJsonParser;
import br.com.danielpadua.crstracker.restClient.jsonParser.FXCBitJsonParser;

import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_PATTERN_API_INVALID_JSON;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_PATTERN_API_NOT_TREATED;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_PATTERN_API_STATUS_500;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_PATTERN_API_TIMEOUT;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_TICKER_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_TIMEOUT_SECONDS;
import static br.com.danielpadua.crstracker.util.CRSUtil.SHARED_PREFERENCES_FXCBIT;

public class FXCBitRestClient extends RestClient<FXCBit> {

    public FXCBitRestClient(Context context, CrsInfoListener infoListener) {
        super(context, infoListener, FXCBit.class);
    }

    @Override
    protected Exchange doInBackground(WidgetTracker... widgetTrackers) {
        if (widgetTrackers != null && widgetTrackers.length > 0) {
            super.widgetTracker = widgetTrackers[0];
        }

        FXCBit fxcbit = repository.get(SHARED_PREFERENCES_FXCBIT);

        if (fxcbit == null) {
            fxcbit = new FXCBit();
            fxcbit = (FXCBit) updateExchange(fxcbit);
            fxcbit.setLastUpdated(new Date());
            repository.persist(SHARED_PREFERENCES_FXCBIT, fxcbit);
        } else {
            long duration = (new Date().getTime() - fxcbit.getLastUpdated().getTime());
            long cooldown = TimeUnit.MILLISECONDS.convert(fxcbit.getRefreshCooldownSeconds(), TimeUnit.SECONDS);
            if (duration > cooldown) {
                fxcbit = new FXCBit();
            }
            fxcbit = (FXCBit) updateExchange(fxcbit);
            fxcbit.setLastUpdated(new Date());
            repository.persist(SHARED_PREFERENCES_FXCBIT, fxcbit);
        }

        return fxcbit;
    }

    @Override
    protected Exchange updateExchange(Exchange exchange) {
        FXCBit fxcbit = new FXCBit();
        fxcbit.getSupportedPairs().clear();
        for (Pair pair : exchange.getSupportedPairs()) {
            long cooldown = TimeUnit.MILLISECONDS.convert(pair.getApiCooldownSeconds(), TimeUnit.SECONDS);
            long duration = 0;
            if (pair.getLastUpdated() != null) {
                duration = new Date().getTime() - pair.getLastUpdated().getTime();
            } else {
                cooldown = 0;
            }
            if (duration < cooldown) {
                fxcbit.getSupportedPairs().add(pair);
            } else {
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JsonObjectRequest request = new JsonObjectRequest(pair.getApiURL(), null, future, future);
                requestQueue.add(request);

                try {
                    JSONObject response = future.get(GLOBAL_DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

                    if (pair instanceof RealPair) {
                        pair = updatePair(pair, response, new FXCBitJsonParser());
                    }
                    pair.setLastUpdated(new Date());
                } catch (TimeoutException e) {
                    String errorMessage = MessageFormat.format(EXCEPTION_PATTERN_API_TIMEOUT, exchange.getName(), GLOBAL_DEFAULT_TIMEOUT_SECONDS, pair.getCoin());
                    Log.e(this.getClass().getName(), errorMessage);
                    pair = setPairTickerPriceErrorValues(pair);
                    super.errors.add(new CrsRestException(errorMessage, e));
                } catch (InterruptedException | ExecutionException e) {
                    if (e.getCause() instanceof VolleyError) {
                        VolleyError volleyError = (VolleyError) e.getCause();
                        NetworkResponse networkResponse = volleyError.networkResponse;
                        String errorMessage;
                        if (networkResponse.statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                            errorMessage = MessageFormat.format(EXCEPTION_PATTERN_API_STATUS_500, exchange.getName(), pair.getCoin());
                        } else {
                            errorMessage = MessageFormat.format(EXCEPTION_PATTERN_API_NOT_TREATED, exchange.getName(), pair.getCoin());
                        }
                        Log.e(this.getClass().getName(), errorMessage);
                        pair = setPairTickerPriceErrorValues(pair);
                        super.errors.add(new CrsRestException(errorMessage, e));
                    }
                } catch (JSONException e) {
                    String errorMessage = MessageFormat.format(EXCEPTION_PATTERN_API_INVALID_JSON, exchange.getName(), pair.getCoin());
                    Log.e(this.getClass().getName(), errorMessage);
                    pair = setPairTickerPriceErrorValues(pair);
                    super.errors.add(new CrsRestException(errorMessage, e));
                } finally {
                    fxcbit.getSupportedPairs().add(pair);
                }
            }
        }
        return fxcbit;
    }

    @Override
    protected void onPostExecute(Exchange updatedExchange) {
        if (super.errors.size() == 0) {
            if (super.infoListener instanceof MainInfoListener) {
                ((MainInfoListener) super.infoListener).onTaskCompleted(updatedExchange);
            } else {
                ((WidgetInfoListener) super.infoListener).onTaskCompleted(super.widgetTracker, updatedExchange);
            }
        } else {
            if (super.infoListener instanceof MainInfoListener) {
                ((MainInfoListener) super.infoListener).onTaskError(super.errors, updatedExchange);
            } else {
                String errorMessage = super.errors.get(0).getMessage();
                ((WidgetInfoListener) super.infoListener).onTaskError(super.widgetTracker, updatedExchange, errorMessage);
            }
        }
    }

    private Pair updatePair(Pair pair, JSONObject jCryptoPair, CrsJsonParser jsonParser) throws JSONException {
        pair.getTickerPrices().clear();
        List<TickerPrice> updatedTickerPrices = jsonParser.parseJson(jCryptoPair);
        pair.setTickerPrices(updatedTickerPrices);
        return pair;
    }

    private Pair setPairTickerPriceErrorValues(Pair pair) {
        TickerPrice tickerPrice = new TickerPrice(EXCEPTION_TICKER_LABEL, EXCEPTION_TICKER_DESCRIPTION);
        pair.getTickerPrices().clear();
        pair.getTickerPrices().add(tickerPrice);
        return pair;
    }
}
