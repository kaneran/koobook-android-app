package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface AuthorDao {

    @Query("SELECT authorId FROM Author WHERE name = :name")
    int getAuthorId(String name);

    @Insert
    void insertAuthor(Author... author);
}
