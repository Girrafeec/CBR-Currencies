package com.girrafeecstud.cbrcurrencies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ConverterActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner convertFromSpinner, convertToSpinner;

    private EditText convertFromEditText;

    private TextView convertResult;

    private Button convertButton;

    private ImageButton changeSpinners;

    private BottomNavigationView bottomNavigationView;

    private Toast backToast;

    private CurrencyDataBase currencyDataBase;

    private ArrayList<Currency> currenciesArrayList;

    private ArrayList<String> rubArrayList;

    private long backPressedTime;

    private static int converterMode = 1;

    private static double numerator = 0, denominator = 0, firstFactor = 0, secondFactor = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.convertBtn:
                convertValute();
                break;
            case R.id.changeSpinnersImgButton:
                changeConvertSpinners();
                break;
            default:
                break;
        }
    }

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
        setContentView(R.layout.activity_converter);

        initDataBase();

        fillCurrenciesArrayList();

        initUiValues();

        fillConvertFromSpinner();
        fillConvertToSpinner();

        convertButton.setOnClickListener(this);
        changeSpinners.setOnClickListener(this);

        bottomNavigationView.setSelectedItemId(R.id.converterMenuItem);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rateMenuItem:
                        ConverterActivity.this.startActivity(new Intent(ConverterActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.converterMenuItem:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        convertFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (converterMode){
                    case 0:
                        break;
                    case 1:
                        // Set second factor as chosen valute
                        secondFactor = currenciesArrayList.get(i).getValue();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        convertToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (converterMode){
                    case 0:
                        // Set denominator as chosen valute
                        denominator = currenciesArrayList.get(i).getValue();
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Initialization of UI values
    private void initUiValues(){
        convertFromEditText = findViewById(R.id.enterCurrencyNumberEdtTxt);
        convertResult = findViewById(R.id.convertResultTxt);
        convertFromSpinner = findViewById(R.id.convertFromSpinner);
        convertToSpinner = findViewById(R.id.convertToSpinner);
        convertButton =  findViewById(R.id.convertBtn);
        changeSpinners = findViewById(R.id.changeSpinnersImgButton);
        bottomNavigationView = findViewById(R.id.mainBottomNavigationView);
    }

    // Initialization of database
    private void initDataBase(){
        currencyDataBase = CurrencyDataBase.getInstance(ConverterActivity.this);
    }

    // Filling currencies Array List
    private void fillCurrenciesArrayList(){
        currenciesArrayList = new ArrayList<>(currencyDataBase.currencyDao().getAll());
        rubArrayList = new ArrayList<>();
        rubArrayList.add("RUB");
    }

    // Filling first Spinner. spinnerMode = 0 > filling with one item "RUB", spinnerMode = 1 > filling with currencies list
    private void fillConvertFromSpinner(){

        switch (converterMode){
            case 0:
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                        ConverterActivity.this, android.R.layout.simple_spinner_dropdown_item,
                        rubArrayList);
                convertFromSpinner.setAdapter(spinnerAdapter);
                break;
            case 1:
                CurrenciesSpinnerAdapter currenciesSpinnerAdapter = new CurrenciesSpinnerAdapter(ConverterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, currenciesArrayList);
                convertFromSpinner.setAdapter(currenciesSpinnerAdapter);
                break;
            default:
                break;
        }
    }

    // Filling second Spinner. spinnerMode = 0 > filling with one item "RUB", spinnerMode = 1 > filling with currencies list
    private void fillConvertToSpinner(){

        switch (converterMode){
            case 0:
                CurrenciesSpinnerAdapter currenciesSpinnerAdapter = new CurrenciesSpinnerAdapter(ConverterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, currenciesArrayList);
                convertToSpinner.setAdapter(currenciesSpinnerAdapter);
                break;
            case 1:
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                        ConverterActivity.this, android.R.layout.simple_spinner_dropdown_item,
                        rubArrayList);
                convertToSpinner.setAdapter(spinnerAdapter);
                break;
            default:
                break;
        }
    }

    // Change spinners
    private void changeConvertSpinners(){
        switch (converterMode){
            case 0:
                converterMode = 1;
                fillConvertFromSpinner();
                fillConvertToSpinner();
                break;
            case 1:
                converterMode = 0;
                fillConvertFromSpinner();
                fillConvertToSpinner();
                break;
            default:
                break;
        }
    }

    // Converting entered valute
    private void convertValute(){
        switch (converterMode){
            case 0:
                divideValute();
                break;
            case 1:
                multipleValute();
                break;
            default:
                break;
        }
    }

    // divide currencies
    private void divideValute(){

        if (convertFromEditText.getText().equals(""))
            numerator = Double.parseDouble(convertFromEditText.getHint().toString());
        else
            numerator = Double.parseDouble(convertFromEditText.getText().toString());

        convertResult.setText(String.format("%.4f", numerator / denominator));

    }

    // Multiple currencies
    private void multipleValute(){

        if (convertFromEditText.getText().equals(""))
            firstFactor = Double.parseDouble(convertFromEditText.getHint().toString());
        else
            firstFactor = Double.parseDouble(convertFromEditText.getText().toString());

        convertResult.setText(String.format("%.4f", firstFactor * secondFactor));
    }
}