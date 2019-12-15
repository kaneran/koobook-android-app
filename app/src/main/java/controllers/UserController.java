package controllers;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import dataaccess.setup.AppDatabase;
import dataaccess.sqlserver.SqlServerDatabase;
import entities.Color;
import entities.User;

public class UserController {
    EditText editText_password;
    EditText editText_confirmedPassword;
    HashMap<EditText, TextView> map;
    AppDatabase db;

    public void setMap(HashMap<EditText, TextView> map) {
        this.map = map;
    }


    public boolean checkIfUserEnteredInformationInAllFields(HashMap<EditText, TextView> map, TextView passwordDoesNotMatchTextView) {
        ColorController colorController = new ColorController();
        int nonEmptyFieldsCounter = 0;
        setMap(map);
        for (EditText editText : map.keySet()) {
            TextView fieldRequiredTextView = map.get(editText);

            //Checks if the user entered something in the Edittext element
            if (editText.getText().toString().matches("")) {


                //If the text view is for "confirmed password" then I must override its string value
                fieldRequiredTextView.setText(R.string.this_field_is_required);
                fieldRequiredTextView.setVisibility(View.VISIBLE);
                colorController.setBackgroundTint(editText, ColorController.Colors.RED);

            } else {
                //Hide the error message affiliated with the edittext
                colorController.setBackgroundTint(editText, ColorController.Colors.WHITE);
                fieldRequiredTextView.setVisibility(View.INVISIBLE);
                nonEmptyFieldsCounter++;

                //Did this to ensure that the overwritten message for the text view was correct
                //which helps to validate the prechecks in the checkIfPasswordsMatch method
                if (fieldRequiredTextView.equals(passwordDoesNotMatchTextView)) {
                    fieldRequiredTextView.setText(R.string.passwords_does_not_match);

                }

            }
        } return nonEmptyFieldsCounter == map.size();

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
                colorController.setBackgroundTint(editText_confirmedPassword, ColorController.Colors.RED);
                colorController.setBackgroundTint(editText_password, ColorController.Colors.RED);
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
        HashMap<Integer, String> userDetailsHashMap = new HashMap<>();
        for (EditText editText : map.keySet()) {
            switch (editText.getId()) {
                case R.id.edittext_firstname:
                    userDetailsHashMap.put(0, editText.getText().toString());

                case R.id.edittext_email:
                    userDetailsHashMap.put(1, editText.getText().toString());

                case R.id.edittext_password:
                    userDetailsHashMap.put(2, editText.getText().toString());

            }

        }
        return userDetailsHashMap;
    }

    //Collects all required fields and stores in SQL server database and room database
    public boolean createUserAccount(HashMap<Integer, String> userDetails) {
        try {
            BlowfishController blowfishController = new BlowfishController();
            String key = blowfishController.generateKey();
            String encryptedPassword = blowfishController.encrypt(editText_password.getText().toString(), key);
            SqlServerDatabase ssd = new SqlServerDatabase();
            ssd.insertUserAccount(userDetails.get(0), userDetails.get(1), encryptedPassword);
            ssd.insertBlowfishKey(key, userDetails.get(1));
            return true;
        } catch (Exception e) {
            return false;

        }
    }

    //Checks to validate each edit text field and increments a counter to
    //keep track on how many fields have been validated. If the the counter
    //if the counter is equal to the number of text fields then this implies
    //that all fileds have been validated.
    public boolean validateUserDetails(HashMap<EditText, TextView> map) {

        int validationPassedCounter = 0;
        for (EditText editText : map.keySet()) {
            switch (editText.getId()) {
                case R.id.edittext_email:
                    if (validateEmail(editText, map.get(editText)) == true) {
                        validationPassedCounter++;
                    } else {

                    }
                case R.id.edittext_firstname:
                    if (validateFirstName(editText, map.get(editText)) == true) {
                        validationPassedCounter++;
                    } else{

                    }

                case R.id.edittext_password:
                    if (validatePassword(editText, map.get(editText)) == true) {
                        validationPassedCounter++;
                    } else{

                    }

                case R.id.edittext_confirmpassword:
                    if (validatePassword(editText, map.get(editText)) == true) {
                        validationPassedCounter++;
                    } else{

                    }
            }

        }
        return validationPassedCounter == map.size();
    }

    //Checks if emails does not contain any spacing and has the characters that are usually in emails
    public boolean validateEmail(EditText editText_email, TextView textView_email_errorMsg) {
        String email = editText_email.getText().toString();
        ColorController colorController = new ColorController();
        if (email.contains(" ") || !email.contains("@") || !email.contains(".")) {
            colorController.setBackgroundTint(editText_email, ColorController.Colors.RED);

            //Access the error msg that coresponds to the edit text for email and set its text value

            textView_email_errorMsg.setText("Invalid email");
            textView_email_errorMsg.setVisibility(View.VISIBLE);
            return false;
        } else {
            textView_email_errorMsg.setText("");
            textView_email_errorMsg.setVisibility(View.INVISIBLE);
            colorController.setBackgroundTint(editText_email, ColorController.Colors.WHITE);
            return true;
        }

    }

    //Source- https://stackoverflow.com/questions/43292673/java-how-to-check-if-a-string-contains-a-digit this was used for
    //the first condition in the if statement which checks if the string has any digits.
    public boolean validateFirstName(EditText editText_firstName, TextView textView_firstName_errorMsg) {
        String firstName = editText_firstName.getText().toString();
        ColorController colorController = new ColorController();
        if (firstName.contains(".*\\d+.*")) {
            colorController.setBackgroundTint(editText_firstName, ColorController.Colors.RED);

            //Access the error msg that coresponds to the edit text for first name and set its text value

            textView_firstName_errorMsg.setText("Invalid first name");
            textView_firstName_errorMsg.setVisibility(View.VISIBLE);
            return false;


        } else {
            textView_firstName_errorMsg.setVisibility(View.INVISIBLE);
            colorController.setBackgroundTint(editText_firstName, ColorController.Colors.WHITE);
            return true;
        }

    }

    //First checks if password contains any spacing and then checks if it contains digits
    public boolean validatePassword(EditText editText_password, TextView textView_password_errorMsg) {
        String password = editText_password.getText().toString();
        ColorController colorController = new ColorController();
        if (password.contains(" ")) {
            colorController.setBackgroundTint(editText_password, ColorController.Colors.RED);

            //Access the error msg that coresponds to the edit text for password and set its text value

            textView_password_errorMsg.setText("Password invalid");
            textView_password_errorMsg.setVisibility(View.VISIBLE);
            return false;
        } else if (!password.contains(".*\\d+.*")) {
            colorController.setBackgroundTint(editText_password, ColorController.Colors.RED);

            //Access the error msg that coresponds to the edit text for password and set its text value
            textView_password_errorMsg.setText("Password must contains at least one number");
            textView_password_errorMsg.setVisibility(View.VISIBLE);
            return false;
        } else {
            textView_password_errorMsg.setVisibility(View.INVISIBLE);
            colorController.setBackgroundTint(editText_password, ColorController.Colors.WHITE);
            return true;
        }
    }

    //Checks to see if all validation checks return true before proceeding with storing the user details in the databases
    public boolean validateSignUpDetails(HashMap<EditText,TextView> map, ArrayList<EditText> passwords, TextView textView_passwords_error_msg){

        if(!checkIfUserEnteredInformationInAllFields(map, textView_passwords_error_msg) || !validateUserDetails(map)|| !checkIfPasswordsMatch(passwords, textView_passwords_error_msg)){
            return false;
        } else{
            return true;
        }
    }


}
