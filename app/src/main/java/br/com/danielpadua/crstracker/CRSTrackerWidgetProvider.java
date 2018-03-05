package br.com.danielpadua.crstracker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_DELETED;
import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;
import static br.com.danielpadua.crstracker.CRSUtil.CRS_WIDGET_PREFERENCES;
import static br.com.danielpadua.crstracker.CRSUtil.supportedExchanges;


public class CRSTrackerWidgetProvider extends AppWidgetProvider implements OnTaskCompleted {

    private AppWidgetManager appWidgetManager;
    private RemoteViews remoteViews;
    private Intent intent;
    private Context context;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, CRSTrackerWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        this.remoteViews = new RemoteViews(context.getPackageName(), R.layout.crs_widget_layout);
        this.appWidgetManager = appWidgetManager;
        this.context = context;
        this.intent = new Intent(context, CRSTrackerWidgetProvider.class);

        try {
            new PriceUpdateTask(context, this, allWidgetIds).execute();
        } catch (NullPointerException ex) {
            Log.e("ERROR", "NullpointerException updating widget");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,
                    CRSTrackerWidgetProvider.class));
            if (intent.getAction() != null) {
                if (intent.getAction().equals(ACTION_APPWIDGET_UPDATE)) {
                    for (int id : ids) {
                        SharedPreferences crsPrefs = context.getSharedPreferences(CRS_WIDGET_PREFERENCES, 0);
                        WidgetTracker searchedWidgetTracker = new Gson().fromJson(crsPrefs.getString(String.valueOf(id),
                                null), WidgetTracker.class);
                        if (searchedWidgetTracker == null) {
                            return;
                        }
                    }
                    for (int id : ids) {
                        this.remoteViews = new RemoteViews(context.getPackageName(), R.layout.crs_widget_layout);
                        this.remoteViews.setViewVisibility(R.id.txvAtualizando, View.VISIBLE);
                        this.remoteViews.setViewVisibility(R.id.linWidgetDados, View.GONE);
                        this.appWidgetManager = AppWidgetManager.getInstance(context);
                        this.appWidgetManager.updateAppWidget(id, this.remoteViews);
                    }
                    onUpdate(context, AppWidgetManager.getInstance(context), ids);
                } else if (intent.getAction().equals(ACTION_APPWIDGET_DELETED)) {
                    if (intent.getExtras() != null) {
                        final int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

                        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                            SharedPreferences crsPrefs = context.getSharedPreferences(CRS_WIDGET_PREFERENCES, 0);
                            crsPrefs.edit().remove(String.valueOf(appWidgetId)).apply();
                        }
                    }
                } else if (intent.getAction().contains("URL_SOLICITADA")) {
                    String action = intent.getAction();
                    int initial = action.lastIndexOf("_") + 1;
                    String widgetId = intent.getAction().substring(initial, action.length());
                    SharedPreferences crsPrefs = context.getSharedPreferences(CRS_WIDGET_PREFERENCES, 0);
                    WidgetTracker widgetTracker = new Gson().fromJson(crsPrefs.getString(widgetId,
                            null), WidgetTracker.class);
                    if (widgetTracker != null) {
                        startBrowsing(context, AppWidgetManager.getInstance(context), widgetTracker);
                    }
                }
            }
        }
    }

    private void startBrowsing(Context ctx, AppWidgetManager appWidgetManager, final WidgetTracker widgetTracker) {
        RemoteViews widgetView = new RemoteViews(ctx.getPackageName(), R.layout.crs_widget_layout);

        Exchange e = Iterables.find(Arrays.asList(supportedExchanges), new Predicate<Exchange>() {
            @Override
            public boolean apply(@Nullable Exchange input) {
                return input != null && input.getId() == widgetTracker.getChosenExchangeId();
            }
        });

        Pair p = Iterables.find(e.getSupportedPairs(), new Predicate<Pair>() {
            @Override
            public boolean apply(@Nullable Pair input) {
                return input != null && input.getId() == widgetTracker.getChosenPairId();
            }
        });

        if (p.getCoin() != SupportedCoins.BRL) {
            Uri uri = Uri.parse(p.getPairURL());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            PendingIntent pIntent = PendingIntent.getActivity(ctx, widgetTracker.getWidgetId(), intent, 0);
            widgetView.setOnClickPendingIntent(R.id.widgetPairURL, pIntent);

            appWidgetManager.updateAppWidget(widgetTracker.getWidgetId(), widgetView);
        }
    }

    @Override
    public void onTaskCompleted() {
        onTaskCompleted(null);
    }

    @Override
    public void onTaskCompleted(int[] widgetIds) {
        if (widgetIds != null) {
            for (int widgetId : widgetIds) {

                this.intent.setAction(ACTION_APPWIDGET_UPDATE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 1, this.intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                SharedPreferences crsPrefs = this.context.getSharedPreferences(CRS_WIDGET_PREFERENCES, 0);
                final WidgetTracker widgetTracker = new Gson().fromJson(crsPrefs.getString(String.valueOf(widgetId), ""),
                        WidgetTracker.class);

                Exchange e = Iterables.find(Arrays.asList(supportedExchanges), new Predicate<Exchange>() {
                    @Override
                    public boolean apply(@Nullable Exchange input) {
                        return input != null && input.getId() == widgetTracker.getChosenExchangeId();
                    }
                });

                Pair p = Iterables.find(e.getSupportedPairs(), new Predicate<Pair>() {
                    @Override
                    public boolean apply(@Nullable Pair input) {
                        return input != null && input.getId() == widgetTracker.getChosenPairId();
                    }
                });

                this.intent.setAction("URL_SOLICITADA_" + widgetId);

                PendingIntent pendingIntentPairURL = PendingIntent.getBroadcast(this.context, 1, this.intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                if (widgetTracker != null) {
                    String pairName = " CRS/" + p.getCoin().toString() + " ";
                    String widgetTitle = e.getName().substring(0, 5)
                            + "." + pairName + " " + new SimpleDateFormat("HH:mm", Locale.ROOT).format(new Date());
                    String line1Label;
                    String line1Value;
                    String line2Label;
                    String line2Value;
                    String line3Label;
                    String line3Value;
                    int color = p.getVariation().contains("-") ? Color.RED : Color.GREEN;

                    if (p.getCoin() == SupportedCoins.BRL) {
                        line1Label = "BRL: ";
                        line1Value = p.getPrice();
                        line2Label = "BTC: ";
                        line2Value = e.getSupportedPairs().get(0).getPrice();
                        line3Label = "Var.24h: ";
                        line3Value = e.getSupportedPairs().get(0).getVariation();
                        this.remoteViews.setTextColor(R.id.widgetLine3Value, color);
                        this.remoteViews.setTextColor(R.id.widgetLine2Value, context.getResources().getColor(android.R.color.holo_blue_dark));

                    } else {
                        line1Label = "Preço: ";
                        line1Value = p.getPrice();
                        line2Label = "Var.24h: ";
                        line2Value = p.getVariation();
                        this.remoteViews.setTextColor(R.id.widgetLine2Value, color);
                        this.remoteViews.setTextColor(R.id.widgetLine3Value, context.getResources().getColor(android.R.color.holo_blue_dark));
                        line3Label = "Vol.24h: ";
                        line3Value = p.getVolume();
                    }

                    this.remoteViews.setTextViewText(R.id.widgetExchangeName, widgetTitle);
                    this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);
                    this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);
                    this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);

                    this.remoteViews.setOnClickPendingIntent(R.id.widgetAtualizar, pendingIntent);
                    this.remoteViews.setOnClickPendingIntent(R.id.widgetPairURL, pendingIntentPairURL);

                    this.remoteViews.setViewVisibility(R.id.txvAtualizando, View.GONE);
                    this.remoteViews.setViewVisibility(R.id.linWidgetDados, View.VISIBLE);
                    if (p.getCoin() == SupportedCoins.BRL)
                        this.remoteViews.setViewVisibility(R.id.widgetPairURL, View.GONE);
                    else
                        this.remoteViews.setViewVisibility(R.id.widgetPairURL, View.VISIBLE);


                    appWidgetManager.updateAppWidget(widgetTracker.getWidgetId(), this.remoteViews);
                    startBrowsing(this.context, this.appWidgetManager, widgetTracker);
                }
            }

            Toast.makeText(this.context, "CRS Atualizado", Toast.LENGTH_SHORT).show();
        }
    }
}