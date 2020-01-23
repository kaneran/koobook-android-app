package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import entities.Book;

@Dao
public interface BookDao {

    @Query("SELECT * FROM Book WHERE isbnNumber = :isbnNumber")
    Book getBookBasedOnIsbnNumber(String isbnNumber);

    @Query("SELECT * FROM Book WHERE title like :title")
    List<Book> getBookBasedOnTitle(String title);

    @Query("SELECT * FROM Book WHERE bookId = :bookId")
    Book getBookBasedOnBookId(int bookId);

    @Query("UPDATE Book SET upvoteCount = :upvoteCount WHERE isbnNumber = :isbnNumber")
    void updateUpvoteCount(String isbnNumber, int upvoteCount);

    @Insert
    void insertBook(Book... book);

    @Query("SELECT isbnNumber FROM Book WHERE bookId = :bookId")
    String getIsbnNumber(int bookId);
}
