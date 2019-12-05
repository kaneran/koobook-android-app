package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface ColorDao {

    @Insert
    void insertColor(Color... color);
}
