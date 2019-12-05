package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BookReviewDao {

    @Query("SELECT * FROM Review WHERE book_bookId= :bookId")
    List<Review> getReviewsOfBook(int bookId);
}
