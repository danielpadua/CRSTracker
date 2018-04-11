package br.com.danielpadua.crstracker.restClient.jsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CFINEX_ASK_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CFINEX_BID_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CFINEX_LOCALVOLUME_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CFINEX_MAINPAIR_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CFINEX_PERCENT_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CFINEX_PRICE_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_TICKER_24H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.validateNullDoubleAPI;

/**
 * Created by danielpadua on 29/03/2018.
 */

public class CFinexJsonParser implements CrsJsonParser {
    @Override
    public List<TickerPrice> parseJson(JSONObject jsonObject) throws JSONException {
        List<TickerPrice> cfinexTickerPrices = new ArrayList<>();

        // get 24h ticker
        Double bid = validateNullDoubleAPI(jsonObject.getJSONObject(JSON_CFINEX_BID_LABEL).getString(JSON_CFINEX_PRICE_LABEL));
        Double ask = validateNullDoubleAPI(jsonObject.getJSONObject(JSON_CFINEX_ASK_LABEL).getString(JSON_CFINEX_PRICE_LABEL));
        Double price = validateNullDoubleAPI(jsonObject.getJSONObject(JSON_CFINEX_MAINPAIR_LABEL).getString(JSON_CFINEX_PRICE_LABEL));
        Double percent = validateNullDoubleAPI(jsonObject.getJSONObject(JSON_CFINEX_MAINPAIR_LABEL).getString(JSON_CFINEX_PERCENT_LABEL));
        Double volume = validateNullDoubleAPI(jsonObject.getJSONObject(JSON_CFINEX_MAINPAIR_LABEL).getString(JSON_CFINEX_LOCALVOLUME_LABEL));
        TickerPrice cFinexTicker24h = new TickerPrice(JSON_TICKER_24H_LABEL, GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION, TickerType.TICKER24H, bid, ask, price, percent, volume);
        cfinexTickerPrices.add(cFinexTicker24h);

        return cfinexTickerPrices;
    }
}
