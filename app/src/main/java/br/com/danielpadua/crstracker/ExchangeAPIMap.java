package br.com.danielpadua.crstracker;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import br.com.danielpadua.crstracker.model.CrsInfoListener;
import br.com.danielpadua.crstracker.model.exchange.Exchange;
import br.com.danielpadua.crstracker.model.exchange.Southxchange;
import br.com.danielpadua.crstracker.restClient.RestClient;
import br.com.danielpadua.crstracker.restClient.SouthxchangeRestClient;

/**
 * Created by danielpadua on 14/03/2018.
 */

public final class ExchangeAPIMap {
    public static Map<Exchange, RestClient> getMap(final Context context, final CrsInfoListener infoListener) {
        return new HashMap<Exchange, RestClient>() {{
            put(new Southxchange(), new SouthxchangeRestClient(context, infoListener));
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
