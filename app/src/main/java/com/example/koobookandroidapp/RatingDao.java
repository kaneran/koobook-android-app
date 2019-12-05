package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface RatingDao {

    @Insert
    void insertRatings(Rating... rating);
}
