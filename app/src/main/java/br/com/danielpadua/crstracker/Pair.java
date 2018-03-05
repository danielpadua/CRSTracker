package br.com.danielpadua.crstracker;

import java.io.Serializable;

/**
 * Created by danielpadua on 13/02/2018.
 */

class Pair implements Serializable {
    private int id;
    private SupportedCoins coin;
    private String apiURL;
    private String price;
    private String volume;
    private String variation;
    private int widgetId;
    private String pairURL;

    Pair() {
    }

    Pair(int id, SupportedCoins coin, String apiURL, String price, String volume, String variation, int widgetId, String pairURL) {
        setId(id);
        setCoin(coin);
        setApiURL(apiURL);
        setPrice(price);
        setVolume(volume);
        setVariation(variation);
        setWidgetId(widgetId);
        setPairURL(pairURL);
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    SupportedCoins getCoin() {
        return coin;
    }

    void setCoin(SupportedCoins coin) {
        this.coin = coin;
    }

    String getApiURL() {
        return apiURL;
    }

    void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    String getPrice() {
        return price;
    }

    void setPrice(String price) {
        this.price = price;
    }

    String getVolume() {
        return volume;
    }

    void setVolume(String volume) {
        this.volume = volume;
    }

    String getVariation() {
        return variation;
    }

    void setVariation(String variation) {
        this.variation = variation;
    }

    int getWidgetId() {
        return widgetId;
    }

    void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    String getPairURL() {
        return pairURL;
    }

    void setPairURL(String pairURL) {
        this.pairURL = pairURL;
    }

    @Override
    public String toString() {
        return "CRS/" + getCoin().toString();
    }


}
