package com.girrafeecstud.cbrcurrencies;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "dates")
@TypeConverters({LocalDateTimeConverter.class})
public class UpdateDateTime implements Serializable {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private LocalDateTime timeStamp;

    public UpdateDateTime(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
