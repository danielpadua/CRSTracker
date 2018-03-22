package br.com.danielpadua.crstracker.restClient.jsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;

/**
 * Created by danielpadua on 16/03/2018.
 */

public interface CrsJsonParser {
    List<TickerPrice> parseJson(JSONObject jsonObject) throws JSONException;
}
