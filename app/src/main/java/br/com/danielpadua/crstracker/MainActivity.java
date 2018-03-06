package br.com.danielpadua.crstracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static br.com.danielpadua.crstracker.CRSUtil.supportedExchanges;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMainScreen();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadMainScreen();
    }

    private void loadMainScreen() {
        setContentView(R.layout.activity_main);
        this.lv = findViewById(R.id.lvCRSExchanges);
        new PriceUpdateTask(this, this).execute();
    }

    @Override
    public void onTaskCompleted() {
        onTaskCompleted(null);
    }

    @Override
    public void onTaskCompleted(int[] widgetIds) {
        this.lv.setAdapter(new CRSExchangeAdapter(getBaseContext(), new ArrayList<>(Arrays.asList(supportedExchanges))));
    }

    @Override
    public void onTaskTimeout(int[] widgetIds) {
        Toast.makeText(this, "Problemas com a conexão na exchange. Não atualizado.", Toast.LENGTH_LONG).show();
    }
}
