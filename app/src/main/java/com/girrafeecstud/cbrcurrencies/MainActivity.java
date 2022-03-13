package com.girrafeecstud.cbrcurrencies;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView fetchingDateTimeInfo;

    private RecyclerView currenciesRecView;

    private ProgressBar gettingCurrencyProgressBar;

    private SwipeRefreshLayout swipeRefreshLayout;

    private BottomNavigationView bottomNavigationView;

    private Toast backToast;

    private CurrencyDataBase currencyDataBase;

    private CurrencyAdapter currencyAdapter;

    private ArrayList<Currency> currencyDataArrayList = new ArrayList<Currency>();

    private long backPressedTime;

    private final static String CBR_CURRENCIES_JSON_URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String LAST_FETCHING_DATE_TIME = "LAST_FETCHING_DATE_TIME";
    public static final String FIRST_LAUNCH = "FIRST_LAUNCH";

    public static final String EXTRA_MAIN_BACKGROUND_METHOD = "EXTRA_MAIN_BACKGROUND_METHOD";

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            backToast.cancel();
            finishAffinity();
            return;
        }

        backToast = Toast.makeText(this, "Для выхода нажмите назад ещё раз", Toast.LENGTH_SHORT);
        backToast.show();

        backPressedTime = System.currentTimeMillis();
    }

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

        setFetchingInfo();

        // Add background alarm task after first launch
        if (isFirstLaunch()){
            setBackgroundAlarmManager();
            saveFirstLaunchInfo();
        }

        // Actions for bottom navigation view
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

        // Actions for swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (currencyAdapter.getItemCount() != 0){
                    currencyAdapter.clear();
                    currencyAdapter.notifyDataSetChanged();
                    getCurrenciesJson();
                    swipeRefreshLayout.setRefreshing(false);
                }
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
        fetchingDateTimeInfo = findViewById(R.id.fetchingDateTimeInfoTxt);
        currenciesRecView = findViewById(R.id.currenciesRecView);
        gettingCurrencyProgressBar = findViewById(R.id.gettingCurrencyProgressBar);
        swipeRefreshLayout = findViewById(R.id.refreshCurrenciesLayout);
        bottomNavigationView = findViewById(R.id.mainBottomNavigationView);
    }

    // Setting info about last fetcing date and time
    private void setFetchingInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String lastFetchingTime = sharedPreferences.getString(LAST_FETCHING_DATE_TIME, "");

        if (!lastFetchingTime.equals("")) {
            LocalDateTime localDateTime = LocalDateTime.parse(lastFetchingTime);

            // Add 0 to string month value for minutes from 0 to 9
            String month = String.valueOf(localDateTime.getMonthValue());
            switch (month.length()){
                case 1:
                    month = "0" + month;
                    break;
                default:
                    break;
            }

            // Add 0 to string minutes value for minutes from 0 to 9
            String minutes = String.valueOf(localDateTime.getMinute());
            switch (minutes.length()){
                case 1:
                    minutes = "0" + minutes;
                    break;
                default:
                    break;
            }

            fetchingDateTimeInfo.setText("Обновлено " + localDateTime.getDayOfMonth() + "." + month + "." + localDateTime.getYear() +
                    " в " + localDateTime.getHour() + ":" + minutes);
        }
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

    // Check for first app launch
    private boolean isFirstLaunch(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean(FIRST_LAUNCH, true);

        if (firstStart)
            return true;
        return false;
    }

    // Get intent
    private void getIntentFromMainActivity(){
        Intent intent = getIntent();
        if (intent.getStringExtra(MainActivity.EXTRA_MAIN_BACKGROUND_METHOD).equals("getCurrenciesJson")){
            //getCurrenciesJson();
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Title")
                            .setContentText("Notification text");

            Notification notification = builder.build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);

        }
    }

    // TODO
    // Set alarm manager for every day in 12:00
    private void setBackgroundAlarmManager(){

        AlarmManager mAlarmManger = (AlarmManager) getSystemService(MainActivity.this.ALARM_SERVICE);

        //Create pending intent and register it
        Intent intent = new Intent(MainActivity.this, JsonFetchingBackgroundActivity.class);
        intent.putExtra(MainActivity.EXTRA_MAIN_BACKGROUND_METHOD, "getCurrenciesJson");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Set timer you want alarm to work
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 2);
        calendar.set(Calendar.SECOND, 0);

        // Set that timer as a RTC Wakeup to alarm manager object
        mAlarmManger.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    // Adding currencies to recycler view
    private void addCurrenciesToRecView(){

        // Remove progress bar when show recyvler view
        gettingCurrencyProgressBar.setVisibility(View.GONE);
        // Set enabler swipre refresh layout
        swipeRefreshLayout.setEnabled(true);

        currencyAdapter = new CurrencyAdapter(new ArrayList<>(currencyDataBase.currencyDao().getAll()),
                MainActivity.this);

        currenciesRecView.setAdapter(currencyAdapter);
        currenciesRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
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

    // Save last fetching date and time to shared preferences
    private void saveSharedPreferences(LocalDateTime localDateTime){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_FETCHING_DATE_TIME, localDateTime.toString());
        editor.apply();
    }

    // Save info about first launch
    private void saveFirstLaunchInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_LAUNCH, false);
        editor.apply();
    }

    private void getCurrenciesJson(){
        new FetchCurrenciesJson().execute();
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
            swipeRefreshLayout.setEnabled(false);
            gettingCurrencyProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object o) {
            addCurrenciesToDataBase(currentDateTime, currenciesJsonObject);
            saveSharedPreferences(currentDateTime);
            setFetchingInfo();
            addCurrenciesToRecView();
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