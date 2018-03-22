package br.com.danielpadua.crstracker.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import br.com.danielpadua.crstracker.ExchangeAPIMap;
import br.com.danielpadua.crstracker.R;
import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.TickerType;
import br.com.danielpadua.crstracker.model.exchange.Exchange;
import br.com.danielpadua.crstracker.model.pair.Pair;
import br.com.danielpadua.crstracker.model.pair.RealPair;
import br.com.danielpadua.crstracker.model.pair.RelativePair;
import br.com.danielpadua.crstracker.model.widget.WidgetInfoListener;
import br.com.danielpadua.crstracker.model.widget.WidgetInfoOption;
import br.com.danielpadua.crstracker.model.widget.WidgetTracker;
import br.com.danielpadua.crstracker.persistance.Repository;
import br.com.danielpadua.crstracker.restClient.RestClient;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_DELETED;
import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;
import static br.com.danielpadua.crstracker.util.CRSUtil.formatCryptoPrice;
import static br.com.danielpadua.crstracker.util.CRSUtil.formatFiatPrice;
import static br.com.danielpadua.crstracker.util.CRSUtil.formatVariation;
import static br.com.danielpadua.crstracker.util.CRSUtil.formatVolume;


public class CRSTrackerWidgetProvider extends AppWidgetProvider implements WidgetInfoListener {

    private AppWidgetManager appWidgetManager;
    private RemoteViews remoteViews;
    private Intent intent;
    private Context context;
    //private int[] allWidgetIds;
    private List<Exchange> exchanges;
    private Repository<WidgetTracker> repository;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //ComponentName thisWidget = new ComponentName(context, CRSTrackerWidgetProvider.class);
        //this.allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        this.remoteViews = new RemoteViews(context.getPackageName(), R.layout.crs_widget_layout);
        this.appWidgetManager = appWidgetManager;
        this.context = context;
        this.intent = new Intent(context, this.getClass());
        this.exchanges = getExchanges();
        this.repository = new Repository<>(context, WidgetTracker.class);

        List<WidgetTracker> allWidgetsTrackers = repository.getAll();

        for (WidgetTracker widgetTracker : allWidgetsTrackers) {
            Exchange chosenExchange = getExchangeById(widgetTracker.getChosenExchangeId());
            RestClient restClient = ExchangeAPIMap.getRestClientByExchange(this.context, this, chosenExchange);
            restClient.execute(new WidgetTracker[]{widgetTracker});
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
                        repository = new Repository<>(context, WidgetTracker.class);
                        WidgetTracker searchedWidgetTracker = repository.get(String.valueOf(id));
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
                            repository = new Repository<>(context, WidgetTracker.class);
                            repository.remove(String.valueOf(appWidgetId));
                        }
                    }
                } else if (intent.getAction().contains("URL_SOLICITADA")) {
                    String action = intent.getAction();
                    int initial = action.lastIndexOf("_") + 1;
                    String widgetId = intent.getAction().substring(initial, action.length());
                    repository = new Repository<>(context, WidgetTracker.class);
                    WidgetTracker widgetTracker = repository.get(String.valueOf(widgetId));
                    if (widgetTracker != null) {
                        startBrowsing(context, AppWidgetManager.getInstance(context), widgetTracker);
                    }
                }
            }
        }
    }

    private void startBrowsing(Context ctx, AppWidgetManager appWidgetManager, final WidgetTracker widgetTracker) {
        RemoteViews widgetView = new RemoteViews(ctx.getPackageName(), R.layout.crs_widget_layout);

        Exchange e = getExchangeById(widgetTracker.getChosenExchangeId());

        Pair p = Iterables.find(e.getSupportedPairs(), new Predicate<Pair>() {
            @Override
            public boolean apply(@Nullable Pair input) {
                return input != null && input.getId() == widgetTracker.getChosenPairId();
            }
        });

        if (p instanceof RealPair) {
            Uri uri = Uri.parse(p.getTradeURL());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            PendingIntent pIntent = PendingIntent.getActivity(ctx, widgetTracker.getWidgetId(), intent, 0);
            widgetView.setOnClickPendingIntent(R.id.widgetPairURL, pIntent);

            appWidgetManager.updateAppWidget(widgetTracker.getWidgetId(), widgetView);
        }
    }

    private List<Exchange> getExchanges() {
        List<Exchange> exchanges = new ArrayList<>();
        Iterator it = ExchangeAPIMap.getMap(this.context, this).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            exchanges.add((Exchange) pair.getKey());
            it.remove();
        }
        return exchanges;
    }

    @Override
    public void onTaskCompleted(WidgetTracker widgetTracker, Exchange updatedExchange) {
        int widgetId = widgetTracker.getWidgetId();

        this.intent.setAction(ACTION_APPWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 1, this.intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Pair p = updatedExchange.getPairById(widgetTracker.getChosenPairId());

        this.intent.setAction("URL_SOLICITADA_" + widgetId);

        PendingIntent pendingIntentPairURL = PendingIntent.getBroadcast(this.context, 1, this.intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String pairName = " CRS/" + p.getCoin() + " ";
        String widgetTitle = updatedExchange.getName().substring(0, 5)
                + "." + pairName + " " + new SimpleDateFormat("HH:mm", Locale.ROOT).format(new Date());

        for (WidgetInfoOption widgetInfoOption : widgetTracker.getInfoOptions()) {
            try {
                setWidgetLine(p, widgetInfoOption, widgetTracker.getChosenTickerType());
            } catch (CrsWidgetException e) {
                Log.e(this.getClass().getName(), e.getMessage());
                Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        this.remoteViews.setTextViewText(R.id.widgetExchangeName, widgetTitle);

        this.remoteViews.setOnClickPendingIntent(R.id.widgetAtualizar, pendingIntent);
        this.remoteViews.setOnClickPendingIntent(R.id.widgetPairURL, pendingIntentPairURL);

        this.remoteViews.setViewVisibility(R.id.txvAtualizando, View.GONE);
        this.remoteViews.setViewVisibility(R.id.linWidgetDados, View.VISIBLE);
        if (p instanceof RelativePair) {
            this.remoteViews.setViewVisibility(R.id.widgetPairURL, View.GONE);
        } else {
            this.remoteViews.setViewVisibility(R.id.widgetPairURL, View.VISIBLE);
        }

        appWidgetManager.updateAppWidget(widgetTracker.getWidgetId(), this.remoteViews);
        startBrowsing(this.context, this.appWidgetManager, widgetTracker);


        Toast.makeText(this.context, "CRS Atualizado", Toast.LENGTH_SHORT).show();
    }

    private void setWidgetLine(Pair pair, WidgetInfoOption widgetInfoOption, TickerType chosenTickerType) throws CrsWidgetException {
        String line1Label;
        String line1Value;
        String line2Label;
        String line2Value;
        String line3Label;
        String line3Value;

        TickerPrice chosenTickerPrice = pair.getTickerByType(chosenTickerType);
        int order = widgetInfoOption.getOrder();
        boolean isFiat = pair.isFiat();

        switch (widgetInfoOption.getInfoOption()) {
            case Price:
                if (order == 0) {
                    line1Label = "Val: ";
                    if (pair instanceof RelativePair) {
                        RelativePair relativePair = (RelativePair) pair;
                        line1Value = !isFiat ? formatCryptoPrice(relativePair.getCrsPrice()) : formatFiatPrice(relativePair.getCrsPrice());
                    } else {
                        line1Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getLast()) : formatFiatPrice(chosenTickerPrice.getLast());
                    }
                    this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);
                } else if (order == 1) {
                    line2Label = "Val: ";
                    line2Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getLast()) : formatFiatPrice(chosenTickerPrice.getLast());
                    this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);
                } else if (order == 2) {
                    line3Label = "Val: ";
                    line3Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getLast()) : formatFiatPrice(chosenTickerPrice.getLast());
                    this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);
                }
                break;
            case Volume:
                if (order == 0) {
                    line1Label = "Vol: ";
                    line1Value = formatVolume(chosenTickerPrice.getVolume());
                    this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);
                } else if (order == 1) {
                    line2Label = "Vol: ";
                    line2Value = formatVolume(chosenTickerPrice.getVolume());
                    this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);
                } else if (order == 2) {
                    line3Label = "Vol: ";
                    line3Value = formatVolume(chosenTickerPrice.getVolume());
                    this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);
                }
                break;
            case Variation:
                int color = String.valueOf(chosenTickerPrice.getVariation()).contains("-") ? Color.RED : Color.rgb(50, 205, 50);
                if (order == 0) {
                    line1Label = "Var: ";
                    line1Value = formatVariation(chosenTickerPrice.getVariation());
                    this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);

                    if (color != Color.RED)
                        this.remoteViews.setTextViewText(R.id.widgetLine1Value, "+" + line1Value);
                    else
                        this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);

                    this.remoteViews.setTextColor(R.id.widgetLine1Value, color);
                } else if (order == 1) {
                    line2Label = "Var: ";
                    line2Value = formatVariation(chosenTickerPrice.getVariation());
                    this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);

                    if (color != Color.RED)
                        this.remoteViews.setTextViewText(R.id.widgetLine2Value, "+" + line2Value);
                    else
                        this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);

                    this.remoteViews.setTextColor(R.id.widgetLine2Value, color);
                } else if (order == 2) {
                    line3Label = "Var: ";
                    line3Value = formatVariation(chosenTickerPrice.getVariation());
                    this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);

                    if (color != Color.RED)
                        this.remoteViews.setTextViewText(R.id.widgetLine3Value, "+" + line3Value);
                    else
                        this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);


                    this.remoteViews.setTextColor(R.id.widgetLine3Value, color);
                }
                break;
            case Ask:
                if (order == 0) {
                    line1Label = "Ask: ";
                    line1Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getAsk()) : formatFiatPrice(chosenTickerPrice.getAsk());
                    this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);
                } else if (order == 1) {
                    line2Label = "Ask: ";
                    line2Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getAsk()) : formatFiatPrice(chosenTickerPrice.getAsk());
                    this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);
                } else if (order == 2) {
                    line3Label = "Ask: ";
                    line3Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getAsk()) : formatFiatPrice(chosenTickerPrice.getAsk());
                    this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);
                }
                break;
            case Bid:
                if (order == 0) {
                    line1Label = "Bid: ";
                    line1Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getBid()) : formatFiatPrice(chosenTickerPrice.getBid());
                    this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);
                } else if (order == 1) {
                    line2Label = "Bid: ";
                    line2Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getBid()) : formatFiatPrice(chosenTickerPrice.getBid());
                    this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);
                } else if (order == 2) {
                    line3Label = "Bid: ";
                    line3Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getBid()) : formatFiatPrice(chosenTickerPrice.getBid());
                    this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);
                }
                break;
            case High:
                if (order == 0) {
                    line1Label = "High: ";
                    line1Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getHigh()) : formatFiatPrice(chosenTickerPrice.getHigh());
                    this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);
                } else if (order == 1) {
                    line2Label = "High: ";
                    line2Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getHigh()) : formatFiatPrice(chosenTickerPrice.getHigh());
                    this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);
                } else if (order == 2) {
                    line3Label = "High: ";
                    line3Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getHigh()) : formatFiatPrice(chosenTickerPrice.getHigh());
                    this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);
                }
                break;
            case Low:
                if (order == 0) {
                    line1Label = "High: ";
                    line1Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getLow()) : formatFiatPrice(chosenTickerPrice.getLow());
                    this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);
                } else if (order == 1) {
                    line2Label = "High: ";
                    line2Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getLow()) : formatFiatPrice(chosenTickerPrice.getLow());
                    this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);
                } else if (order == 2) {
                    line3Label = "High: ";
                    line3Value = !isFiat ? formatCryptoPrice(chosenTickerPrice.getLow()) : formatFiatPrice(chosenTickerPrice.getLow());
                    this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);
                }
                break;
            case BtcPrice:
                if (pair instanceof RelativePair) {
                    RelativePair relativePair = (RelativePair) pair;
                    if (order == 0) {
                        line1Label = "Btc: ";
                        line1Value = formatCryptoPrice(relativePair.getBtcConversionPrice());
                        this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);
                        this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);
                    } else if (order == 1) {
                        line2Label = "Btc: ";
                        line2Value = formatCryptoPrice(relativePair.getBtcConversionPrice());
                        this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);
                        this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);
                    } else if (order == 2) {
                        line3Label = "Btc: ";
                        line3Value = formatCryptoPrice(relativePair.getBtcConversionPrice());
                        this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);
                        this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);
                    }
                } else {
                    throw new CrsWidgetException("Não é possível atribuir o valor de conversão Bitcoin se o par não for Relativo");
                }
                break;
            case TradeCount:
                if (order == 0) {
                    line1Label = "Trad: ";
                    line1Value = String.valueOf(chosenTickerPrice.getTradeCount());
                    this.remoteViews.setTextViewText(R.id.widgetLine1Label, line1Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine1Value, line1Value);
                } else if (order == 1) {
                    line2Label = "Trad: ";
                    line2Value = String.valueOf(chosenTickerPrice.getTradeCount());
                    this.remoteViews.setTextViewText(R.id.widgetLine2Label, line2Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine2Value, line2Value);
                } else if (order == 2) {
                    line3Label = "Trad: ";
                    line3Value = String.valueOf(chosenTickerPrice.getTradeCount());
                    this.remoteViews.setTextViewText(R.id.widgetLine3Label, line3Label);
                    this.remoteViews.setTextViewText(R.id.widgetLine3Value, line3Value);
                }
                break;
            default:
                throw new CrsWidgetException("Não foi possível identificar o tipo de informação selecionada.");
        }
    }

    private Exchange getExchangeById(final int exchangeId) {
        return Iterables.find(exchanges, new Predicate<Exchange>() {
            @Override
            public boolean apply(@Nullable Exchange input) {
                return input != null && input.getId() == exchangeId;
            }
        });
    }

    @Override
    public void onTaskError(WidgetTracker widgetTracker, Exchange updatedExchange, String errorMessage) {
        this.remoteViews.setTextViewText(R.id.txvAtualizando, errorMessage);
        this.intent.setAction(ACTION_APPWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 1, this.intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        this.remoteViews.setOnClickPendingIntent(R.id.widgetAtualizar, pendingIntent);

        this.appWidgetManager.updateAppWidget(widgetTracker.getWidgetId(), this.remoteViews);
    }
}