package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import entities.BookGenre;

import java.util.List;

@Dao
public interface BookGenreDao {

    @Query("SELECT genre_genreId FROM BookGenre WHERE book_bookId = :bookId")
    List<Integer> getGenreIdsOfBook(int bookId);

    @Insert
    void insertBookGenre(BookGenre... bookGenre);

    @Query("SELECT * FROM BookGenre")
    List<BookGenre> getBookGenres();

    @Query("SELECT * FROM BookGenre WHERE book_bookId = :bookId")
    List<BookGenre> getBookGenresUsingBookId(int bookId);

    @Delete
    void deleteBookGenre(BookGenre bookGenre);
}
