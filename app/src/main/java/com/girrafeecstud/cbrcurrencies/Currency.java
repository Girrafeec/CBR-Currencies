package com.girrafeecstud.cbrcurrencies;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "currencies", primaryKeys = {"timeStamp", "currencyId"})
public class Currency implements Serializable {

    @ColumnInfo(name = "timeStamp")
    private LocalDateTime timeStamp;

    @ColumnInfo(name = "currencyid")
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
    private float value;

    @ColumnInfo (name = "previousvalue")
    private String previousValue;

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCurrencyID() {
        return currencyId;
    }

    public void setCurrencyID(String currencyId) {
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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }
}
