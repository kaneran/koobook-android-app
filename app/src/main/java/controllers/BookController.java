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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataaccess.setup.AppDatabase;
import entities.Book;

public class BookController extends AsyncTask<String, Void, Boolean> {
    AppDatabase db;
    Book book;
    String isbn;
    HashMap<BookData,String> bookDataMap;
    String[] genres;
    String[] reviews;
    String[] authors;

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
        String bookDataString = "";
        String fromReceiver = "";
        try{
            String data;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("192.168.1.252",9876);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //Send isbn number to the server
            outToServer.writeBytes(isbn + "#");

            //Receive the book data from the server
            while(!end){
                int bytesRead = in.read(messageByte);
                bookDataString += new String(messageByte, 0, bytesRead);
                if(bookDataString.length()> 0){
                    end = true;
                }

            }
            //reset the end boolean value and dataString value
            end = false;

            //Send a FIN message to the server to tell it that it is ready to close connection
            outToServer.writeBytes("FIN");
            while(!end){
                int bytesRead = in.read(messageByte);
                fromReceiver += new String(messageByte, 0, bytesRead);

                //Checks to see if the Server acknowledged(ACK) and also it is ready to close connection (FIN)
                if(fromReceiver.length()> 0 && fromReceiver.contains("FIN") && fromReceiver.contains("ACK")){
                    outToServer.writeBytes("ACK");

                }
                //Check to see if the server has proceeded to close its connection from its side
                if(fromReceiver.length()>0 && fromReceiver.contains("CLOSED")){
                    end = true;
                }
            }
            clientSocket.close();
            bookDataMap = EncodeBookData(bookDataString);
            genres = SplitStringsAndPutIntoList(bookDataMap.get(BookData.Genres));
            authors = SplitStringsAndPutIntoList(bookDataMap.get(BookData.Authors));
            reviews = SplitStringsAndPutIntoList(bookDataMap.get(BookData.AmazonReviews));
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public HashMap<BookData,String> EncodeBookData(String bookData) {
        char[] chars  = bookData.toCharArray();
        HashMap<BookData,String> bookDataMap = new HashMap<BookData,String>();
        StringBuilder sb = new StringBuilder();
        int bookDataIndex = 0;
        for(int i =0; i<chars.length; i++) {

            if(chars[i] == '$') {
                bookDataIndex++;
                switch(bookDataIndex){
                    case 1:
                        bookDataMap.put(BookData.Title, sb.toString());
                    case 2:
                        bookDataMap.put(BookData.Authors, sb.toString());
                    case 3:
                        bookDataMap.put(BookData.Description, sb.toString());
                    case 4:
                        bookDataMap.put(BookData.Subtitle, sb.toString());
                    case 5:
                        bookDataMap.put(BookData.Isbn, sb.toString());
                    case 6:
                        bookDataMap.put(BookData.GoogleBooksAverageRating, sb.toString());
                    case 7:
                        bookDataMap.put(BookData.GoodreadsAverageRating, sb.toString());
                    case 8:
                        bookDataMap.put(BookData.AmazonAverageRating, sb.toString());
                    case 9:
                        bookDataMap.put(BookData.AmazonFiveStarRatingPercentage, sb.toString());
                    case 10:
                        bookDataMap.put(BookData.AmazonFourStarRatingPercentage, sb.toString());
                    case 11:
                        bookDataMap.put(BookData.AmazonThreeStarRatingPercentage, sb.toString());
                    case 12:
                        bookDataMap.put(BookData.AmazonTwoStarRatingPercentage, sb.toString());
                    case 13:
                        bookDataMap.put(BookData.AmazonOneStarRatingPercentage, sb.toString());
                    case 14:
                        bookDataMap.put(BookData.AmazonReviews, sb.toString());
                    case 15:
                        bookDataMap.put(BookData.AmazonReviewsCount, sb.toString());
                    case 16:
                        bookDataMap.put(BookData.Genres, sb.toString());
                    case 17:
                        bookDataMap.put(BookData.PageCount, sb.toString());
                }
                sb.setLength(0);

            }
            else {
                sb.append(chars[i]);

            }

        }

        return bookDataMap;
    }

    public static String[] SplitStringsAndPutIntoList(String str){
        String[] stringArray = str.split("#",5);

        return stringArray;
    }

    public enum BookData {
        Title,
        Authors,
        Description,
        Subtitle,
        Isbn,
        GoogleBooksAverageRating,
        GoodreadsAverageRating,
        AmazonAverageRating,
        AmazonFiveStarRatingPercentage,
        AmazonFourStarRatingPercentage,
        AmazonThreeStarRatingPercentage,
        AmazonTwoStarRatingPercentage,
        AmazonOneStarRatingPercentage,
        AmazonReviews,
        AmazonReviewsCount,
        Genres,
        PageCount
    }
}
