package com.example.koobookandroidapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "user_userId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Book.class, parentColumns = "bookId", childColumns = "book_bookId", onDelete = ForeignKey.CASCADE)})
public class Audit {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int auditId;

    @ColumnInfo(name = "user_userId")
    private int userId;

    @ColumnInfo(name = "book_bookId")
    private int bookId;

    public Audit(int auditId, int userId, int bookId) {
        this.auditId = auditId;
        this.userId = userId;
        this.bookId = bookId;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
