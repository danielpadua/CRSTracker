package br.com.danielpadua.crstracker;

/**
 * Created by danielpadua on 21/02/2018.
 */

class WidgetTracker {
    private int widgetId;
    private int chosenExchangeId;
    private int chosenPairId;


    WidgetTracker() {
    }

    WidgetTracker(int widgetId, int chosenExchangeId, int chosenPairId) {
        setWidgetId(widgetId);
        setChosenExchangeId(chosenExchangeId);
        setChosenPairId(chosenPairId);
    }

    int getWidgetId() {
        return widgetId;
    }

    void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    int getChosenExchangeId() {
        return chosenExchangeId;
    }

    void setChosenExchangeId(int chosenExchangeId) {
        this.chosenExchangeId = chosenExchangeId;
    }

    int getChosenPairId() {
        return chosenPairId;
    }

    void setChosenPairId(int chosenPairId) {
        this.chosenPairId = chosenPairId;
    }
}
