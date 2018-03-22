package br.com.danielpadua.crstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.collect.Iterables;

import java.util.List;

import br.com.danielpadua.crstracker.model.TickerPrice;
import br.com.danielpadua.crstracker.model.exchange.Exchange;
import br.com.danielpadua.crstracker.model.pair.Pair;
import br.com.danielpadua.crstracker.model.pair.RealPair;
import br.com.danielpadua.crstracker.model.pair.RelativePair;

import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_PRICE_ERROR;
import static br.com.danielpadua.crstracker.util.CRSUtil.EXCEPTION_TICKER_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.JSON_TICKER_24H_LABEL;
import static br.com.danielpadua.crstracker.util.CRSUtil.formatCryptoPrice;
import static br.com.danielpadua.crstracker.util.CRSUtil.formatFiatPrice;

/**
 * Created by danielpadua on 13/02/2018.
 */

class CRSExchangeAdapter extends BaseAdapter {
    private static List<Exchange> lstExchanges;

    private final LayoutInflater mInflater;

    public CRSExchangeAdapter(Context context, List<Exchange> results) {
        lstExchanges = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lstExchanges.size();
    }

    @Override
    public Object getItem(int position) {
        return lstExchanges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtExchangeName = convertView.findViewById(R.id.exchangeName);
            holder.txtPairPrice = convertView.findViewById(R.id.pairPrice);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtExchangeName.setText("Exchange: " + lstExchanges.get(position).getName());
        StringBuilder sb = new StringBuilder();
        for (Pair p : lstExchanges.get(position).getSupportedPairs()) {
            for (TickerPrice tickerPrice : p.getTickerPrices()) {
                if (tickerPrice.getTickerLabel().equals(JSON_TICKER_24H_LABEL)) {
                    String price;
                    String relativePairMessage = "";
                    if (p instanceof RealPair) {
                        if (!p.isFiat())
                            price = formatCryptoPrice(tickerPrice.getLast());
                        else
                            price = formatFiatPrice(tickerPrice.getLast());
                    } else {
                        RelativePair relativePair = (RelativePair) p;
                        price = formatFiatPrice(relativePair.getCrsPrice());
                        relativePairMessage = " - Par Relativo";
                    }
                    sb.append("CRS/").append(p.getCoin()).append(": ").append(price).append(relativePairMessage).append(p != Iterables.getLast(lstExchanges.get(position).getSupportedPairs()) ? System.getProperty("line.separator") : "");
                } else if (tickerPrice.getTickerLabel().equals(EXCEPTION_TICKER_LABEL)) {
                    sb.append("CRS/").append(p.getCoin()).append(": ").append(EXCEPTION_PRICE_ERROR).append(p != Iterables.getLast(lstExchanges.get(position).getSupportedPairs()) ? System.getProperty("line.separator") : "");
                }
            }
        }
        holder.txtPairPrice.setText(sb.toString());

        return convertView;
    }

    static class ViewHolder {
        TextView txtExchangeName;
        TextView txtPairPrice;
    }
}
