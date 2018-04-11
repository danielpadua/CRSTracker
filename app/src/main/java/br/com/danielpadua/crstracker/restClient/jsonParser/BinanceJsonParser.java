package br.com.danielpadua.crstracker.restClient.jsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BINANCE_PRICE_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_TICKER_24H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.validateNullDoubleAPI;

/**
 * Created by danielpadua on 29/03/2018.
 */

public class BinanceJsonParser implements CrsJsonParser {
    @Override
    public List<TickerPrice> parseJson(JSONObject jsonObject) throws JSONException {
        List<TickerPrice> binanceTickerPrices = new ArrayList<>();

        // get 24h ticker

        Double price = validateNullDoubleAPI(jsonObject.getString(JSON_BINANCE_PRICE_LABEL));
        TickerPrice binanceTicker24h = new TickerPrice(JSON_TICKER_24H_LABEL, GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION, TickerType.TICKER24H, price, 0, 0, 0, 0);
        binanceTickerPrices.add(binanceTicker24h);

        return binanceTickerPrices;
    }
}
