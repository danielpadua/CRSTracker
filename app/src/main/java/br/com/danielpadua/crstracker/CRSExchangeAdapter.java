package br.com.danielpadua.crstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.collect.Iterables;

import java.util.ArrayList;

/**
 * Created by danielpadua on 13/02/2018.
 */

class CRSExchangeAdapter extends BaseAdapter {
    private static ArrayList<Exchange> lstExchanges;

    private final LayoutInflater mInflater;

    public CRSExchangeAdapter(Context context, ArrayList<Exchange> results) {
        lstExchanges = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return lstExchanges.size();
    }

    public Object getItem(int position) {
        return lstExchanges.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

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
            sb.append("CRS/").append(p.getCoin()).append(": ").append(p.getPrice()).append(p != Iterables.getLast(lstExchanges.get(position).getSupportedPairs()) ? "\n" : "");
        }
        holder.txtPairPrice.setText(sb.toString());

        return convertView;
    }

    static class ViewHolder {
        TextView txtExchangeName;
        TextView txtPairPrice;
    }
}
