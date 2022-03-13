package com.girrafeecstud.cbrcurrencies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class CurrencyJsonParser {

    Currency currency;

    public Currency getCurrency() {
        return currency;
    }

    // Parsing currency value json for currency values
    public Currency parseCurrencyJson(LocalDateTime localDateTime, JSONObject jsonObject){

        String id = "";
        String numCode = "";
        String charCode = "";
        String nominal = "";
        String name = "";
        String value = "";
        String previousValue = "";

        try {
            id = jsonObject.getString("ID");
            numCode = jsonObject.getString("NumCode");
            charCode = jsonObject.getString("CharCode");
            nominal = jsonObject.getString("Nominal");
            name = jsonObject.getString("Name");
            value = jsonObject.getString("Value");
            previousValue = jsonObject.getString("Previous");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        Log.i("id",  id);
        Log.i("numcode", numCode);
        Log.i("charcode", charCode);
        Log.i("nominal", nominal);
        Log.i("name", name);
        Log.i("value", value);
        Log.i("previous", previousValue);
         */

        currency = new Currency(localDateTime, id, numCode, charCode, Integer.parseInt(nominal),
                name, Double.parseDouble(value), Double.parseDouble(previousValue));

        return getCurrency();
    }
}
