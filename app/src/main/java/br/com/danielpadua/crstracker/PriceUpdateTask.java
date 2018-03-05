package br.com.danielpadua.crstracker;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static br.com.danielpadua.crstracker.CRSUtil.CRS_DEFAULT_REQUEST_TIMEOUT;
import static br.com.danielpadua.crstracker.CRSUtil.getTotalPairCount;
import static br.com.danielpadua.crstracker.CRSUtil.supportedExchanges;

/**
 * Created by danielpadua on 15/02/2018.
 */

class PriceUpdateTask extends AsyncTask<Void, Void, Void> {

    private final Context context;
    private final OnTaskCompleted listener;
    private int[] widgetIds;
    private int requestCounter;
    private Pair btcPair;
    private Pair brlPair;


    PriceUpdateTask(Context context, OnTaskCompleted listener) {
        this.context = context;
        this.listener = listener;
        this.widgetIds = null;
        this.requestCounter = 0;
        this.btcPair = null;
        this.brlPair = null;
    }

    PriceUpdateTask(Context context, OnTaskCompleted listener, int[] widgetIds) {
        this.context = context;
        this.listener = listener;
        this.widgetIds = widgetIds;
        this.requestCounter = 0;
        this.btcPair = null;
        this.brlPair = null;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);

        for (final Exchange e : supportedExchanges) {
            for (final Pair p : e.getSupportedPairs()) {
                JsonObjectRequest jReq = new JsonObjectRequest(Request.Method.GET, p.getApiURL(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.length() > 0) {
                                    try {
                                        //double bid = Double.parseDouble(response.get("Bid").toString());
                                        //double ask = Double.parseDouble(response.get("Ask").toString());
                                        if (p.getCoin() == SupportedCoins.BTC) btcPair = p;

                                        if (p.getCoin() == SupportedCoins.BRL) {
                                            p.setPrice(response.get("last").toString());
                                            brlPair = p;
                                        } else {

                                            double last = response.get("Last").toString() != "null" ? Double.parseDouble(response.get("Last").toString()) : 0.0;
                                            double variation24Hr = response.get("Variation24Hr").toString() != "null" ? Double.parseDouble(response.get("Variation24Hr").toString()) : 0.0;
                                            double volume24Hr = Double.parseDouble(response.get("Volume24Hr").toString());
                                            if (p.getCoin() == SupportedCoins.USD)
                                                p.setPrice(String.format(Locale.ROOT, "%.2f", last).replace(",", "."));
                                            else
                                                p.setPrice(String.format(Locale.ROOT, "%.8f", last).replace(",", "."));
                                            p.setVariation(String.format(Locale.ROOT, "%.2f", variation24Hr).replace(",", ".") + "%");
                                            p.setVolume(String.format(Locale.ROOT, "%.2f", volume24Hr).replace(",", "."));
                                        }

                                        requestCounter++;

                                        if (requestCounter == getTotalPairCount()) {
                                            double brlPrice = Double.parseDouble(brlPair.getPrice());
                                            double btcPrice = Double.parseDouble(btcPair.getPrice());
                                            brlPair.setPrice(String.format(Locale.ROOT, "%.2f", (brlPrice * btcPrice)));
                                            brlPair.setVariation(btcPair.getVariation());
                                            requestCounter = 0;
                                            if (widgetIds != null)
                                                listener.onTaskCompleted(widgetIds);
                                            else
                                                listener.onTaskCompleted();
                                        }


                                    } catch (JSONException e) {
                                        Log.e("Volley", "Invalid JSON Object.");
                                    }
                                } else {
                                    Log.e("Volley", "Exchange didn't return any data.");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", error.toString());
                            }
                        }
                );

                jReq.setRetryPolicy(new DefaultRetryPolicy(
                        CRS_DEFAULT_REQUEST_TIMEOUT,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(jReq);
            }
        }

        return null;
    }
}
