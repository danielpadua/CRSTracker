package br.com.danielpadua.crstracker.model.exchange;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;
import br.com.danielpadua.crstracker.model.pair.Pair;
import br.com.danielpadua.crstracker.model.pair.RealPair;
import br.com.danielpadua.crstracker.model.pair.RelativePair;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_BITVALOR_API_CALL_TIMER;
import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_SOUTHXCHANGE_API_CALL_TIMER;

/**
 * Created by danielpadua on 11/03/2018.
 */

public class Southxchange extends Exchange {

    private Pair bitcoinPair;
    private List<TickerType> supportedTickerTypes;
    private List<Pair> supportedPairs;


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Pair getBitcoinPair() {
        if (this.bitcoinPair == null) {
            this.bitcoinPair = new RealPair(0, "BTC", new ArrayList<TickerPrice>(), "https://www.southxchange.com/Market/Book/CRS/BTC", "https://www.southxchange.com/api/price/CRS/BTC", false, GLOBAL_SOUTHXCHANGE_API_CALL_TIMER, null);
        }
        return this.bitcoinPair;
    }

    @Override
    public List<TickerType> getSupportedTickerTypes() {
        if (this.supportedTickerTypes == null) {
            this.supportedTickerTypes = new ArrayList<TickerType>() {{
                add(TickerType.TICKER24H);
            }};
        }
        return this.supportedTickerTypes;
    }

    @Override
    public List<Pair> getSupportedPairs() {
        if (this.supportedPairs == null) {
            this.supportedPairs = new ArrayList<Pair>() {{
                add(getBitcoinPair());
                add(new RealPair(1, "DASH", new ArrayList<TickerPrice>(), "https://www.southxchange.com/Market/Book/CRS/DASH", "https://www.southxchange.com/api/price/CRS/DASH", false, GLOBAL_SOUTHXCHANGE_API_CALL_TIMER, null));
                add(new RealPair(2, "LTC", new ArrayList<TickerPrice>(), "https://www.southxchange.com/Market/Book/CRS/LTC", "https://www.southxchange.com/api/price/CRS/LTC", false, GLOBAL_SOUTHXCHANGE_API_CALL_TIMER, null));
                add(new RealPair(3, "USD", new ArrayList<TickerPrice>(), "https://www.southxchange.com/Market/Book/CRS/USD", "https://www.southxchange.com/api/price/CRS/USD", true, GLOBAL_SOUTHXCHANGE_API_CALL_TIMER, null));
                add(new RelativePair(4, "BRL", new ArrayList<TickerPrice>(), "https://api.bitvalor.com/v1/ticker.json", 0, 0, true, GLOBAL_BITVALOR_API_CALL_TIMER, null));
                add(new RelativePair(5, "USD", new ArrayList<TickerPrice>(), "https://www.southxchange.com/api/price/BTC/USD", 0, 0, true, GLOBAL_SOUTHXCHANGE_API_CALL_TIMER, null));
            }};
        }
        return this.supportedPairs;
    }

    @Override
    public int getRefreshCooldownSeconds() {
        return GLOBAL_SOUTHXCHANGE_API_CALL_TIMER;
    }

    @Override
    public String getName() {
        return "Southxchange";
    }
}
