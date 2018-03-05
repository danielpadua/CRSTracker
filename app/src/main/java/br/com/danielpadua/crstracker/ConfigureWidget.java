package br.com.danielpadua.crstracker;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;
import static br.com.danielpadua.crstracker.CRSUtil.CRS_WIDGET_PREFERENCES;
import static br.com.danielpadua.crstracker.CRSUtil.supportedExchanges;

public class ConfigureWidget extends AppCompatActivity {

    ArrayAdapter<Exchange> exchangeAdapter;
    ArrayAdapter<Pair> pairAdapter;
    Spinner cfgExchange;
    Spinner cfgPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_widget);
        setResult(RESULT_CANCELED);

        initListViews();
    }

    private void initListViews() {

        this.cfgExchange = findViewById(R.id.configureExchange);

        this.exchangeAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                CRSUtil.supportedExchanges);

        this.cfgExchange.setAdapter(this.exchangeAdapter);

        cfgExchange.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadPairs(position);
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

    private void loadPairs(int selectedExchangeId) {
        this.cfgPair = findViewById(R.id.configurePair);

        this.pairAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                supportedExchanges[selectedExchangeId].getSupportedPairs());
        this.cfgPair.setAdapter(pairAdapter);
    }


    private void handleOkButton() {
        showAppWidget();
    }

    private void showAppWidget() {

        int appwidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            // Setting a widget tracker
            appwidgetId = extras.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
            Exchange e = (Exchange) this.cfgExchange.getSelectedItem();
            Pair p = (Pair) this.cfgPair.getSelectedItem();
            WidgetTracker wt = new WidgetTracker(appwidgetId, e.getId(), p.getId());

            // Saving widget tracker
            SharedPreferences crsPrefs = getSharedPreferences(CRS_WIDGET_PREFERENCES, 0);
            SharedPreferences.Editor edit = crsPrefs.edit();
            edit.putString(String.valueOf(appwidgetId), new Gson().toJson(wt));
            edit.apply();
            // Start update intent
//            Intent updateIntent = new Intent(this, CRSTrackerWidgetProvider.class);
//            updateIntent.putExtra(EXTRA_APPWIDGET_IDS, new int[] { appwidgetId });
//            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            sendBroadcast(intent);
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
            Log.i("WIDGET", "Invalid widgetId");
            finish();
        }
    }


}
