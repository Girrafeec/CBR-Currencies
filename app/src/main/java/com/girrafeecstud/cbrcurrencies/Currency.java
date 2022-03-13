package com.girrafeecstud.cbrcurrencies;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "currencies", primaryKeys = {"currencyid"})
@TypeConverters({LocalDateTimeConverter.class})
public class Currency implements Serializable {

    @ColumnInfo(name = "timestamp")
    private LocalDateTime timeStamp;

    @ColumnInfo(name = "currencyid")
    @NonNull
    private String currencyId;

    @ColumnInfo (name = "numcode")
    private String numCode;

    @ColumnInfo (name = "charcode")
    private String charCode;

    @ColumnInfo (name = "nominal")
    private int nominal;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "value")
    private double value;

    @ColumnInfo (name = "previousvalue")
    private double previousValue;

    public Currency(LocalDateTime timeStamp, String currencyId, String numCode,
                    String charCode, int nominal, String name, double value, double previousValue) {
        this.timeStamp = timeStamp;
        this.currencyId = currencyId;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
        this.previousValue = previousValue;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        currencyId = currencyId;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }
}
