package com.girrafeecstud.cbrcurrencies;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "dates")
public class Date implements Serializable {
    @PrimaryKey(autoGenerate = false)
    LocalDateTime timeStamp;

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
