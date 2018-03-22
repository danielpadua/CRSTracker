package br.com.danielpadua.crstracker.model.widget;

import java.util.List;

import br.com.danielpadua.crstracker.model.TickerType;

/**
 * Created by danielpadua on 21/02/2018.
 */

public class WidgetTracker {
    private int widgetId;
    private int chosenExchangeId;
    private int chosenPairId;
    private List<WidgetInfoOption> infoOptions;
    private TickerType chosenTickerType;

    public WidgetTracker(int widgetId, int chosenExchangeId, int chosenPairId, List<WidgetInfoOption> infoOptions, TickerType chosenTickerType) {
        setWidgetId(widgetId);
        setChosenExchangeId(chosenExchangeId);
        setChosenPairId(chosenPairId);
        setInfoOptions(infoOptions);
        setChosenTickerType(chosenTickerType);
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public int getChosenExchangeId() {
        return chosenExchangeId;
    }

    public void setChosenExchangeId(int chosenExchangeId) {
        this.chosenExchangeId = chosenExchangeId;
    }

    public int getChosenPairId() {
        return chosenPairId;
    }

    public void setChosenPairId(int chosenPairId) {
        this.chosenPairId = chosenPairId;
    }

    public List<WidgetInfoOption> getInfoOptions() {
        return infoOptions;
    }

    public void setInfoOptions(List<WidgetInfoOption> infoOptions) {
        this.infoOptions = infoOptions;
    }

    public TickerType getChosenTickerType() {
        return chosenTickerType;
    }

    public void setChosenTickerType(TickerType chosenTickerType) {
        this.chosenTickerType = chosenTickerType;
    }
}
