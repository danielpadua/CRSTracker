package br.com.danielpadua.crstracker.model.widget;

import br.com.danielpadua.crstracker.CRSTracker;
import br.com.danielpadua.crstracker.R;

/**
 * Created by danielpadua on 16/03/2018.
 */

public enum InfoOption {
    Price(R.string.infoOption_price),
    Bid(R.string.infoOption_bid),
    Ask(R.string.infoOption_ask),
    Volume(R.string.infoOption_volume),
    Variation(R.string.infoOption_variation),
    High(R.string.infoOption_high),
    Low(R.string.infoOption_low),
    TradeCount(R.string.infoOption_trade_count),
    BtcPrice(R.string.infoOption_btc_price),
    Empty(R.string.infoOption_empty);

    private int resourceId;

    InfoOption(int id) {
        resourceId = id;
    }

    @Override
    public String toString() {
        return CRSTracker.getAppContext().getString(resourceId);
    }
}
