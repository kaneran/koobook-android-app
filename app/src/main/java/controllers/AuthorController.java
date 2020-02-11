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
        this.context = context;
        helper = new Helper();
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        userId = userController.getUserIdFromSharedPreferneces(context);
        authors = new ArrayList<>();
    }

    //This method works by getting all the books liked by the user and storing it into a list. All the authors name's are then retrieved from the list
    //and also stored into a list. A hashmap is created to store the authors name and the number of times it appeared within the list of author names
    //This hashmap is then sorted such that the most popular authors are at the top of the list. Finally, the sorted list is then used to get the top 5
    //authors which Returns a list of pairs containing the author name and its number of occurences from the author names list.
    public List<Pair<String,Integer>> getMostLikedAuthors(){
     books = bookController.getBooksUsingStatus(userId, BookController.BookStatus.Liked, null);
     List<String> authorNamesFromLikedBooks = getAuthorNamesOfBooks(books);
     HashMap<String, Integer> mostLikedAuthorHashMap = helper.getOccurencesOfStringList(authorNamesFromLikedBooks);
     Object[] sortedHashMapByIntegerValue = helper.sortHashMapBasedOnKeyValue(mostLikedAuthorHashMap);
     List<Pair<String,Integer>> topFiveMostLikedAuthors = helper.getTopPairs(sortedHashMapByIntegerValue);
     return topFiveMostLikedAuthors;
    }

    //This methods works exactly the same as for getting the most liked authors(see previous method) excepts the books to collect will be based on the status "Disliked"
    public List<Pair<String,Integer>> getMostDislikedAuthors(){
        books = bookController.getBooksUsingStatus(userId, BookController.BookStatus.Disliked, null);
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


    //For each book in the list of books which it takes as its arugment, get the authors ids based on the book's id
    //then for each author id from the list of author ids, get the author name and store into the list of author names
    //before returning the list of author names, the elements that contain empty values are removed from the list.
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
