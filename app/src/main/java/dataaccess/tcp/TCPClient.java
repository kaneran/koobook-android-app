package dataaccess.tcp;

import android.os.AsyncTask;

public class TCPClient extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        try{
            connect();
        }catch(Exception e){
            return false;
        }
    }

    public void connect(){

    }
}
