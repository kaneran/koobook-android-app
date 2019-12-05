package com.example.koobookandroidapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Book.class, parentColumns = "bookId", childColumns = "book_bookId", onDelete = ForeignKey.CASCADE))
public class Rating {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int ratingId;

    @ColumnInfo(name = "book_bookId")
    private int bookId;

    @ColumnInfo(name = "overallAverageRating")
    private double overallAverageRating;

    @ColumnInfo(name = "amazonAverageRating")
    private double amazonAverageRating;

    @ColumnInfo(name = "googleBooksAverageRating")
    private double googleBooksAverageRating;

    @ColumnInfo(name = "goodreadsAverageRating")
    private double goodreadsAverageRating;

    @ColumnInfo(name = "amazonFiveStarRatingPercentage")
    private int amazonFiveStarRatingPercentage;

    @ColumnInfo(name = "amazonFourStarRatingPercentage")
    private int amazonFourStarRatingPercentage;

    @ColumnInfo(name = "amazonThreeStarRatingPercentage")
    private int amazonThreeStarRatingPercentage;

    @ColumnInfo(name = "amazonTwoStarRatingPercentage")
    private int amazonTwoStarRatingPercentage;

    @ColumnInfo(name = "amazonOneStarRatingPercentage")
    private int amazonOneStarRatingPercentage;

    @ColumnInfo(name = "amazonReviewsCount")
    private int amazonReviewsCount;

    public Rating(int ratingId, int bookId, double overallAverageRating, double amazonAverageRating, double googleBooksAverageRating, double goodreadsAverageRating, int amazonFiveStarRatingPercentage, int amazonFourStarRatingPercentage, int amazonThreeStarRatingPercentage, int amazonTwoStarRatingPercentage, int amazonOneStarRatingPercentage, int amazonReviewsCount) {
        this.ratingId = ratingId;
        this.bookId = bookId;
        this.overallAverageRating = overallAverageRating;
        this.amazonAverageRating = amazonAverageRating;
        this.googleBooksAverageRating = googleBooksAverageRating;
        this.goodreadsAverageRating = goodreadsAverageRating;
        this.amazonFiveStarRatingPercentage = amazonFiveStarRatingPercentage;
        this.amazonFourStarRatingPercentage = amazonFourStarRatingPercentage;
        this.amazonThreeStarRatingPercentage = amazonThreeStarRatingPercentage;
        this.amazonTwoStarRatingPercentage = amazonTwoStarRatingPercentage;
        this.amazonOneStarRatingPercentage = amazonOneStarRatingPercentage;
        this.amazonReviewsCount = amazonReviewsCount;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public double getOverallAverageRating() {
        return overallAverageRating;
    }

    public void setOverallAverageRating(double overallAverageRating) {
        this.overallAverageRating = overallAverageRating;
    }

    public double getAmazonAverageRating() {
        return amazonAverageRating;
    }

    public void setAmazonAverageRating(double amazonAverageRating) {
        this.amazonAverageRating = amazonAverageRating;
    }

    public double getGoogleBooksAverageRating() {
        return googleBooksAverageRating;
    }

    public void setGoogleBooksAverageRating(double googleBooksAverageRating) {
        this.googleBooksAverageRating = googleBooksAverageRating;
    }

    public double getGoodreadsAverageRating() {
        return goodreadsAverageRating;
    }

    public void setGoodreadsAverageRating(double goodreadsAverageRating) {
        this.goodreadsAverageRating = goodreadsAverageRating;
    }

    public int getAmazonFiveStarRatingPercentage() {
        return amazonFiveStarRatingPercentage;
    }

    public void setAmazonFiveStarRatingPercentage(int amazonFiveStarRatingPercentage) {
        this.amazonFiveStarRatingPercentage = amazonFiveStarRatingPercentage;
    }

    public int getAmazonFourStarRatingPercentage() {
        return amazonFourStarRatingPercentage;
    }

    public void setAmazonFourStarRatingPercentage(int amazonFourStarRatingPercentage) {
        this.amazonFourStarRatingPercentage = amazonFourStarRatingPercentage;
    }

    public int getAmazonThreeStarRatingPercentage() {
        return amazonThreeStarRatingPercentage;
    }

    public void setAmazonThreeStarRatingPercentage(int amazonThreeStarRatingPercentage) {
        this.amazonThreeStarRatingPercentage = amazonThreeStarRatingPercentage;
    }

    public int getAmazonTwoStarRatingPercentage() {
        return amazonTwoStarRatingPercentage;
    }

    public void setAmazonTwoStarRatingPercentage(int amazonTwoStarRatingPercentage) {
        this.amazonTwoStarRatingPercentage = amazonTwoStarRatingPercentage;
    }

    public int getAmazonOneStarRatingPercentage() {
        return amazonOneStarRatingPercentage;
    }

    public void setAmazonOneStarRatingPercentage(int amazonOneStarRatingPercentage) {
        this.amazonOneStarRatingPercentage = amazonOneStarRatingPercentage;
    }

    public int getAmazonReviewsCount() {
        return amazonReviewsCount;
    }

    public void setAmazonReviewsCount(int amazonReviewsCount) {
        this.amazonReviewsCount = amazonReviewsCount;
    }
}
