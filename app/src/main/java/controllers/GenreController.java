package controllers;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dataaccess.setup.AppDatabase;
import entities.Book;
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
        books = bookController.getBooksUsingStatus(userId, db, BookController.BookStatus.Liked,null);
        books = bookController.getLikedBooksWrittenByAuthor(author, books);

        List<String> genresFromLikedBooksWrittenByAuthor = getGenresOfBooks(books);
        HashMap<String, Integer> mostLikedGenresHashMap = helper.getOccurencesOfStringList(genresFromLikedBooksWrittenByAuthor);
        Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostLikedGenresHashMap);
        List<Pair<String,Integer>> topFiveMostLikedGenresWrittenByAuthor = helper.getTopPairs(sortedHashMapByIntegerValue);
        return topFiveMostLikedGenresWrittenByAuthor;

    }

    public List<Pair<String,Integer>> getMostDislikedGenres(){
        BookController bookController = new BookController(context);
        List<Book> books = bookController.getBooksUsingStatus(userId, db, BookController.BookStatus.Disliked, BookController.DislikedBookReason.Genre);
        List<String> genresFromDislikedBooks = getGenresOfBooks(books);
        HashMap<String, Integer> mostDislikedGenreHashMap = helper.getOccurencesOfStringList(genresFromDislikedBooks);
        Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostDislikedGenreHashMap);
        List<Pair<String,Integer>> topFiveMostDislikedGenres = helper.getTopPairs(sortedHashMapByIntegerValue);
        return topFiveMostDislikedGenres;
    }

    public List<Pair<String,Integer>> getMostLikedGenres(){
        BookController bookController = new BookController(context);
        List<Book> books = bookController.getBooksUsingStatus(userId, db, BookController.BookStatus.Liked, null);
        List<String> genresFromLikedBooks = getGenresOfBooks(books);
        HashMap<String, Integer> mostLikedGenreHashMap = helper.getOccurencesOfStringList(genresFromLikedBooks);
        Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostLikedGenreHashMap);
        List<Pair<String,Integer>> topFiveMostLikedGenres = helper.getTopPairs(sortedHashMapByIntegerValue);
        return topFiveMostLikedGenres;
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

    public List<String> getUniqueGenres(List<String> genres){
        Set<String> uniqueGenres= new HashSet<>(genres);
        return new ArrayList<String>(uniqueGenres);
    }

    public String getTopGenreFromPairData(List<Pair<String,Integer>> data){
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

    public String getMostLikedGenre(){
        List<Pair<String,Integer>> data = getMostLikedGenres();
        return getTopGenreFromPairData(data);
    }

    public String getMostDislikedGenre(){
        List<Pair<String,Integer>> data = getMostDislikedGenres();
        return getTopGenreFromPairData(data);
    }

}

