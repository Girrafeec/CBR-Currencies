package com.girrafeecstud.cbrcurrencies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ConverterActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner convertFromSpinner, convertToSpinner;

    private EditText convertFromEditText, convertResult;

    private Button convertButton, goToMainActivity;

    private ImageButton changeSpinners;

    private BottomNavigationView bottomNavigationView;

    private Toast backToast;

    private LinearLayout noDataError, converterLayout;

    private CurrencyDataBase currencyDataBase;

    private ArrayList<Currency> currenciesArrayList;

    private ArrayList<Currency> rubArrayList;

    private long backPressedTime;

    private static int converterMode = 0;

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
            case R.id.noDataGoToMainBtn:
                ConverterActivity.this.startActivity(new Intent(ConverterActivity.this, MainActivity.class));
                overridePendingTransition(0,0);
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

        backToast = Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT);
        backToast.show();

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        initDataBase();

        initUiValues();

        fillTextFields(savedInstanceState);

        fillCurrenciesArrayList();

        // Show empty data message if we have no data
        if (isDatabaseEmpty()) {
            converterLayout.setVisibility(View.GONE);
            noDataError.setVisibility(View.VISIBLE);
        }
        // Else show converter layout
        else{
            converterLayout.setVisibility(View.VISIBLE);
            noDataError.setVisibility(View.GONE);
            fillConvertFromSpinner();
            fillConvertToSpinner();
        }

        convertButton.setOnClickListener(this);
        changeSpinners.setOnClickListener(this);
        goToMainActivity.setOnClickListener(this);

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

        // First soinner actions
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

        // Second spinner actions
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("convertFrom", convertFromEditText.getText().toString());
        outState.putString("convertTo", convertResult.getText().toString());

    }

    // Fill text fields after rotating screen
    private void fillTextFields(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            convertFromEditText.setText(savedInstanceState.getString("convertFrom"));
            convertResult.setText(savedInstanceState.getString("convertTo"));
        }
    }

    // Initialization of UI values
    private void initUiValues(){
        convertFromEditText = findViewById(R.id.enterCurrencyNumberEdtTxt);
        convertResult = findViewById(R.id.convertResultTxt);
        convertFromSpinner = findViewById(R.id.convertFromSpinner);
        convertToSpinner = findViewById(R.id.convertToSpinner);
        convertButton =  findViewById(R.id.convertBtn);
        changeSpinners = findViewById(R.id.changeSpinnersImgButton);
        goToMainActivity = findViewById(R.id.noDataGoToMainBtn);
        bottomNavigationView = findViewById(R.id.mainBottomNavigationView);
        noDataError = findViewById(R.id.noDataLinLay);
        converterLayout = findViewById(R.id.converterLinLay);

        // Disable opportunity to enter text to convert result edit text
        convertResult.setRawInputType(InputType.TYPE_NULL);
    }

    // Initialization of database
    private void initDataBase(){
        currencyDataBase = CurrencyDataBase.getInstance(ConverterActivity.this);
    }

    // Check if database is empty
    private boolean isDatabaseEmpty(){

        if (currencyDataBase.currencyDao().getAll().isEmpty())
            return true;
        return false;

    }

    // Filling currencies Array List
    private void fillCurrenciesArrayList(){
        currenciesArrayList = new ArrayList<>(currencyDataBase.currencyDao().getAll());
        rubArrayList = new ArrayList<>();
        rubArrayList.add(new Currency(LocalDateTime.parse("2001-06-30T00:00"), "", "643", "RUS", 0, "", 0, 0));
    }

    // Filling first Spinner. spinnerMode = 0 > filling with one item "RUB", spinnerMode = 1 > filling with currencies list
    private void fillConvertFromSpinner(){

        switch (converterMode){
            case 0:
                CurrenciesSpinnerAdapter currenciesRusSpinnerAdapter = new CurrenciesSpinnerAdapter(ConverterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, rubArrayList);
                convertFromSpinner.setAdapter(currenciesRusSpinnerAdapter);
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
                CurrenciesSpinnerAdapter currenciesRusSpinnerAdapter = new CurrenciesSpinnerAdapter(ConverterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, rubArrayList);
                convertToSpinner.setAdapter(currenciesRusSpinnerAdapter);
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

    // Divide currencies
    private void divideValute(){

        if (convertFromEditText.getText().toString().isEmpty())
            numerator = 0;
        else
            numerator = Double.parseDouble(convertFromEditText.getText().toString());

        convertResult.setText(String.format("%.4f", numerator / denominator));

    }

    // Multiple currencies
    private void multipleValute(){

        if (convertFromEditText.getText().toString().isEmpty())
            firstFactor = 0;
        else
            firstFactor = Double.parseDouble(convertFromEditText.getText().toString());

        convertResult.setText(String.format("%.4f", firstFactor * secondFactor));
    }
}