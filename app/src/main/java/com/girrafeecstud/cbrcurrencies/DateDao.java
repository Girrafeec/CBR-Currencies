package com.girrafeecstud.cbrcurrencies;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DateDao {

    // Insert Query
    @Insert(onConflict = REPLACE)
    void insert(Date date);

    // Get all queries from dates table
    @Query("SELECT * FROM dates")
    List<Date> getAll();

    // Get last date query from dates table
    @Query("SELECT * FROM dates WHERE timeStamp = (SELECT MAX(timeStamp) FROM dates)")
    Date getLastDate();
}
