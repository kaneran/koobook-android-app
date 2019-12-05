package com.example.koobookandroidapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Book {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int bookId;

    @ColumnInfo(name = "isbnNumber")
    private String isbnNumber;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "subtitle")
    private String subtitle;

    @ColumnInfo(name = "pageCount")
    private int pageCount;

    @ColumnInfo(name = "summary")
    private String summary;

    @ColumnInfo(name = "thumbnailUrl")
    private String thumbnailUrl;

    @ColumnInfo(name = "upvoteCount")
    private int upvoteCount;

    public Book(int bookId, String isbnNumber, String title, String subtitle, int pageCount, String summary, String thumbnailUrl, int upvoteCount) {
        this.bookId = bookId;
        this.isbnNumber = isbnNumber;
        this.title = title;
        this.subtitle = subtitle;
        this.pageCount = pageCount;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
        this.upvoteCount = upvoteCount;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getIsbnNumber() {
        return isbnNumber;
    }

    public void setIsbnNumber(String isbnNumber) {
        this.isbnNumber = isbnNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }
}
