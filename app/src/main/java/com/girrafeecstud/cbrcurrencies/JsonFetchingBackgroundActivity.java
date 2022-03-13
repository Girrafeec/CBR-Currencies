package com.girrafeecstud.cbrcurrencies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
// TODO
public class JsonFetchingBackgroundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentFromMainActivity();
    }

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

}