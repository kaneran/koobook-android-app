package controllers;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataaccess.setup.AppDatabase;
import entities.Book;
import entities.Review;
import extras.Helper;

//Credit to https://github.com/atripa5/Sentiment-Analysis-in-Java/ for the solution on ranking the reviews based on positive and negative words per review
public class ReviewsController extends AsyncTask<String, Void, Boolean>  {
    Helper helper;
    AppDatabase db;
    BookController bookController;
    UserController userController;
    Context context;
    ProgressDialog progressDialog;
    List<String> positiveReviews =null;

  public List<String> getPositiveReviewsList(){
      return positiveReviews;
  }

    public ReviewsController(Context context)
    {
        this.context = context;
        helper = new Helper();
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        bookController = new BookController(context);
        userController = new UserController();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Fetching positive reviews", "Please wait", false, false);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean reviewsCollected = getPositiveReviews();
        return reviewsCollected;
    }

    public boolean getPositiveReviews(){
        byte[] messageByte = new byte[1000];
        boolean end = false;
        String receivedDataString = "";
        String fromReceiver = "";
        String data = getReviewsBasedOnBooksLikedByUser();
        try{
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("192.168.1.252",9877);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());


            //Send data(reviews) to the server
            outToServer.writeBytes(data );

            //Receive the book data from the server
            while(!end){
                int bytesRead = in.read(messageByte);
                receivedDataString += new String(messageByte, 0, bytesRead);
                if(receivedDataString.contains("]d2C>^+")){
                    end = true;
                }

            }

            receivedDataString = receivedDataString.replace("]d2C>^+","");
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

            positiveReviews = decodeReviewsDataString(receivedDataString);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            positiveReviews = new ArrayList<>();
            return false;
        }

    }

    //This will return a single string that contains all the reviews that satisfy the mininum length
    //All strings are separated by a "$" symbol and after concatanating all the reviews, I will append a "#" symbol to tell the TCP server that this is the end of the string
    //such that it can close the network stream
    public String getReviewsBasedOnBooksLikedByUser(){
        StringBuilder sb = new StringBuilder();
        int userId = userController.getUserIdFromSharedPreferneces(context);
        List<Book> books = bookController.getBooksUsingStatus(userId, db, BookController.BookStatus.Liked,null);
        for(Book book: books){
            List<Review> rviews = db.reviewDao().getReviews(book.getBookId());
            for(Review rview: rviews){
                //Check if review contains more than 3 words
                if(rview.getReview().split(" ").length >3 && rview.getReview().split(" ").length <7) {
                    sb.append(rview.getReview() + "$");
                }
            }
        }
        sb.append("#");
        return sb.toString();
    }

    public List<String> decodeReviewsDataString(String reviewsDataString){

        String[] reviews = reviewsDataString.replace("$","#").split("#");
        List<String> revs = new ArrayList<>();
        if(reviews.length >3){
            revs.add(reviews[0]);
            revs.add(reviews[1]);
            revs.add(reviews[2]);
            revs.add(reviews[3]);
            revs.add(reviews[4]);
        } else{
            revs.addAll(Arrays.asList(reviews));
        }
        return revs;
    }
}
