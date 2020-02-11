package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import entities.BookAuthor;

import java.util.List;

@Dao
public interface BookAuthorDao {

    @Query("SELECT author_authorId FROM BookAuthor WHERE book_bookId = :bookId")
    List<Integer> getAuthorIdsOfBook(int bookId);

    @Query("SELECT book_bookId FROM BookAuthor WHERE author_authorId = :authorId")
    List<Integer> getAuthorsBookIds(int authorId);

    @Query("SELECT * FROM BookAuthor WHERE author_authorId = :authorId AND book_bookId = :bookId")
    BookAuthor getBookAuthor(int authorId, int bookId);

    @Query("SELECT * FROM BookAuthor WHERE book_bookId = :bookId")
    List<BookAuthor> getBookAuthorsUsingBookId(int bookId);

    @Delete
    void deleteBookAuthor(BookAuthor bookAuthor);

    @Insert
    void insertBookAuthor(BookAuthor... bookAuthor);
}
