package controllers;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import dataaccess.setup.AppDatabase;
import entities.Book;

public class BookController extends AsyncTask<String, Void, Boolean> {
    AppDatabase db;
    Book book;
    String isbn;

    //Checks in room database to see if book exists
    public boolean checkIfBookExistsInDatabase(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        isbn = getBookIsbnFromSharedPreferneces(context);
        book = db.bookDao().getBook(isbn);
        return !(book == null);
    }

    //Store the book isbn in a shared preference file
    public boolean storeBookIsbn(Context context, String isbn) {
        try {
            SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("BookPref", Context.MODE_PRIVATE).edit();
            editor.putString("isbn", isbn).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }

    //Retrieve the isbn from the shared preference file
    public String getBookIsbnFromSharedPreferneces(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("BookPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("isbn", "default");
    }



    public Object receiveBookInformation(){


    }
}
