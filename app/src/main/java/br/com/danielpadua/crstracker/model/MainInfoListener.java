package br.com.danielpadua.crstracker.model;

import java.util.List;

import br.com.danielpadua.crstracker.model.exchange.Exchange;
import br.com.danielpadua.crstracker.restClient.CrsRestException;

/**
 * Created by danielpadua on 15/02/2018.
 */

public interface MainInfoListener extends CrsInfoListener {

    void onTaskCompleted(Exchange exchange);

    void onTaskError(List<CrsRestException> errors, Exchange exchange);
}
