package br.com.danielpadua.crstracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.danielpadua.crstracker.model.MainInfoListener;
import br.com.danielpadua.crstracker.model.exchange.Exchange;
import br.com.danielpadua.crstracker.restClient.CrsRestException;
import br.com.danielpadua.crstracker.restClient.RestClient;

public class MainActivity extends AppCompatActivity implements MainInfoListener {

    private final List<Exchange> exchanges = new ArrayList<>();
    private ListView lv;
    private int exchangesRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMainScreen();
    }

    private void loadMainScreen() {
        setContentView(R.layout.activity_main);
        this.lv = findViewById(R.id.lvCRSExchanges);

        Iterator it = ExchangeAPIMap.getMap(this, this).entrySet().iterator();
        this.exchangesRemaining = ExchangeAPIMap.getMap(this, this).size();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            RestClient restClient = (RestClient) pair.getValue();
            restClient.execute((Object[]) null);
            it.remove();
        }
    }

    @Override
    public void onTaskCompleted(Exchange exchange) {
        exchanges.add(exchange);
        checkFillList();
    }

    @Override
    public void onTaskError(List<CrsRestException> errors, Exchange exchange) {
        exchanges.add(exchange);
        StringBuilder sb = new StringBuilder();
        for (Exception error : errors) {
            sb.append(error != Iterables.getLast(errors) ? error.getMessage() + System.getProperty("line.separator") : error.getMessage());
        }
        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        checkFillList();
    }

    private void checkFillList() {
        this.exchangesRemaining--;

        if (this.exchangesRemaining == 0) {
            fillList(this.exchanges);
        }
    }

    private void fillList(List<Exchange> exchanges) {
        this.lv.setAdapter(new CRSExchangeAdapter(this, exchanges));
    }
}
