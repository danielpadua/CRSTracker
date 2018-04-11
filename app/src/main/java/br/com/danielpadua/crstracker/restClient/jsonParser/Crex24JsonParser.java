package br.com.danielpadua.crstracker.restClient.jsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CREX24_HIGH_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CREX24_LAST_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CREX24_LOW_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CREX24_PERCENT_CHANGE_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CREX24_TICKERS_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_CREX24_VOLUME_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_TICKER_24H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.validateNullDoubleAPI;

public class Crex24JsonParser implements CrsJsonParser {
    @Override
    public List<TickerPrice> parseJson(JSONObject jsonObject) throws JSONException {
        List<TickerPrice> crex24TickerPrices = new ArrayList<>();

        // get 24h ticker
        JSONArray root = jsonObject.getJSONArray(JSON_CREX24_TICKERS_LABEL);
        Double last = validateNullDoubleAPI(root.getJSONObject(0).getString(JSON_CREX24_LAST_LABEL));
        Double low = validateNullDoubleAPI(root.getJSONObject(0).getString(JSON_CREX24_LOW_LABEL));
        Double high = validateNullDoubleAPI(root.getJSONObject(0).getString(JSON_CREX24_HIGH_LABEL));
        Double variation = validateNullDoubleAPI(root.getJSONObject(0).getString(JSON_CREX24_PERCENT_CHANGE_LABEL));
        Double volume = validateNullDoubleAPI(root.getJSONObject(0).getString(JSON_CREX24_VOLUME_LABEL));
        TickerPrice crex24Ticker24h = new TickerPrice(JSON_TICKER_24H_LABEL, GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION, TickerType.TICKER24H, last, high, low, volume, 0);
        crex24Ticker24h.setVariation(variation);
        crex24TickerPrices.add(crex24Ticker24h);

        return crex24TickerPrices;
    }
}
