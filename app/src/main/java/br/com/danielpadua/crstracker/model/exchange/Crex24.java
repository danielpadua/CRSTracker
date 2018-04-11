package br.com.danielpadua.crstracker.model.exchange;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;
import br.com.danielpadua.crstracker.model.pair.Pair;
import br.com.danielpadua.crstracker.model.pair.RealPair;
import br.com.danielpadua.crstracker.model.pair.RelativePair;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_BINANCE_API_CALL_TIMER;
import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_BITVALOR_API_CALL_TIMER;
import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_CREX24_API_CALL_TIMER;

public class Crex24 extends Exchange {
    private Pair bitcoinPair;
    private List<TickerType> supportedTickerTypes;
    private List<Pair> supportedPairs;

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public Pair getBitcoinPair() {
        if (this.bitcoinPair == null) {
            this.bitcoinPair = new RealPair(0, "BTC", new ArrayList<TickerPrice>(), "https://crex24.com/exchange/CRS-BTC", "https://api.crex24.com/CryptoExchangeService/BotPublic/ReturnTicker?request=[NamePairs=BTC_CRS]", false, GLOBAL_CREX24_API_CALL_TIMER, null);
        }
        return this.bitcoinPair;
    }

    @Override
    public List<Pair> getSupportedPairs() {
        if (this.supportedPairs == null) {
            this.supportedPairs = new ArrayList<Pair>() {{
                add(getBitcoinPair());
                add(new RelativePair(1, "BRL", new ArrayList<TickerPrice>(), "https://api.bitvalor.com/v1/ticker.json", 0, 0, true, GLOBAL_BITVALOR_API_CALL_TIMER, null));
                add(new RelativePair(2, "USDT", new ArrayList<TickerPrice>(), "https://api.binance.com/api/v1/ticker/price?symbol=BTCUSDT", 0, 0, true, GLOBAL_BINANCE_API_CALL_TIMER, null));
            }};
        }
        return this.supportedPairs;
    }

    @Override
    public String getName() {
        return "Crex24";
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
        return GLOBAL_CREX24_API_CALL_TIMER;
    }
}
