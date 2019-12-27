package controllers;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

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

    @Override
    protected Boolean doInBackground(String... strings) {
        try{
            receiveBookInformation(strings);
            return true;
        } catch (Exception e){

            return false;
        }

    }

    //Credit to  //Code from https://systembash.com/a-simple-java-tcp-server-and-tcp-client/ and
    // Rodolk from https://stackoverflow.com/questions/19839172/how-to-read-all-of-inputstream-in-server-socket-java for the TCP client implementation
    public void receiveBookInformation(String... strings){
        byte[] messageByte = new byte[1000];
        boolean end = false;
        String dataString = "";
        try{
            String data;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("192.168.1.141",9876);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            //DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            //outToServer.writeBytes("Oi oi");
            while(!end){
                int bytesRead = in.read(messageByte);
                dataString += new String(messageByte, 0, bytesRead);
                if(dataString.length()> 0){
                    end = true;
                }

            }
            clientSocket.close();
        } catch (Exception e){
            e.printStackTrace();
        }



    }
}
