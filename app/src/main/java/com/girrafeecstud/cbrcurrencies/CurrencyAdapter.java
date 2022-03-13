package com.girrafeecstud.cbrcurrencies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    Context context;

    ArrayList<Currency> currencies = new ArrayList<>();

    public CurrencyAdapter(ArrayList<Currency> currencies, Context context) {
        this.currencies = currencies;
        this.context = context;
        notifyDataSetChanged();
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.ViewHolder holder, int position) {
        holder.currencyCharCode.setText(currencies.get(position).getCharCode());
        holder.currencyValue.setText(String.valueOf(currencies.get(position).getValue()));
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView currencyCharCode, currencyValue;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            currencyCharCode = itemView.findViewById(R.id.currencyCharCodeTxt);
            currencyValue = itemView.findViewById(R.id.currencyValueTxt);
        }
    }
}
