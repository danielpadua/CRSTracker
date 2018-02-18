package br.com.danielpadua.crstracker;

/**
 * Created by danielpadua on 13/02/2018.
 */

class Pair {
    private SupportedCoins coin;
    private String apiURL;
    private String price;
    private String volume;
    private String variation;

    public Pair() {
    }

    public Pair(SupportedCoins coin, String apiURL, String price, String volume, String variation) {
        setCoin(coin);
        setApiURL(apiURL);
        setPrice(price);
        setVolume(volume);
        setVariation(variation);
    }

    public SupportedCoins getCoin() {
        return coin;
    }

    public void setCoin(SupportedCoins coin) {
        this.coin = coin;
    }

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }


}
