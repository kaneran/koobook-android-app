package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BookAuthorDao {

    @Query("SELECT author_authorId FROM BookAuthor WHERE book_bookId = :bookId")
    List<Integer> getAuthorIdsOfBook(int bookId);

    @Query("SELECT book_bookId FROM BookAuthor WHERE author_authorId = :authorId")
    List<Integer> getAuthorsBookIds(int authorId);

    @Insert
    void insertBookAuthor(BookAuthor... bookAuthor);
}
