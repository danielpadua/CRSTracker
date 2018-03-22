package br.com.danielpadua.crstracker.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.danielpadua.crstracker.ExchangeAPIMap;
import br.com.danielpadua.crstracker.R;
import br.com.danielpadua.crstracker.model.TickerType;
import br.com.danielpadua.crstracker.model.exchange.Exchange;
import br.com.danielpadua.crstracker.model.exchange.Southxchange;
import br.com.danielpadua.crstracker.model.pair.Pair;
import br.com.danielpadua.crstracker.model.pair.RealPair;
import br.com.danielpadua.crstracker.model.pair.RelativePair;
import br.com.danielpadua.crstracker.model.widget.InfoOption;
import br.com.danielpadua.crstracker.model.widget.WidgetInfoListener;
import br.com.danielpadua.crstracker.model.widget.WidgetInfoOption;
import br.com.danielpadua.crstracker.model.widget.WidgetTracker;
import br.com.danielpadua.crstracker.persistance.Repository;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class ConfigureWidget extends AppCompatActivity implements WidgetInfoListener {

    private List<Exchange> exchanges;
    private ArrayAdapter<Exchange> exchangeAdapter;
    private ArrayAdapter<TickerType> tickerTypeAdapter;
    private ArrayAdapter<Pair> pairAdapter;
    private ArrayAdapter<InfoOption> infoOptionsAdapter;
    private Spinner cfgExchange;
    private Spinner cfgPair;
    private Spinner cfgTickerTypes;
    private Spinner cfgOption1;
    private Spinner cfgOption2;
    private Spinner cfgOption3;
    private Repository<WidgetTracker> repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_widget);
        setResult(RESULT_CANCELED);
        this.exchanges = getExchanges();
        initListViews();
    }

    private void initListViews() {
        this.cfgExchange = findViewById(R.id.configureExchange);
        this.cfgPair = findViewById(R.id.configurePair);
        this.exchangeAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, this.exchanges);
        this.cfgExchange.setAdapter(this.exchangeAdapter);

        this.cfgExchange.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadPairs(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        this.cfgPair.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadOptionsRules();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button configureButton = findViewById(R.id.btnConfigure);
        configureButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                handleOkButton();
            }
        });
    }

    private List<Exchange> getExchanges() {
        List<Exchange> exchanges = new ArrayList<>();
        Iterator it = ExchangeAPIMap.getMap(this, this).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            exchanges.add((Exchange) pair.getKey());
            it.remove();
        }
        return exchanges;
    }

    private void loadPairs(int selectedExchangeId) {
        this.cfgTickerTypes = findViewById(R.id.availableTickerValues);
        this.pairAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                this.exchanges.get(selectedExchangeId).getSupportedPairs());
        this.tickerTypeAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                this.exchanges.get(selectedExchangeId).getSupportedTickerTypes());
        this.cfgPair.setAdapter(this.pairAdapter);
        this.cfgTickerTypes.setAdapter(this.tickerTypeAdapter);
    }

    private void loadOptionsRules() {
        this.cfgOption1 = findViewById(R.id.line1ValueOption);
        this.cfgOption2 = findViewById(R.id.line2ValueOption);
        this.cfgOption3 = findViewById(R.id.line3ValueOption);

        Exchange selectedExchange = (Exchange) this.cfgExchange.getSelectedItem();
        Pair selectedPair = (Pair) this.cfgPair.getSelectedItem();
        List<InfoOption> availableOptions = new ArrayList<>();

        // View Rules

        // Southxchange Rules
        if (selectedExchange instanceof Southxchange &&
                (selectedPair instanceof RealPair && (!selectedPair.getCoin().equals("BRL"))) ||
                (selectedPair instanceof RelativePair && (!selectedPair.getCoin().equals("BRL")))) {
            availableOptions.add(InfoOption.Price);
            availableOptions.add(InfoOption.Bid);
            availableOptions.add(InfoOption.Ask);
            availableOptions.add(InfoOption.Variation);
            availableOptions.add(InfoOption.Volume);
        } else if (selectedExchange instanceof Southxchange &&
                (selectedPair instanceof RelativePair && selectedPair.getCoin().equals("BRL"))) {
            availableOptions.add(InfoOption.Price);
            availableOptions.add(InfoOption.BtcPrice);
            availableOptions.add(InfoOption.Volume);
        }

        this.infoOptionsAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                availableOptions);

        this.cfgOption1.setAdapter(infoOptionsAdapter);
        this.cfgOption2.setAdapter(infoOptionsAdapter);
        this.cfgOption3.setAdapter(infoOptionsAdapter);
    }

    private void handleOkButton() {
        int appwidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            // Setting a widget tracker
            appwidgetId = extras.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
            Exchange e = (Exchange) this.cfgExchange.getSelectedItem();
            TickerType chosenTickerType = (TickerType) this.cfgTickerTypes.getSelectedItem();
            Pair p = (Pair) this.cfgPair.getSelectedItem();
            List<WidgetInfoOption> chosenInfoOptions = new ArrayList<>();
            chosenInfoOptions.add(new WidgetInfoOption(0, (InfoOption) this.cfgOption1.getSelectedItem()));
            chosenInfoOptions.add(new WidgetInfoOption(1, (InfoOption) this.cfgOption2.getSelectedItem()));
            chosenInfoOptions.add(new WidgetInfoOption(2, (InfoOption) this.cfgOption3.getSelectedItem()));

            WidgetTracker wt = new WidgetTracker(appwidgetId, e.getId(), p.getId(), chosenInfoOptions, chosenTickerType);
            repository = new Repository<>(this, WidgetTracker.class);
            repository.persist(String.valueOf(appwidgetId), wt);

            new CRSTrackerWidgetProvider()
                    .onUpdate(this,
                            AppWidgetManager.getInstance(this),
                            new int[]{appwidgetId}
                    );

            Intent result = new Intent();
            result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetId);

            setResult(RESULT_OK, result);
            finish();
        }
        if (appwidgetId == INVALID_APPWIDGET_ID) {
            Log.i(this.getClass().getName(), "Invalid widgetId");
            finish();
        }
    }

    @Override
    public void onTaskCompleted(WidgetTracker widgetTracker, Exchange updatedExchange) {

    }

    @Override
    public void onTaskError(WidgetTracker widgetTracker, Exchange updatedExchange, String errorMessage) {

    }
}
