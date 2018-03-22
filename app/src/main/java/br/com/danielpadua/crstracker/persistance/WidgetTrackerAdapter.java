package br.com.danielpadua.crstracker.persistance;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.danielpadua.crstracker.model.TickerType;
import br.com.danielpadua.crstracker.model.widget.InfoOption;
import br.com.danielpadua.crstracker.model.widget.WidgetInfoOption;
import br.com.danielpadua.crstracker.model.widget.WidgetTracker;

/**
 * Created by danielpadua on 21/03/2018.
 */

public class WidgetTrackerAdapter implements JsonDeserializer<WidgetTracker> {

    @Override
    public WidgetTracker deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (json != null) {
            JsonObject jsonObject = json.getAsJsonObject();
            List<WidgetInfoOption> widgetInfoOptions = new ArrayList<>();

            if (jsonObject.get("widgetId") != null) {
                int widgetId = jsonObject.get("widgetId").getAsInt();
                int chosenExchangeId = jsonObject.get("chosenExchangeId").getAsInt();
                int chosenPairId = jsonObject.get("chosenPairId").getAsInt();
                for (JsonElement element : jsonObject.getAsJsonArray("infoOptions")) {
                    JsonObject jInfoObject = element.getAsJsonObject();
                    int order = jInfoObject.get("order").getAsInt();
                    InfoOption infoOption = InfoOption.valueOf(jInfoObject.get("infoOption").getAsString());
                    widgetInfoOptions.add(new WidgetInfoOption(order, infoOption));
                }
                TickerType tickerType = TickerType.valueOf(jsonObject.get("chosenTickerType").getAsString());
                return new WidgetTracker(widgetId, chosenExchangeId, chosenPairId, widgetInfoOptions, tickerType);
            }
        }

        return null;
    }
}
