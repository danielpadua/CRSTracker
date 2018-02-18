package br.com.danielpadua.crstracker;

import java.util.ArrayList;

/**
 * Created by danielpadua on 14/02/2018.
 */

class CRSUtil {

    public static final int CRS_DEFAULT_REQUEST_TIMEOUT = 7000;

    public static final Exchange[] supportedExchanges = {
            new Exchange(new ArrayList<Pair>() {{
                add(new Pair(SupportedCoins.BTC, "https://www.southxchange.com/api/price/CRS/BTC", null, null, null));
                add(new Pair(SupportedCoins.USD, "https://www.southxchange.com/api/price/CRS/USD", null, null, null));
            }}, "Southxchange")
    };

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
