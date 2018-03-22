package br.com.danielpadua.crstracker.model.widget;

import br.com.danielpadua.crstracker.model.CrsInfoListener;
import br.com.danielpadua.crstracker.model.exchange.Exchange;

/**
 * Created by danielpadua on 19/03/2018.
 */

public interface WidgetInfoListener extends CrsInfoListener {
    void onTaskCompleted(WidgetTracker widgetTracker, Exchange updatedExchange);

    void onTaskError(WidgetTracker widgetTracker, Exchange updatedExchange, String errorMessage);
}
