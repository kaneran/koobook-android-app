package dataaccess.setup;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import dataaccess.AuditBookDao;
import dataaccess.AuditDao;
import dataaccess.AuditStatusDao;
import dataaccess.AuthorDao;
import dataaccess.BookAuthorDao;
import dataaccess.BookColorDao;
import dataaccess.BookDao;
import dataaccess.BookGenreDao;
import dataaccess.BookRatingDao;
import dataaccess.BookReviewDao;
import dataaccess.ColorDao;
import dataaccess.GenreDao;
import dataaccess.RatingDao;
import dataaccess.ReviewDao;
import dataaccess.StatusDao;
import dataaccess.UserAuditDao;
import entities.Audit;
import entities.Author;
import entities.Book;
import entities.BookAuthor;
import entities.BookGenre;
import entities.Color;
import entities.Genre;
import entities.Rating;
import entities.Review;
import entities.Status;
import entities.User;
import dataaccess.UserDao;

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
