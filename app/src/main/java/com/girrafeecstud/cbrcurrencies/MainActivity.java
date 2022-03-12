package com.girrafeecstud.cbrcurrencies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView currenciesRecView;

    private CurrencyDataBase currencyDataBase;

    private CurrencyAdapter currencyAdapter;

    private ArrayList<Currency> currencyDataArrayList = new ArrayList<Currency>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUiValues();
    }

    // Initialization of UI values
    private void initUiValues(){
        currenciesRecView = findViewById(R.id.currenciesRecView);
    }
}