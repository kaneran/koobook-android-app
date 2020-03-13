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
    List<String> positiveReviews;

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
        positiveReviews = null;
    }

    //Displays progress dialog while the execution of the Async task commences to inform the user that they need to wait
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Fetching positive reviews", "Please wait", false, false);
    }

    //After the Async task executes, the progress dialog closes
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
    }

    //The execution of the Async task involves retrieving the positive reviews from the C# server console application
    @Override
    protected Boolean doInBackground(String... strings) {
      try {
          boolean reviewsCollected = getPositiveReviews();
          return reviewsCollected;
      } catch(Exception e){
          e.printStackTrace();
          return false;
      }
    }

    //This method works by collecting all the reviews of the books liked by the user. All the reviews  were all concatanted as a string whilst using the "$" to separate each review.
    //It also appended the "#" to the end of this string to tell the server that they have read the entire string and no longer need to wait. It then connects to the C# server console application (Book review manager)
    // via a TCP socket. After connecting,it sends this string and the server will perform text sentiment analysis to distinguish between the positive and negative reviews. It server then sends the positive reviews to the client(Android app).
    //In addition, the server also sends an arbitrary string "]d2C>^+" to inform the client that it was received all the data from the server. After the client receives all the data, it removes the sub string "]d2C>^+" and proceeds
    //to do te partial handshake with the server to close connection. After closing connection, it extracts(decodes) the positive reviews from the retrieved data string and Returns true. If an exception occurs
    //during the method's main execution then simply return false and initialise the list of positve reviews to be an empty list to avoid null exceptions.
    public boolean getPositiveReviews(){
        byte[] messageByte = new byte[1000];
        boolean end = false;
        String receivedDataString = "";
        String fromReceiver = "";
        String data = getReviewsBasedOnBooksLikedByUser();
        try{
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("192.168.1.252",9875);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());


            //Send data(reviews) to the server
            outToServer.writeBytes(data);

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
            String message = e.getMessage();
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
        List<Book> books = bookController.getBooksUsingStatus(userId,BookController.BookStatus.Liked,null);
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

    //This method works by replacing the "$"s with "#" within the string that was passed into the method's argument. It then splits the string by using the "#" which ulimately Returns a string list
    //contains the individual reviews. If there are more than 3 reviews then add the first 5 reviews to the newly initialised list of reviews. Otherwise, add all the reviews to the list. The method then
    //Returns this list of reviews
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
