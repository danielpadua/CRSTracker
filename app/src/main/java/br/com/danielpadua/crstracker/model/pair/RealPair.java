package br.com.danielpadua.crstracker.model.pair;

import java.util.Date;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;

/**
 * Created by danielpadua on 11/03/2018.
 */

public class RealPair extends Pair {
    public RealPair() {
    }

    public RealPair(int id, String coin, List<TickerPrice> tickerPrices, String tradeURL, String apiURL, boolean isFiat, int apiCooldownSeconds, Date lastUpdated) {
        super(id, coin, tickerPrices, tradeURL, apiURL, isFiat, apiCooldownSeconds, lastUpdated);
    }
}
