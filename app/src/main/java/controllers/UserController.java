package controllers;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import dataaccess.setup.AppDatabase;
import dataaccess.sqlserver.SqlServerDatabase;
import entities.User;

public class UserController {
    EditText editText_password;
    EditText editText_confirmedPassword;
    HashMap<EditText, TextView> map;
    AppDatabase db;

    public void setMap(HashMap<EditText, TextView> map) {
        this.map = map;
    }


    public void checkIfUserEnteredInformationInAllFields(HashMap<EditText, TextView> map, TextView passwordDoesNotMatchTextView) {
        ColorController colorController = new ColorController();
        setMap(map);
        for (EditText editText : map.keySet()) {
            TextView fieldRequiredTextView = map.get(editText);

            //Checks if the user entrted something in the Edittext element
            if (editText.getText().toString().matches("")) {


                //If the text view is for "confirmed password" then I must override its string value
                fieldRequiredTextView.setText(R.string.this_field_is_required);
                fieldRequiredTextView.setVisibility(View.VISIBLE);
                colorController.setBackgroundTint(editText, "#D81B60");

            } else {
                //Hide the error message affiliated with the edittext
                colorController.setBackgroundTint(editText, "#FFFFFF");
                fieldRequiredTextView.setVisibility(View.INVISIBLE);

                //Did this to ensure that the overwritten message for the text view was correct
                if (fieldRequiredTextView.equals(passwordDoesNotMatchTextView)) {
                    fieldRequiredTextView.setText(R.string.passwords_does_not_match);

                }

            }
        }

    }

    public boolean checkIfPasswordsMatch(ArrayList<EditText> passwords, TextView passwordDoesNotMatchTextView) {
        ColorController colorController = new ColorController();

        //Before comparing if the passwords match, this checks if both password fields were populated with values
        if (passwordDoesNotMatchTextView.getText().toString().matches("This field is required")) {
            return false;
        } else {

            editText_password = passwords.get(0);
            editText_confirmedPassword = passwords.get(1);

            //If password does not match then outline both edittext in red and display the error message to say password does not match
            if (!editText_password.getText().toString().matches(editText_confirmedPassword.getText().toString())) {
                passwordDoesNotMatchTextView.setText(R.string.passwords_does_not_match);
                passwordDoesNotMatchTextView.setVisibility(View.VISIBLE);
                colorController.setBackgroundTint(editText_confirmedPassword, "#D81B60");
                colorController.setBackgroundTint(editText_password, "#D81B60");
                return false;
            } else {

                //Given the password matches and all the other fields were populated with value,
                //a user will be created using the user's details

                return true;
            }

        }

    }

    //Uses the existing hashmap to only extract the users details and returns it as a hashmap where the key will be the index
    //to maintain the order of the details to be used later.
    public HashMap<Integer, String> getUserDetails() {
        HashMap<Integer,String> userDetailsHashMap = new HashMap<>();
        for (EditText editText : map.keySet()) {
            switch (editText.getId()){
                case R.id.edittext_firstname:
                    userDetailsHashMap.put(0,editText.getText().toString());

                case R.id.edittext_email:
                    userDetailsHashMap.put(1,editText.getText().toString());

                case R.id.edittext_password:
                    userDetailsHashMap.put(2,editText.getText().toString());

            }

        }
        return userDetailsHashMap;
    }

    //Collects all required fields and stores in SQL server database and room database
    public boolean createUserAccount(HashMap<Integer,String> userDetails) {
        try {
            BlowfishController blowfishController = new BlowfishController();
            String key = blowfishController.generateKey();
            String encryptedPassword = blowfishController.encrypt(editText_password.getText().toString(), key);
            SqlServerDatabase ssd = new SqlServerDatabase();
            ssd.insertUserAccount(userDetails.get(0), userDetails.get(1), encryptedPassword);
            ssd.insertBlowfishKey(key, userDetails.get(1));
            return true;
        } catch(Exception e){
            return false;

        }
    }


}
