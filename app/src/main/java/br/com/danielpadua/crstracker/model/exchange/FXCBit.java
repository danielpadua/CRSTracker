package br.com.danielpadua.crstracker.model.exchange;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;
import br.com.danielpadua.crstracker.model.pair.Pair;
import br.com.danielpadua.crstracker.model.pair.RealPair;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_FXCBIT_API_CALL_TIMER;

public class FXCBit extends Exchange {
    private List<TickerType> supportedTickerTypes;
    private List<Pair> supportedPairs;

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public Pair getBitcoinPair() {
        return null;
    }

    @Override
    public List<Pair> getSupportedPairs() {
        if (this.supportedPairs == null) {
            this.supportedPairs = new ArrayList<Pair>() {{
                add(new RealPair(0, "BRL", new ArrayList<TickerPrice>(), "https://platform.fxcbit.com/exchange?c_currency=95", "https://fxcbit.com/api", true, GLOBAL_FXCBIT_API_CALL_TIMER, null));
            }};
        }
        return this.supportedPairs;
    }

    @Override
    public String getName() {
        return "FXCBit";
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
    public int getRefreshCooldownSeconds() {
        return GLOBAL_FXCBIT_API_CALL_TIMER;
    }
}
