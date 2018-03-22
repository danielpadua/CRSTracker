package br.com.danielpadua.crstracker.model.pair;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;

/**
 * Created by danielpadua on 13/02/2018.
 */

public abstract class Pair {
    private int id;
    private String coin;
    private List<TickerPrice> tickerPrices;
    private String tradeURL;
    private String apiURL;
    private boolean isFiat;
    private int apiCooldownSeconds;
    private Date lastUpdated;

    public Pair() {
    }

    public Pair(int id, String coin, List<TickerPrice> tickerPrices, String tradeURL, String apiURL, boolean isFiat, int apiCooldownSeconds, Date lastUpdated) {
        setId(id);
        setCoin(coin);
        setTickerPrices(tickerPrices);
        setTradeURL(tradeURL);
        setApiURL(apiURL);
        setFiat(isFiat);
        setApiCooldownSeconds(apiCooldownSeconds);
        setLastUpdated(lastUpdated);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public List<TickerPrice> getTickerPrices() {
        return tickerPrices;
    }

    public void setTickerPrices(List<TickerPrice> tickerPrices) {
        this.tickerPrices = tickerPrices;
    }

    public String getTradeURL() {
        return tradeURL;
    }

    public void setTradeURL(String tradeURL) {
        this.tradeURL = tradeURL;
    }

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public TickerPrice getTickerByType(final TickerType tickerType) {
        return Iterables.find(getTickerPrices(), new Predicate<TickerPrice>() {
            @Override
            public boolean apply(@Nullable TickerPrice input) {
                return input != null && input.getTickerType() == tickerType;
            }
        });
    }

    public boolean isFiat() {
        return isFiat;
    }

    public void setFiat(boolean fiat) {
        isFiat = fiat;
    }

    @Override
    public String toString() {
        return "CRS/" + getCoin();
    }

    public int getApiCooldownSeconds() {
        return apiCooldownSeconds;
    }

    public void setApiCooldownSeconds(int apiCooldownSeconds) {
        this.apiCooldownSeconds = apiCooldownSeconds;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
