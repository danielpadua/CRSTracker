package br.com.danielpadua.crstracker.model.widget;

/**
 * Created by danielpadua on 16/03/2018.
 */

public enum InfoOption {
    Price("Preço"),
    Bid("Bid"),
    Ask("Ask"),
    Volume("Volume"),
    Variation("Variação"),
    High("High"),
    Low("Low"),
    TradeCount("Qtde Trades"),
    BtcPrice("Preço BTC");

    private final String description;

    InfoOption(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
