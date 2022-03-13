package com.girrafeecstud.cbrcurrencies;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private RecyclerView currenciesRecView;

    private ProgressBar gettingCurrencyProgressBar;

    private BottomNavigationView bottomNavigationView;

    private CurrencyDataBase currencyDataBase;

    private CurrencyAdapter currencyAdapter;

    private ArrayList<Currency> currencyDataArrayList = new ArrayList<Currency>();

    private final static String CBR_CURRENCIES_JSON_URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDataBase();

        initUiValues();

        // When app starts check if we have data in database
        //TODO добавить проверку на наличие соединения в случае попытки получения json
        if (isDatabaseEmpty())
            getCurrenciesJson();
        else
            addCurrenciesToRecView();


        bottomNavigationView.setSelectedItemId(R.id.rateMenuItem);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rateMenuItem:
                        return true;
                    case R.id.converterMenuItem:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, ConverterActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    // Chek if app has network connection
    private boolean hasNetworkConnection(){
        ConnectivityManager connectivityManager = ((ConnectivityManager) getApplicationContext().getSystemService(MainActivity.this.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    // Chek if app has internet connection (we may have network connnection but no internet)
    private boolean hasInternetConnection(){
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Initialization of UI values
    private void initUiValues(){
        currenciesRecView = findViewById(R.id.currenciesRecView);
        gettingCurrencyProgressBar = findViewById(R.id.gettingCurrencyProgressBar);
        bottomNavigationView = findViewById(R.id.mainBottomNavigationView);
    }

    private void initDataBase(){
        currencyDataBase = CurrencyDataBase.getInstance(MainActivity.this);
    }

    // Check if database is empty
    private boolean isDatabaseEmpty(){

        if (currencyDataBase.currencyDao().getAll().isEmpty())
            return true;
        return false;

    }

    // Adding currencies to recycler view
    private void addCurrenciesToRecView(){

        // Remove progress bar when show recyvler view
        gettingCurrencyProgressBar.setVisibility(View.GONE);

        currencyAdapter = new CurrencyAdapter(new ArrayList<>(currencyDataBase.currencyDao().getAll()),
                MainActivity.this);

        currenciesRecView.setAdapter(currencyAdapter);
        currenciesRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    private void getCurrenciesJson(){
        new FetchCurrenciesJson().execute();
    }

    // Adding currencies to database
    private void addCurrenciesToDataBase(LocalDateTime currentDateTime, JSONObject currenciesJsonObject){

        Log.i("json", currenciesJsonObject.toString());

        for (int currenciesIterator = 0; currenciesIterator < currenciesJsonObject.names().length(); currenciesIterator++){
            try {
                currencyDataBase.currencyDao().insert(new CurrencyJsonParser().parseCurrencyJson(currentDateTime,
                        new JSONObject(String.valueOf(currenciesJsonObject.get(currenciesJsonObject.names().getString(currenciesIterator))))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // Class for background fetching data from Json url
    private class FetchCurrenciesJson extends AsyncTask{

        private LocalDateTime currentDateTime;

        private JSONObject currenciesJsonObject;

        private String currenciesJsonData;

        public FetchCurrenciesJson() {
            currenciesJsonData = "";
            currentDateTime = null;
            currenciesJsonObject = null;
        }

        @Override
        protected void onPreExecute() {
            gettingCurrencyProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object o) {
            addCurrenciesToDataBase(currentDateTime, currenciesJsonObject);
            //addCurrenciesToRecView();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            fetchCurrenciesJson();
            return null;
        }

        private void fetchCurrenciesJson(){

            try {
                // Start https connection
                URL cbrCurrenciesJsonUrl = new URL(CBR_CURRENCIES_JSON_URL);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) cbrCurrenciesJsonUrl.openConnection();

                InputStream inputStream = httpsURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";

                while((line = bufferedReader.readLine()) != null){
                    currenciesJsonData += line;
                }

                // Stop https connection
                httpsURLConnection.disconnect();

                if (!currenciesJsonData.isEmpty()){
                    JSONObject jsonObject = new JSONObject(currenciesJsonData);
                    // Get valute JSON object from whole JSON object
                    String valute = jsonObject.getString("Valute");
                    currenciesJsonObject = new JSONObject(valute);
                }

                // Get current date and time
                Date date = Calendar.getInstance().getTime();
                currentDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}