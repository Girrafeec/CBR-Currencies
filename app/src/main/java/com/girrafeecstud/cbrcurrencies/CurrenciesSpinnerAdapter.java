package com.girrafeecstud.cbrcurrencies;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrenciesSpinnerAdapter extends ArrayAdapter<Currency> {


    private Context context;

    private ArrayList<Currency> currencies = new ArrayList<>();

    public CurrenciesSpinnerAdapter(Context context, int textViewResourceId, ArrayList<Currency> currencies) {
        super(context, textViewResourceId, currencies);
        this.context = context;
        this.currencies = currencies;
    }

    @Override
    public int getCount(){
        return currencies.size();
    }

    @Override
    public Currency getItem(int position){
        return currencies.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);

        label.setText(currencies.get(position).getCharCode());

        return label;
    }

    // And here is when the "chooser" is popped up
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(currencies.get(position).getCharCode());

        return label;
    }
}