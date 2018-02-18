package br.com.danielpadua.crstracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import static br.com.danielpadua.crstracker.CRSUtil.supportedExchanges;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.lv = findViewById(R.id.lvCRSExchanges);

        new PriceUpdateTask(this, this).execute();
    }

    @Override
    public void onTaskCompleted() {
        this.lv.setAdapter(new CRSExchangeAdapter(getBaseContext(), new ArrayList<>(Arrays.asList(supportedExchanges))));
    }
}
