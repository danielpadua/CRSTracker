package br.com.danielpadua.crstracker;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import br.com.danielpadua.crstracker.model.CrsInfoListener;
import br.com.danielpadua.crstracker.model.exchange.CFinex;
import br.com.danielpadua.crstracker.model.exchange.Crex24;
import br.com.danielpadua.crstracker.model.exchange.Exchange;
import br.com.danielpadua.crstracker.restClient.CFinexRestClient;
import br.com.danielpadua.crstracker.restClient.Crex24RestClient;
import br.com.danielpadua.crstracker.restClient.RestClient;

/**
 * Created by danielpadua on 14/03/2018.
 */

public final class ExchangeAPIMap {
    public static Map<Exchange, RestClient> getMap(final Context context, final CrsInfoListener infoListener) {
        return new HashMap<Exchange, RestClient>() {{
            //put(new Southxchange(), new SouthxchangeRestClient(context, infoListener));
            put(new CFinex(), new CFinexRestClient(context, infoListener));
            put(new Crex24(), new Crex24RestClient(context, infoListener));
        }};
    }

    public static RestClient getRestClientByExchange(Context context, CrsInfoListener infoListener, final Exchange exchange) {
        for (Map.Entry<Exchange, RestClient> entry : getMap(context, infoListener).entrySet()) {
            if (entry.getKey().getId() == exchange.getId()) {
                return entry.getValue();
            }
        }
        return null;
    }
}
