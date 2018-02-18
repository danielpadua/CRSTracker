package br.com.danielpadua.crstracker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static br.com.danielpadua.crstracker.CRSUtil.supportedExchanges;

public class CRSTrackerWidgetProvider extends AppWidgetProvider implements OnTaskCompleted {

    private final Exchange e;
    private Context context;
    private int[] appWidgetIds;
    private RemoteViews remoteViews;
    private AppWidgetManager appWidgetManager;

    public CRSTrackerWidgetProvider() {
        this.e = supportedExchanges[0];
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        this.appWidgetIds = appWidgetIds;
        this.appWidgetManager = appWidgetManager;
        this.remoteViews = new RemoteViews(context.getPackageName(), R.layout.crs_widget_layout);
        new PriceUpdateTask(this.context, this).execute();
    }

    @Override
    public void onTaskCompleted() {
        Intent intent = new Intent(this.context, CRSTrackerWidgetProvider.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, this.appWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String price = e.getSupportedPairs().get(0).getPrice();
        String variation = e.getSupportedPairs().get(0).getVariation();
        String volume = e.getSupportedPairs().get(0).getVolume();

        this.remoteViews.setTextViewText(R.id.widgetExchangeName, e.getName().substring(0, 5) + "." + " CRS/BTC " + " " + new SimpleDateFormat("HH:mm", Locale.ROOT).format(new Date()));
        this.remoteViews.setTextViewText(R.id.widgetPrice, price);
        this.remoteViews.setTextViewText(R.id.widgetVariation, variation);
        this.remoteViews.setTextViewText(R.id.widgetVolume, volume);
        if (variation.contains("-")) {
            this.remoteViews.setTextColor(R.id.widgetVariation, Color.RED);
        } else {
            this.remoteViews.setTextColor(R.id.widgetVariation, Color.GREEN);
        }

        this.remoteViews.setOnClickPendingIntent(R.id.widgetAtualizar, pendingIntent);
        for (int widgetId : this.appWidgetIds) {
            appWidgetManager.updateAppWidget(widgetId, this.remoteViews);
        }
        Toast.makeText(this.context, "CRS Atualizado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent != null) {
            if (intent.getAction() != null) {
                if (intent.getAction().equalsIgnoreCase("actUpdateWidgets")) {
                    Intent i = new Intent(context, CRSTrackerWidgetProvider.class);
                    i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(
                            new ComponentName(context, CRSTrackerWidgetProvider.class));
                    i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    context.sendBroadcast(i);
                }
            }
        }
    }
}