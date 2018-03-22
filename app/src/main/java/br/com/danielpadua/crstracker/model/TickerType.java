package br.com.danielpadua.crstracker.model;

/**
 * Created by danielpadua on 18/03/2018.
 */

public enum TickerType {
    TICKER24H("Ticker 24h"),
    TICKER12H("Ticker 12h"),
    TICKER1H("Ticker 1h");

    private final String description;

    TickerType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
