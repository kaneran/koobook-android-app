package dataaccess;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import entities.Book;

@Dao
public interface BookDao {

    @Query("SELECT * FROM Book WHERE isbnNumber = :isbnNumber")
    Book getBook(String isbnNumber);

    @Query("UPDATE Book SET upvoteCount = :upvoteCount WHERE isbnNumber = :isbnNumber")
    void updateUpvoteCount(String isbnNumber, int upvoteCount);

    @Insert
    void insertBook(Book... book);

    @Query("SELECT isbnNumber FROM Book WHERE bookId = :bookId")
    String getIsbnNumber(int bookId);
}
