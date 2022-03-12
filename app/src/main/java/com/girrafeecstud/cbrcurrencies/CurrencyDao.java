package com.girrafeecstud.cbrcurrencies;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.time.LocalDateTime;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CurrencyDao {

    // Insert query
    @Insert(onConflict = REPLACE)
    void insert(Currency currency);

    // Get all data from table  with the same date
    @Query("SELECT * FROM currencies WHERE timeStamp= :sTimeStamp")
    List<Currency> getAll(LocalDateTime sTimeStamp);

    // Get one currency value from selected date
    @Query("SELECT * FROM currencies WHERE timeStamp= :sTimeStamp AND currencyid= :sCurrencyId")
    Currency getAll(LocalDateTime sTimeStamp, String sCurrencyId);

}
