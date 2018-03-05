package br.com.danielpadua.crstracker;

import java.util.ArrayList;

/**
 * Created by danielpadua on 14/02/2018.
 */

class CRSUtil {

    static final int CRS_DEFAULT_REQUEST_TIMEOUT = 10000;

    static final String CRS_WIDGET_PREFERENCES = "CRS_WIDGET_PREFERENCES";

    static final Exchange[] supportedExchanges = {
            new Exchange(new ArrayList<Pair>() {{
                add(new Pair(1, SupportedCoins.BTC, "https://www.southxchange.com/api/price/CRS/BTC", null, null, null, -1, "https://www.southxchange.com/Market/Book/CRS/BTC"));
                add(new Pair(2, SupportedCoins.BRL, "https://api.blinktrade.com/api/v1/BRL/ticker?crypto_currency=BTC", null, null, null, -1, null));
                add(new Pair(3, SupportedCoins.USD, "https://www.southxchange.com/api/price/CRS/USD", null, null, null, -1, "https://www.southxchange.com/Market/Book/CRS/USD"));
                add(new Pair(4, SupportedCoins.DASH, "https://www.southxchange.com/api/price/CRS/DASH", null, null, null, -1, "https://www.southxchange.com/Market/Book/CRS/DASH"));
            }}, 1, "Southxchange")};


    public static int getTotalPairCount() {
        int pairCount = 0;
        for (Exchange e : supportedExchanges) {
            for (Pair ignored : e.getSupportedPairs()) {
                pairCount++;
            }
        }
        return pairCount;
    }
}
