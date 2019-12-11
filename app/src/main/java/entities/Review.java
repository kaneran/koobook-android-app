package entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Book.class, parentColumns = "bookId", childColumns = "book_bookId", onDelete = ForeignKey.CASCADE))
public class Review {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int reviewId;

    @ColumnInfo(name = "book_bookId")
    private int bookId;

    @ColumnInfo(name = "review")
    private String review;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Review(int reviewId, int bookId, String review) {
        this.reviewId = reviewId;
        this.bookId = bookId;
        this.review = review;
    }
}
