package entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = {@ForeignKey(entity = Book.class, parentColumns = "bookId", childColumns = "book_bookId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Author.class, parentColumns = "authorId", childColumns = "author_authorId", onDelete = ForeignKey.CASCADE)})
public class BookAuthor {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int bookAuthorId;

    @ColumnInfo(name = "book_bookId")
    private int bookId;

    @ColumnInfo(name = "author_authorId")
    private int authorId;

    public BookAuthor(int bookAuthorId, int bookId, int authorId) {
        this.bookAuthorId = bookAuthorId;
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public int getBookAuthorId() {
        return bookAuthorId;
    }

    public void setBookAuthorId(int bookAuthorId) {
        this.bookAuthorId = bookAuthorId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
}
