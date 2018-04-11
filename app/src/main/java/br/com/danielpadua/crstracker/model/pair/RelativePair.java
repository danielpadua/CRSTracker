package br.com.danielpadua.crstracker.model.pair;

import java.util.Date;
import java.util.List;

import br.com.danielpadua.crstracker.CRSTracker;
import br.com.danielpadua.crstracker.R;
import br.com.danielpadua.crstracker.model.TickerPrice;

/**
 * Created by danielpadua on 11/03/2018.
 */

public class RelativePair extends Pair {
    private double crsPrice;
    private double btcConversionPrice;

    public RelativePair() {
    }

    public RelativePair(int id, String coin, List<TickerPrice> tickerPrices, String apiURL, double crsPrice, double btcConversionPrice, boolean isFiat, int apiCooldownSeconds, Date lastUpdated) {
        super.setId(id);
        super.setCoin(coin);
        super.setTickerPrices(tickerPrices);
        super.setApiURL(apiURL);
        super.setFiat(isFiat);
        super.setApiCooldownSeconds(apiCooldownSeconds);
        setCrsPrice(crsPrice);
        setBtcConversionPrice(btcConversionPrice);
        setLastUpdated(lastUpdated);
    }

    public double getCrsPrice() {
        return crsPrice;
    }

    public void setCrsPrice(double crsPrice) {
        this.crsPrice = crsPrice;
    }

    public double getBtcConversionPrice() {
        return btcConversionPrice;
    }

    public void setBtcConversionPrice(double btcConversionPrice) {
        this.btcConversionPrice = btcConversionPrice;
    }

    @Override
    public String toString() {
        return "CRS/" + getCoin() + " - " + CRSTracker.getAppContext().getString(R.string.main_relative_pair);
    }
}
