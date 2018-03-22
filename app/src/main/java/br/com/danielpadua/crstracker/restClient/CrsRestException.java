package br.com.danielpadua.crstracker.restClient;

/**
 * Created by danielpadua on 16/03/2018.
 */

public class CrsRestException extends Exception {
    public CrsRestException() {
    }

    public CrsRestException(String message) {
        super(message);
    }

    public CrsRestException(String message, Exception cause) {
        super(message, cause);
    }
}
