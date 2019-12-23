package dataaccess.setup;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import dataaccess.room.AuditBookDao;
import dataaccess.room.AuditDao;
import dataaccess.room.AuditStatusDao;
import dataaccess.room.AuthorDao;
import dataaccess.room.BookAuthorDao;
import dataaccess.room.BookColorDao;
import dataaccess.room.BookDao;
import dataaccess.room.BookGenreDao;
import dataaccess.room.BookRatingDao;
import dataaccess.room.BookReviewDao;
import dataaccess.room.ColorDao;
import dataaccess.room.GenreDao;
import dataaccess.room.RatingDao;
import dataaccess.room.ReviewDao;
import dataaccess.room.StatusDao;
import dataaccess.room.UserAuditDao;
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
import dataaccess.room.UserDao;

@Database(entities = {User.class, Audit.class, Status.class, Book.class, BookGenre.class, Genre.class, BookAuthor.class, Author.class, Color.class, Review.class, Rating.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    //Credit to Nikhil from https://stackoverflow.com/questions/50103232/using-singleton-within-the-android-room-library/50105730 for the singleton solution


    private static final String DATABASE_NAME = "koobook_db";
    private static volatile  AppDatabase instance;

    //It's syncronised to prevent multiple instance sbeing created from diifferent different threads
    //This method allows me to get an single instance of this class such that I use it throughout the different acitivities/fragments
    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = create(context);

        } return instance;

    }


    private static AppDatabase create(final Context context){
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();

    }



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
