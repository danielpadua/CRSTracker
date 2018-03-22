package br.com.danielpadua.crstracker.restClient.jsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_SOUTHXCHANGE_ASK_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_SOUTHXCHANGE_BID_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_SOUTHXCHANGE_LAST_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_SOUTHXCHANGE_VARIATION_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_SOUTHXCHANGE_VOLUME_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_TICKER_24H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.validateNullDoubleAPI;

/**
 * Created by danielpadua on 16/03/2018.
 */

public class SouthxchangeJsonParser implements CrsJsonParser {
    @Override
    public List<TickerPrice> parseJson(JSONObject jsonObject) throws JSONException {
        List<TickerPrice> southxchangeTickerPrices = new ArrayList<>();

        // get 24h ticker
        Double bid = validateNullDoubleAPI(jsonObject.getString(JSON_SOUTHXCHANGE_BID_LABEL));
        Double ask = validateNullDoubleAPI(jsonObject.getString(JSON_SOUTHXCHANGE_ASK_LABEL));
        Double last = validateNullDoubleAPI(jsonObject.getString(JSON_SOUTHXCHANGE_LAST_LABEL));
        Double variation = validateNullDoubleAPI(jsonObject.getString(JSON_SOUTHXCHANGE_VARIATION_LABEL));
        Double volume = validateNullDoubleAPI(jsonObject.getString(JSON_SOUTHXCHANGE_VOLUME_LABEL));
        TickerPrice southxchangeTicker24h = new TickerPrice(JSON_TICKER_24H_LABEL, GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION, TickerType.TICKER24H, bid, ask, last, variation, volume);
        southxchangeTickerPrices.add(southxchangeTicker24h);

        return southxchangeTickerPrices;
    }
}
