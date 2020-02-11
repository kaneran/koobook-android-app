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

    //This method works by first working with the book controller to get the all the books that the user liked. It then proceeds to
    //filter down these books by using the book controller to get the liked books that were written by the author(from the method's arguments).
    //It then proceeds to get all the genre labels affiliated with those books which is a string list. It then uses the Helper class to get the
    //frequency of each genre within the list of genres and this Returns a hashmap where the key is the genre and the value is the frequency of occurences.
    //It then uses this hashmap to sort it based on the frequency value such that a sorted list of objects is returned. It then uses this
    //to get the top 5 genres which have the highest frequency value and this Returns a List of Pair where each Pair contains a string which holds the genre label
    //and an integer which holds the frequency value. This list is returned from this method.
    public List<Pair<String, Integer>> getTopGenresOfBooksWrittenByAuthorAndLikedByUser(String author) {
        books = bookController.getBooksUsingStatus(userId, BookController.BookStatus.Liked,null);
        books = bookController.getLikedBooksWrittenByAuthor(author, books);

        List<String> genresFromLikedBooksWrittenByAuthor = getGenresOfBooks(books);
        HashMap<String, Integer> mostLikedGenresHashMap = helper.getOccurencesOfStringList(genresFromLikedBooksWrittenByAuthor);
        Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostLikedGenresHashMap);
        List<Pair<String,Integer>> topFiveMostLikedGenresWrittenByAuthor = helper.getTopPairs(sortedHashMapByIntegerValue);
        return topFiveMostLikedGenresWrittenByAuthor;

    }

    //This method works by first working with the book controller to get the all the books that the user disliked and where the reason
    //for disliking the books was due to the genre.
    //It then proceeds to get all the genre labels affiliated with those books which is a string list. It then uses the Helper class to get the
    //frequency of each genre within the list of genres and this Returns a hashmap where the key is the genre and the value is the frequency of occurences.
    //It then uses this hashmap to sort it based on the frequency value such that a sorted list of objects is returned. It then uses this
    //to get the top 5 genres which have the highest frequency value and this Returns a List of Pair where each Pair contains a string which holds the genre label
    //and an integer which holds the frequency value. This list is returned from this method.
    public List<Pair<String,Integer>> getMostDislikedGenres(){
        BookController bookController = new BookController(context);
        List<Book> books = bookController.getBooksUsingStatus(userId,BookController.BookStatus.Disliked, BookController.DislikedBookReason.Genre);
        List<String> genresFromDislikedBooks = getGenresOfBooks(books);
        HashMap<String, Integer> mostDislikedGenreHashMap = helper.getOccurencesOfStringList(genresFromDislikedBooks);
        Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostDislikedGenreHashMap);
        List<Pair<String,Integer>> topFiveMostDislikedGenres = helper.getTopPairs(sortedHashMapByIntegerValue);
        return topFiveMostDislikedGenres;
    }

    //This method works by first working with the book controller to get the all the books that the user liked.
    //It then proceeds to get all the genre labels affiliated with those books which is a string list. It then uses the Helper class to get the
    //frequency of each genre within the list of genres and this Returns a hashmap where the key is the genre and the value is the frequency of occurences.
    //It then uses this hashmap to sort it based on the frequency value such that a sorted list of objects is returned. It then uses this
    //to get the top 5 genres which have the highest frequency value and this Returns a List of Pair where each Pair contains a string which holds the genre label
    //and an integer which holds the frequency value. This list is returned from this method.
    public List<Pair<String,Integer>> getMostLikedGenres(){
        BookController bookController = new BookController(context);
        List<Book> books = bookController.getBooksUsingStatus(userId, BookController.BookStatus.Liked, null);
        List<String> genresFromLikedBooks = getGenresOfBooks(books);
        HashMap<String, Integer> mostLikedGenreHashMap = helper.getOccurencesOfStringList(genresFromLikedBooks);
        Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostLikedGenreHashMap);
        List<Pair<String,Integer>> topFiveMostLikedGenres = helper.getTopPairs(sortedHashMapByIntegerValue);
        return topFiveMostLikedGenres;
    }

    //This methods works by first iterating through each book from the list of books(passed into the method's arguments) and uses it to
    //get the genre ids from the BookGenre table in the Room database.
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

    //This methods works by getting only the unique/distinct genres from the list of genre passed into the method's argument. This was achieved by using a HashSet which does not tolerate duplicates
    public List<String> getUniqueGenres(List<String> genres){
        Set<String> uniqueGenres= new HashSet<>(genres);
        return new ArrayList<>(uniqueGenres);
    }

    //This methods Returns a string which is the top genre from the list of genres/frequency pairs(passed into the method's arguments). If there is only on pair or the top two pairs are equal to one another
    // in terms of frequency, then the string returned will be "Undetermined" to denote that there was no top genre. Otherwise, the top genre label will be returned.
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


}

