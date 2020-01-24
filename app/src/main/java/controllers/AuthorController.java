package controllers;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dataaccess.setup.AppDatabase;
import entities.Author;
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
     books = bookController.getLikedBooks(userId, db);
     List<String> authorNamesFromLikedBooks = getAuthorNamesOfBooks(books);
     HashMap<String, Integer> mostLikedAuthorHashMap = helper.getOccurencesOfStringList(authorNamesFromLikedBooks);
     Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostLikedAuthorHashMap);
     List<Pair<String,Integer>> topFiveMostLikedAuthors = helper.getTopPairs(sortedHashMapByIntegerValue);
     return topFiveMostLikedAuthors;
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


}
