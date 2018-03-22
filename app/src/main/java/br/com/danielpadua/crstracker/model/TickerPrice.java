package br.com.danielpadua.crstracker.model;

/**
 * Created by danielpadua on 11/03/2018.
 */

public class TickerPrice {
    private String tickerLabel;
    private String tickerDescription;
    private TickerType tickerType;
    private double last;
    private double bid;
    private double ask;
    private double variation;
    private double volume;
    private double high;
    private double low;
    private long tradeCount;

    public TickerPrice() {
    }

    public TickerPrice(String tickerLabel, String tickerDescription) {
        setTickerLabel(tickerLabel);
        setTickerDescription(tickerDescription);
    }

    public TickerPrice(String tickerLabel, String tickerDescription, TickerType tickerType, double bid, double ask, double last, double variation, double volume) {
        setTickerLabel(tickerLabel);
        setTickerDescription(tickerDescription);
        setTickerType(tickerType);
        setBid(bid);
        setAsk(ask);
        setLast(last);
        setVariation(variation);
        setVolume(volume);
    }

    public TickerPrice(String tickerLabel, String tickerDescription, TickerType tickerType, double last, double high, double low, double volume, long tradeCount) {
        setTickerLabel(tickerLabel);
        setTickerDescription(tickerDescription);
        setTickerType(tickerType);
        setLast(last);
        setHigh(high);
        setLow(low);
        setVolume(volume);
        setTradeCount(tradeCount);
    }

    public String getTickerLabel() {
        return tickerLabel;
    }

    public void setTickerLabel(String tickerLabel) {
        this.tickerLabel = tickerLabel;
    }

    public String getTickerDescription() {
        return tickerDescription;
    }

    public void setTickerDescription(String tickerDescription) {
        this.tickerDescription = tickerDescription;
    }

    public TickerType getTickerType() {
        return tickerType;
    }

    public void setTickerType(TickerType tickerType) {
        this.tickerType = tickerType;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getVariation() {
        return variation;
    }

    public void setVariation(double variation) {
        this.variation = variation;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(long tradeCount) {
        this.tradeCount = tradeCount;
    }
}
