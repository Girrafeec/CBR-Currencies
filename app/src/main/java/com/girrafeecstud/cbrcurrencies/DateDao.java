package com.girrafeecstud.cbrcurrencies;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DateDao {

    // Insert Query
    @Insert(onConflict = REPLACE)
    void insert(UpdateDateTime updateDateTime);

    // Get all queries from dates table
    @Query("SELECT * FROM dates")
    List<UpdateDateTime> getAll();

    // Get last date query from dates table
    @Query("SELECT * FROM dates WHERE timeStamp = (SELECT MAX(timeStamp) FROM dates)")
    UpdateDateTime getLastDate();
}
