package br.com.danielpadua.crstracker.restClient;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.CrsInfoListener;
import br.com.danielpadua.crstracker.model.exchange.Exchange;
import br.com.danielpadua.crstracker.model.widget.WidgetTracker;
import br.com.danielpadua.crstracker.persistance.Repository;

/**
 * Created by danielpadua on 14/03/2018.
 */

public abstract class RestClient<T extends Exchange> extends AsyncTask<WidgetTracker, Void, Exchange> {
    protected final List<CrsRestException> errors;
    protected final CrsInfoListener infoListener;
    protected final RequestQueue requestQueue;
    protected final Repository<T> repository;
    protected WidgetTracker widgetTracker;

    public RestClient(Context context, CrsInfoListener infoListener, Class<T> type) {
        this.infoListener = infoListener;
        this.requestQueue = Volley.newRequestQueue(context);
        this.errors = new ArrayList<>();
        this.repository = new Repository<>(context, type);
    }

    protected abstract Exchange updateExchange(Exchange exchange);
    //protected abstract Pair updatePair(Pair pair);
}
