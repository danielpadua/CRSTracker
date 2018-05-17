package br.com.danielpadua.crstracker.restClient.jsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_FXCBIT_LAST_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_TICKER_24H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.validateNullDoubleAPI;

public class FXCBitJsonParser implements CrsJsonParser {
    @Override
    public List<TickerPrice> parseJson(JSONObject jsonObject) throws JSONException {
        List<TickerPrice> fxcbitTickerPrices = new ArrayList<>();

        // get 24h ticker
        Double last = validateNullDoubleAPI(jsonObject.getString(JSON_FXCBIT_LAST_LABEL));
        TickerPrice crex24Ticker24h = new TickerPrice(JSON_TICKER_24H_LABEL, GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION, TickerType.TICKER24H, last, 0, 0, 0, 0);
        fxcbitTickerPrices.add(crex24Ticker24h);

        return fxcbitTickerPrices;
    }
}
