package com.girrafeecstud.cbrcurrencies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
// TODO
public class JsonFetchingBackgroundActivity extends BroadcastReceiver {

    private CurrencyDataBase currencyDataBase;

    @Override
    public void onReceive(Context context, Intent intent) {

        initDataBase(context);

        if (intent.getStringExtra(MainActivity.EXTRA_MAIN_BACKGROUND_METHOD).equals("getCurrenciesJson")) {
            if (hasNetworkConnection(context)){
                getCurrenciesJson(context);
                showSuccessNotification(context);
            }
            else {
                Log.i("background json", "unsucceed");
                showUnsuccessNotification(context);
            }
        }
    }

    // Initialization of database
    private void initDataBase(Context context){
        currencyDataBase = CurrencyDataBase.getInstance(context);
    }

    // Show refresh success notification
    private void showSuccessNotification(Context context){

        // Setting bitmap for large icon
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground, options);

        NotificationChannel channel = new NotificationChannel("FetcingCurrenciesNotification", "FetcingCurrenciesNotification", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        // Create PendingIntent
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FetcingCurrenciesNotification")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO поставить иконку приложения в конце
                .setLargeIcon(bitmap)
                .setContentTitle("Обновление курсов")
                .setContentText("Курсы валют ЦБ обновлены")
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_VIBRATE);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());

        Log.i("notification", "showed");
    }

    // Show refresh unsuccess notification
    private void showUnsuccessNotification(Context context){

        // Setting bitmap for large icon
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground, options);

        NotificationChannel channel = new NotificationChannel("FetcingCurrenciesNotification", "FetcingCurrenciesNotification", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        // Create PendingIntent
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FetcingCurrenciesNotification")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(bitmap)
                .setContentTitle("Обновление курсов")
                .setContentText("Невозможно обновить курсы валют ЦБ без наличия доступа в интернет")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Невозможно обновить курсы валют ЦБ без наличия доступа в интернет"))
                .setContentIntent(resultPendingIntent)
                .setOngoing(true)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_VIBRATE);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());

        Log.i("unsuccess notification", "showed");
    }

    // Chek if app has network connection
    private boolean hasNetworkConnection(Context context){
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
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
    private void saveSharedPreferences(LocalDateTime localDateTime, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.LAST_FETCHING_DATE_TIME, localDateTime.toString());
        editor.apply();
    }

    private void getCurrenciesJson(Context context){
        new JsonFetchingBackgroundActivity.FetchCurrenciesJson(context).execute();
    }

    // Class for background fetching data from Json url
    private class FetchCurrenciesJson extends AsyncTask{

        private Context context;

        private LocalDateTime currentDateTime;

        private JSONObject currenciesJsonObject;

        private String currenciesJsonData;

        public FetchCurrenciesJson(Context context) {
            this.context = context;
            this.currenciesJsonData = "";
            this.currentDateTime = null;
            this.currenciesJsonObject = null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Object o) {
            addCurrenciesToDataBase(currentDateTime, currenciesJsonObject);
            saveSharedPreferences(currentDateTime, context);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            fetchCurrenciesJson();
            return null;
        }

        private void fetchCurrenciesJson(){

            try {
                // Start https connection
                URL cbrCurrenciesJsonUrl = new URL(MainActivity.CBR_CURRENCIES_JSON_URL);
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

                Log.i("background json", "succeed");

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