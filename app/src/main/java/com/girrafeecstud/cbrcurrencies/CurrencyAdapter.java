package com.girrafeecstud.cbrcurrencies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public void onBindViewHolder(@NonNull CurrencyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.currencyCharCode.setText(currencies.get(position).getCharCode());
        holder.currencyValue.setText(String.valueOf(currencies.get(position).getValue()) + " ₽");
        holder.currencyCountryImg.setImageResource(getCountryImage(currencies.get(position).getNumCode()));

        holder.name.setText("Наименование: " + currencies.get(position).getName());
        holder.numCode.setText("Цифровой код: " + currencies.get(position).getNumCode());
        holder.nominal.setText("Единиц: " + String.valueOf(currencies.get(position).getNominal()));
        holder.smallCurrencyValue.setText("Курс: " + String.valueOf(currencies.get(position).getValue()) + " ₽");
        holder.previousCurrencyValue.setText("Предыдущий курс: " + String.valueOf(currencies.get(position).getPreviousValue()) + " ₽");

        boolean currencyExpanded = currencies.get(position).isExpandable();

        if (currencyExpanded) {
            holder.dropDown.setImageResource(R.drawable.ic_baseline_arrow_drop_up);
            holder.expandedCurrencyInfo.setVisibility(View.VISIBLE);
        }
        else {
            holder.dropDown.setImageResource(R.drawable.ic_baseline_arrow_drop_down);
            holder.expandedCurrencyInfo.setVisibility(View.GONE);
        }

        // Change expanded status
        holder.dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencies.get(position).setExpandable(!currencies.get(position).isExpandable());
                notifyDataSetChanged();
            }
        });

    }

    private int getCountryImage(String countryNumCode){
        switch (countryNumCode){
            case "036":
                return R.drawable.ic_au;
            case "944":
                return R.drawable.ic_az;
            case "826":
                return R.drawable.ic_gb;
            case "051":
                return R.drawable.ic_am;
            case "933":
                return R.drawable.ic_by;
            case "975":
                return R.drawable.ic_bg;
            case "986":
                return R.drawable.ic_br;
            case "348":
                return R.drawable.ic_hu;
            case "344":
                return R.drawable.ic_hk;
            case "208":
                return R.drawable.ic_dk;
            case "840":
                return R.drawable.ic_us;
            case "978":
                return R.drawable.ic_eu;
            case "356":
                return R.drawable.ic_in;
            case "398":
                return R.drawable.ic_kz;
            case "124":
                return R.drawable.ic_ca;
            case "417":
                return R.drawable.ic_kg;
            case "156":
                return R.drawable.ic_cn;
            case "498":
                return R.drawable.ic_md;
            case "578":
                return R.drawable.ic_no;
            case "985":
                return R.drawable.ic_pl;
            case "946":
                return R.drawable.ic_ro;
            case "960":
                return R.drawable.ic_xdr;
            case "702":
                return R.drawable.ic_sg;
            case "972":
                return R.drawable.ic_tj;
            case "949":
                return R.drawable.ic_tr;
            case "934":
                return R.drawable.ic_tm;
            case "860":
                return R.drawable.ic_uz;
            case "980":
                return R.drawable.ic_ua;
            case "203":
                return R.drawable.ic_cz;
            case "752":
                return R.drawable.ic_se;
            case "756":
                return R.drawable.ic_ch;
            case "710":
                return R.drawable.ic_za;
            case "410":
                return R.drawable.ic_kr;
            case "392":
                return R.drawable.ic_jp;
            default:
                return R.drawable.ic_default_country;
        }
    }

    // Clear rec view adapter
    public void clear() {
        int size = currencies.size();
        currencies.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout expandedCurrencyInfo;

        private TextView currencyCharCode, currencyValue, smallCurrencyValue, previousCurrencyValue, nominal, name, numCode;

        private ImageView currencyCountryImg;

        private ImageButton dropDown;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            currencyCharCode = itemView.findViewById(R.id.currencyCharCodeTxt);
            currencyValue = itemView.findViewById(R.id.currencyValueTxt);
            numCode = itemView.findViewById(R.id.currencyNumCodeTxt);
            smallCurrencyValue = itemView.findViewById(R.id.currencySmallValueTxt);
            previousCurrencyValue = itemView.findViewById(R.id.currencyPreviousValueTxt);
            nominal = itemView.findViewById(R.id.currencyNominalTxt);
            name = itemView.findViewById(R.id.currencyNameTxt);
            currencyCountryImg = itemView.findViewById(R.id.currencyCountryImg);
            dropDown = itemView.findViewById(R.id.dropDownImgButton);
            expandedCurrencyInfo = itemView.findViewById(R.id.curencyExtraDataLinLay);
        }
    }
}
