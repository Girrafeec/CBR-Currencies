package com.girrafeecstud.cbrcurrencies;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Date.class, Currency.class}, version = 1, exportSchema = false)
public abstract class CurrencyDataBase extends RoomDatabase {

    // Define database name
    private static final String DATABASE_NAME = "currency_database";
    // Create database instance
    private static CurrencyDataBase currencyDataBase;

    public synchronized static CurrencyDataBase getInstance(Context context){

        // Create database is it does not exist
        if (currencyDataBase == null){
            currencyDataBase = Room.databaseBuilder(context.getApplicationContext(),
                    CurrencyDataBase.class,
                    DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        // Return database instance
        return currencyDataBase;
    }

    // Create Date Dao
    public abstract DateDao dateDao();

    // Create currencies Dao
    public abstract CurrencyDao currencyDao();

}
