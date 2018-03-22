package br.com.danielpadua.crstracker.restClient.jsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_12H_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_1H_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BITVALOR_HIGH_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BITVALOR_LAST_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BITVALOR_LOW_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BITVALOR_TICKER12H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BITVALOR_TICKER1H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BITVALOR_TICKER24H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BITVALOR_TOTAL_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BITVALOR_TRADE_COUNT_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_BITVALOR_VOLUME_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_TICKER_12H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_TICKER_1H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_TICKER_24H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.validateNullDoubleAPI;

/**
 * Created by danielpadua on 16/03/2018.
 */

public class BitvalorJsonParser implements CrsJsonParser {

    @Override
    public List<TickerPrice> parseJson(JSONObject jsonObject) throws JSONException {
        List<TickerPrice> bitvalorTickerPrices = new ArrayList<>();

        // get 24h ticker
        TickerPrice bitvalorTickerPrice24h = getBitvalorTickerPrice(jsonObject, JSON_TICKER_24H_LABEL, GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION, JSON_BITVALOR_TICKER24H_LABEL, TickerType.TICKER24H);
        bitvalorTickerPrices.add(bitvalorTickerPrice24h);

        // get 12h ticker
        TickerPrice bitvalorTickerPrice12h = getBitvalorTickerPrice(jsonObject, JSON_TICKER_12H_LABEL, GLOBAL_DEFAULT_12H_TICKER_DESCRIPTION, JSON_BITVALOR_TICKER12H_LABEL, TickerType.TICKER12H);
        bitvalorTickerPrices.add(bitvalorTickerPrice12h);

        // get 1h ticker
        TickerPrice bitvalorTickerPrice1h = getBitvalorTickerPrice(jsonObject, JSON_TICKER_1H_LABEL, GLOBAL_DEFAULT_1H_TICKER_DESCRIPTION, JSON_BITVALOR_TICKER1H_LABEL, TickerType.TICKER1H);
        bitvalorTickerPrices.add(bitvalorTickerPrice1h);

        return bitvalorTickerPrices;
    }

    private TickerPrice getBitvalorTickerPrice(JSONObject jsonObject, String crsTickerLabel, String crsTickerDescription, String bitValorTickerLabel, TickerType tickerType) throws JSONException {
        JSONObject jTotal24 = jsonObject.getJSONObject(bitValorTickerLabel).getJSONObject(JSON_BITVALOR_TOTAL_LABEL);
        Double last = validateNullDoubleAPI(jTotal24.getString(JSON_BITVALOR_LAST_LABEL));
        Double high = validateNullDoubleAPI(jTotal24.getString(JSON_BITVALOR_HIGH_LABEL));
        Double low = validateNullDoubleAPI(jTotal24.getString(JSON_BITVALOR_LOW_LABEL));
        Double volume = validateNullDoubleAPI(jTotal24.getString(JSON_BITVALOR_VOLUME_LABEL));
        Long tradeCount = Long.parseLong(jTotal24.getString(JSON_BITVALOR_TRADE_COUNT_LABEL));
        return new TickerPrice(crsTickerLabel, crsTickerDescription, tickerType, last, high, low, volume, tradeCount);
    }
}
