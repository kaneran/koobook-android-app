package controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeatureMatchingController extends AsyncTask<String, Void, Boolean> {
    Context context;
    ProgressDialog progressDialog;
    List<String> thumbnailUrls;

    public FeatureMatchingController(Context context) {
        this.context = context;
    }




    public List<String> getTopMatchingThumbnailUrlsList(){
        return thumbnailUrls;
    }

    //Displays progress dialog while the execution of the Async task commences to inform the user that they need to wait
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Matching your capture with book covers", "Please wait", false, false);
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
            boolean urlsCollected = getThumbnailUrlsThatBestMatchesCapturedImage();
            return urlsCollected;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    //Store the data string, containing all the brief information about the 10 books, in a shared preference file
    public boolean storeUriDataString(Context context, String uri) {
        try {
            SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("UriDataPref", Context.MODE_PRIVATE).edit();
            editor.putString("uriData", uri).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Retrieve the data string, containing all the brief information about the 10 books,  from the shared preference file
    public String getUriDataStringFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("UriDataPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("uriData", "");
    }


    public boolean getThumbnailUrlsThatBestMatchesCapturedImage(){

        byte[] messageByte = new byte[1000];
        boolean end = false;
        String receivedDataString = "";
        String fromReceiver = "";
        String data = getUriDataStringFromSharedPreferences(context);
        try{
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("192.168.1.252",9879);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //Send data(reviews) to the server
            outToServer.writeBytes(data + "#");

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
            thumbnailUrls = decodeThumbnailUrlsDataString(receivedDataString);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            String message = e.getMessage();
            thumbnailUrls = new ArrayList<>();
            return false;
        }


    }

    public List<String> decodeThumbnailUrlsDataString(String thumbnailUrlsDataString){

        String[] thumbnailUrls = thumbnailUrlsDataString.split("\\#");
        List<String> thumbnailUrlList = new ArrayList<>();
        thumbnailUrlList.addAll(Arrays.asList(thumbnailUrls));
        return thumbnailUrlList;
    }

}
