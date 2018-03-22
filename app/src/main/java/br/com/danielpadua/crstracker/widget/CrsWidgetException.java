package br.com.danielpadua.crstracker.widget;

/**
 * Created by danielpadua on 19/03/2018.
 */

class CrsWidgetException extends Exception {
    public CrsWidgetException() {
    }

    public CrsWidgetException(String message) {
        super(message);
    }

    public CrsWidgetException(String message, Exception cause) {
        super(message, cause);
    }
}
