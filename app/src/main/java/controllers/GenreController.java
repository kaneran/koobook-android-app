package controllers;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataaccess.setup.AppDatabase;
import entities.Book;
import entities.Genre;
import extras.Helper;

public class GenreController {
    UserController userController;
    BookController bookController;
    Helper helper;
    List<Book> books;
    List<String> genres;
    Context context;
    AppDatabase db;
    int userId;

    public GenreController(Context context) {
        this.context = context;
        userController = new UserController();
        helper = new Helper();
        books = new ArrayList<>();
        genres = new ArrayList<>();
        bookController = new BookController(context);
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        userId = userController.getUserIdFromSharedPreferneces(context);
    }

    public List<Pair<String, Integer>> getTopGenresOfBooksWrittenByAuthorAndLikedByUser(String author) {
        books = bookController.getBooks(userId, db, BookController.BookStatus.Liked,null);
        books = bookController.getLikedBooksWrittenByAuthor(author, books);

        List<String> genresFromLikedBooksWrittenByAuthor = getGenresOfBooks(books);
        HashMap<String, Integer> mostLikedGenresHashMap = helper.getOccurencesOfStringList(genresFromLikedBooksWrittenByAuthor);
        Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostLikedGenresHashMap);
        List<Pair<String,Integer>> topFiveMostLikedGenresWrittenByAuthor = helper.getTopPairs(sortedHashMapByIntegerValue);
        return topFiveMostLikedGenresWrittenByAuthor;

    }

    public List<String> getGenresOfBooks(List<Book> books){
        for(Book book: books){

            List<Integer> genreIds = db.bookGenreDao().getGenreIdsOfBook(book.getBookId());
            for(int genreId: genreIds){
                String genreLabel = db.genreDao().getGenreLabel(genreId);
                genres.add(genreLabel);
            }
        }
        return genres;
    }

    public String getMostLikedGenreText(List<Pair<String,Integer>> data){
        if(data.size() == 1){
            return "Undetermined";

        } else{
            if(data.get(0).second.equals(data.get(1).second)){
                return "Undetermined";
            } else{
                return data.get(0).first;
            }

        }

    }

}

