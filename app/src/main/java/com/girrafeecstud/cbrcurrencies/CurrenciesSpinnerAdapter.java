package com.girrafeecstud.cbrcurrencies;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    // Spinner choosed element
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_currencies_spinner_item, null, true);

        TextView spinnerCharCode = convertView.findViewById(R.id.currencySpinnerCharCodeTxt);
        ImageView spinnerImageIcon = convertView.findViewById(R.id.spinnerCountryIconImg);

        spinnerImageIcon.setImageResource(getCountryImage(currencies.get(position).getNumCode()));
        spinnerCharCode.setText(currencies.get(position).getCharCode());
        spinnerCharCode.setTextColor(context.getResources().getColor(R.color.white));

        return convertView;
    }

    // Spinner list view
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_currencies_spinner_item, null, true);

            TextView spinnerCharCode = convertView.findViewById(R.id.currencySpinnerCharCodeTxt);
            ImageView spinnerImageIcon = convertView.findViewById(R.id.spinnerCountryIconImg);

            spinnerImageIcon.setImageResource(getCountryImage(currencies.get(position).getNumCode()));
            spinnerCharCode.setText(currencies.get(position).getCharCode());
            spinnerCharCode.setTextColor(context.getResources().getColor(R.color.dark_gray));

            return convertView;
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
            case "643":
                return R.drawable.ic_ru;
            default:
                return R.drawable.ic_default_country;
        }
    }
}