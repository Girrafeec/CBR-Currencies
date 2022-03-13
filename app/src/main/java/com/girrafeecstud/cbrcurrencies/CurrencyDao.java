package com.girrafeecstud.cbrcurrencies;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CurrencyDao {

    // Insert query
    @Insert(onConflict = REPLACE)
    void insert(Currency currency);

    // Get all data from table  with the same date
    @Query("SELECT * FROM currencies")
    List<Currency> getAll();

    // Get one currency value from selected date
    @Query("SELECT * FROM currencies WHERE currencyid= :sCurrencyId")
    Currency getById(String sCurrencyId);

}
