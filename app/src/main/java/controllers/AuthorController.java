package controllers;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dataaccess.setup.AppDatabase;
import entities.Book;
import extras.Helper;

public class AuthorController {
    UserController userController;
    BookController bookController;
    Helper helper;
    Context context;
    AppDatabase db;
    int userId;
    List<Book> books;
    List<String> authors;
    List<Integer> authorIds;

    public AuthorController(Context context) {
        userController = new UserController();
        bookController = new BookController(context);
        helper = new Helper();
        this.context = context;
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        userId = userController.getUserIdFromSharedPreferneces(context);
        authors = new ArrayList<>();
    }

    public List<Pair<String,Integer>> getMostLikedAuthors(){
     books = bookController.getBooksUsingStatus(userId, db, BookController.BookStatus.Liked, null);
     List<String> authorNamesFromLikedBooks = getAuthorNamesOfBooks(books);
     HashMap<String, Integer> mostLikedAuthorHashMap = helper.getOccurencesOfStringList(authorNamesFromLikedBooks);
     Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostLikedAuthorHashMap);
     List<Pair<String,Integer>> topFiveMostLikedAuthors = helper.getTopPairs(sortedHashMapByIntegerValue);
     return topFiveMostLikedAuthors;
    }


    public List<Pair<String,Integer>> getMostDislikedAuthors(){
        books = bookController.getBooksUsingStatus(userId, db, BookController.BookStatus.Disliked, null);
        List<String> authorNamesFromLikedBooks = getAuthorNamesOfBooks(books);
        HashMap<String, Integer> mostDislikedAuthorHashMap = helper.getOccurencesOfStringList(authorNamesFromLikedBooks);
        Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostDislikedAuthorHashMap);
        List<Pair<String,Integer>> topFiveMostDislikedAuthors = helper.getTopPairs(sortedHashMapByIntegerValue);
        return topFiveMostDislikedAuthors;
    }


    //If the first two authors have the same value then set the text value to reflect this
    public String getTopAuthorFromPairData(List<Pair<String,Integer>> data){
        if(data.get(0).second.equals(data.get(1).second)){
            return "Undetermined";
        } else{
            return data.get(0).first;
        }
    }



    public List<String> getAuthorNamesOfBooks(List<Book> books){
        for(Book book : books){
            authorIds = db.bookAuthorDao().getAuthorIdsOfBook(book.getBookId());
            for(int authorId: authorIds){
                String authorName = db.authorDao().getAuthorName(authorId);
                authors.add(authorName);
            }
        }

        authors.removeAll(Arrays.asList(""));
        return authors;
    }

    public String getMostLikedAuthor(){
        List<Pair<String, Integer>> data = getMostLikedAuthors();
        return getTopAuthorFromPairData(data);
    }

    public String getMostDislikedAuthor(){
        List<Pair<String, Integer>> data = getMostDislikedAuthors();
        return getTopAuthorFromPairData(data);
    }


}
