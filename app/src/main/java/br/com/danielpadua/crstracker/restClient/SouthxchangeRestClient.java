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
import br.com.danielpadua.crstracker.model.exchange.Southxchange;
import br.com.danielpadua.crstracker.model.pair.Pair;
import br.com.danielpadua.crstracker.model.pair.RealPair;
import br.com.danielpadua.crstracker.model.pair.RelativePair;
import br.com.danielpadua.crstracker.model.widget.WidgetInfoListener;
import br.com.danielpadua.crstracker.model.widget.WidgetTracker;
import br.com.danielpadua.crstracker.restClient.jsonParser.BitvalorJsonParser;
import br.com.danielpadua.crstracker.restClient.jsonParser.CrsJsonParser;
import br.com.danielpadua.crstracker.restClient.jsonParser.SouthxchangeJsonParser;
import br.com.danielpadua.crstracker.util.CRSUtil;

import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_PATTERN_API_INVALID_JSON;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_PATTERN_API_NOT_TREATED;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_PATTERN_API_STATUS_500;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_PATTERN_API_TIMEOUT;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_TICKER_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_TIMEOUT_SECONDS;
import static br.com.danielpadua.crstracker.util.CRSUtil.SHARED_PREFERENCES_SOUTHXCHANGE;

/**
 * Created by danielpadua on 11/03/2018.
 */

public class SouthxchangeRestClient extends RestClient<Southxchange> {

    public SouthxchangeRestClient(Context context, CrsInfoListener infoListener) {
        super(context, infoListener, Southxchange.class);
    }

    @Override
    protected Exchange doInBackground(WidgetTracker... widgetTrackers) {
        if (widgetTrackers != null && widgetTrackers.length > 0) {
            super.widgetTracker = widgetTrackers[0];
        }

        Southxchange southxchange = repository.get(CRSUtil.SHARED_PREFERENCES_SOUTHXCHANGE);

        if (southxchange == null) {
            southxchange = new Southxchange();
            southxchange = (Southxchange) updateExchange(southxchange);
            southxchange.setLastUpdated(new Date());
            repository.persist(SHARED_PREFERENCES_SOUTHXCHANGE, southxchange);
        } else {
            long duration = (new Date().getTime() - southxchange.getLastUpdated().getTime());
            long cooldown = TimeUnit.MILLISECONDS.convert(southxchange.getRefreshCooldownSeconds(), TimeUnit.SECONDS);
            if (duration > cooldown) {
                southxchange = new Southxchange();
            }
            southxchange = (Southxchange) updateExchange(southxchange);
            southxchange.setLastUpdated(new Date());
            repository.persist(SHARED_PREFERENCES_SOUTHXCHANGE, southxchange);
        }


        return southxchange;
    }

    @Override
    protected Exchange updateExchange(Exchange exchange) {
        Southxchange southxchange = new Southxchange();
        southxchange.getSupportedPairs().clear();
        for (Pair pair : exchange.getSupportedPairs()) {
            long cooldown = TimeUnit.MILLISECONDS.convert(pair.getApiCooldownSeconds(), TimeUnit.SECONDS);
            long duration = 0;
            if (pair.getLastUpdated() != null) {
                duration = new Date().getTime() - pair.getLastUpdated().getTime();
            } else {
                cooldown = 0;
            }
            if (duration < cooldown) {
                southxchange.getSupportedPairs().add(pair);
            } else {
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JsonObjectRequest request = new JsonObjectRequest(pair.getApiURL(), null, future, future);
                requestQueue.add(request);

                try {
                    JSONObject response = future.get(GLOBAL_DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

                    if (pair instanceof RealPair) {
                        pair = updatePair(pair, response, new SouthxchangeJsonParser());
                    } else {
                        if (pair.getCoin().equals("BRL")) {
                            pair = updatePair((RelativePair) pair, exchange.getBitcoinPair(), response, new BitvalorJsonParser());
                        } else {
                            pair = updatePair((RelativePair) pair, exchange.getBitcoinPair(), response, new SouthxchangeJsonParser());
                        }
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
                    southxchange.getSupportedPairs().add(pair);
                }
            }
        }
        return southxchange;
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

    private Pair updatePair(RelativePair relativePair, Pair bitcoinPair, JSONObject jFiatPair, CrsJsonParser jsonParser) throws JSONException {
        RelativePair p = (RelativePair) updatePair(relativePair, jFiatPair, jsonParser);
        if (p.isFiat()) {
            p = loadCrsFiatPrice(p, bitcoinPair);
        }
        return p;
    }

    private RelativePair loadCrsFiatPrice(RelativePair relativePair, Pair bitcoinPair) {
        for (TickerPrice tpBtc : bitcoinPair.getTickerPrices()) {
            for (TickerPrice tpFiat : relativePair.getTickerPrices()) {
                if (tpBtc.getTickerLabel().equals(tpFiat.getTickerLabel())) {
                    double btcPrice = tpBtc.getLast();
                    double fiatPrice = tpFiat.getLast();
                    relativePair.setCrsPrice(btcPrice * fiatPrice);
                    relativePair.setBtcConversionPrice(btcPrice);
                }
            }
        }
        return relativePair;
    }

    private Pair setPairTickerPriceErrorValues(Pair pair) {
        TickerPrice tickerPrice = new TickerPrice(EXCEPTION_TICKER_LABEL, EXCEPTION_TICKER_DESCRIPTION);
        pair.getTickerPrices().clear();
        pair.getTickerPrices().add(tickerPrice);
        return pair;
    }
}
