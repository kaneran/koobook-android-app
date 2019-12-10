package com.example.koobookandroidapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import User.User;
import User.UserDao;

@Database(entities = {User.class, Audit.class, Status.class, Book.class, BookGenre.class, Genre.class, BookAuthor.class, Author.class, Color.class, Review.class, Rating.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AuthorDao authorDao();
    public abstract BookAuthorDao bookAuthorDao();
    public abstract AuditBookDao auditBookDao();
    public abstract BookReviewDao bookReviewDao();
    public abstract RatingDao ratingDao();
    public abstract AuditStatusDao auditStatusDao();
    public abstract StatusDao statusDao();
    public abstract AuditDao auditDao();
    public abstract GenreDao genreDao();
    public abstract BookGenreDao bookGenreDao();
    public abstract UserAuditDao userAuditDao();
    public abstract BookRatingDao bookRatingDao();
    public abstract BookDao bookDao();
    public abstract ColorDao colorDao();
    public abstract ReviewDao reviewDao();
    public abstract BookColorDao bookColorDao();

}
