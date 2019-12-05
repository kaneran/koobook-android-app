package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

@Dao
public interface BookRatingDao {

    @Query("SELECT * FROM Rating WHERE book_bookId = :bookId")
    Rating getRatings(int bookId);
}
