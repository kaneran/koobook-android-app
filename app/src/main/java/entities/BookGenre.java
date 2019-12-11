package entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = {@ForeignKey(entity = Book.class, parentColumns = "bookId", childColumns = "book_bookId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Genre.class, parentColumns = "genreId", childColumns = "genre_genreId", onDelete = ForeignKey.CASCADE)})
public class BookGenre {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int bookGenreId;

    @ColumnInfo(name = "book_bookId")
    private int bookId;

    @ColumnInfo(name = "genre_genreId")
    private int genreId;

    public BookGenre(int bookGenreId, int bookId, int genreId) {
        this.bookGenreId = bookGenreId;
        this.bookId = bookId;
        this.genreId = genreId;
    }

    public int getBookGenreId() {
        return bookGenreId;
    }

    public void setBookGenreId(int bookGenreId) {
        this.bookGenreId = bookGenreId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
}
