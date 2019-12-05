package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ReviewDao {

    @Insert
    void insertReview(Review... review);

    @Query("SELECT * FROM Review")
    List<Review> getReviews();
}
