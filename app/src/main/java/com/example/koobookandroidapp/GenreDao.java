package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface GenreDao {

    @Insert
    void insertGenre(Genre... genre);

    @Query("SELECT genreId FROM Genre WHERE genreLabel = :genreLabel")
    int getGenreId(String genreLabel);
}
