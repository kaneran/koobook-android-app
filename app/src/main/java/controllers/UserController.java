package controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dataaccess.setup.AppDatabase;
import dataaccess.sqlserver.SqlServerDatabase;

public class UserController {
    EditText editText_password;
    EditText editText_confirmedPassword;
    HashMap<EditText, TextView> map;
    AppDatabase db;
    SqlServerDatabase ssd;
    boolean passwordsMatch;


    public boolean checkIfUserEnteredInformationInAllFields(HashMap<EditText, TextView> map, TextView passwordDoesNotMatchTextView) {
        ColorController colorController = new ColorController();
        int nonEmptyFieldsCounter = 0;

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
        }
        return nonEmptyFieldsCounter == map.size();

    }

    public boolean checkIfPasswordsMatch(ArrayList<EditText> passwords, TextView passwordDoesNotMatchTextView, boolean passwordsValidated) {
        ColorController colorController = new ColorController();
        editText_password = passwords.get(0);
        editText_confirmedPassword = passwords.get(1);
        passwordsMatch = true;


        if(passwordsValidated == true){

            //Check if passwords match
            if (!editText_password.getText().toString().matches(editText_confirmedPassword.getText().toString())) {
                passwordDoesNotMatchTextView.setText(R.string.passwords_does_not_match);
                passwordDoesNotMatchTextView.setVisibility(View.VISIBLE);
                colorController.setBackgroundTint(editText_confirmedPassword, ColorController.Colors.RED);
                colorController.setBackgroundTint(editText_password, ColorController.Colors.RED);
                passwordsMatch = false;
            }

        }
        return passwordsMatch;

    }


    //Uses the existing hashmap to only extract the users details and returns it as a hashmap where the key will be the index
    //to maintain the order of the details to be used later.
    public HashMap<Integer, String> getUserDetails(HashMap<EditText, TextView> map) {
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
    public boolean createUserAccount(String firstName, String email) {
        try {
            BlowfishController blowfishController = new BlowfishController();
            String key = blowfishController.generateKey();
            String encryptedPassword = blowfishController.encrypt(editText_password.getText().toString(), key);
            SqlServerDatabase ssd = new SqlServerDatabase();
            insertUserAccountInSqlDatabase(firstName, email, encryptedPassword);
            blowfishController.insertBlowfishKeyInSqlServerDatabase(key, email);
            return true;
        } catch (Exception e) {
            return false;

        }
    }

    public boolean insertUserAccountInSqlDatabase(String firstName, String email, String encryptedPassword) {
        SqlServerDatabase ssd = new SqlServerDatabase();
        try {
            ssd.executeUpdateStatement("insert into [dbo].[User] (FirstName,Email,EncryptedPassword) values ('" + firstName + "','" + email + "','" + encryptedPassword + "');");
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
                    } else {

                    }

                case R.id.edittext_password:
                    if (validatePassword(editText, map.get(editText)) == true) {
                        validationPassedCounter++;
                    } else {

                    }

                case R.id.edittext_confirmpassword:
                    if (validatePassword(editText, map.get(editText)) == true) {
                        validationPassedCounter++;
                    } else {

                    }
            }

        }
        return validationPassedCounter == map.size();
    }

    //Checks if emails does not contain any spacing and has the characters that are usually in emails
    public boolean validateEmail(EditText editText_email, TextView textView_email_errorMsg) {
        boolean emailedValidated = false;
        int validationCount = 2;
        String email = editText_email.getText().toString();

        if (checkEditTextFieldNonEmpty(editText_email, textView_email_errorMsg, false) == true) {

            if (!email.contains("@") || !email.contains(".")) {
                editMessageProperties(editText_email, textView_email_errorMsg, "Invalid email", ColorController.Colors.RED, true);
                validationCount--;
            }
            if (checkIfEmailAlreadyUsed(email) == true) {
                editMessageProperties(editText_email, textView_email_errorMsg, "Email already used", ColorController.Colors.RED, true);
                validationCount--;
            }
            //Both validation checks passed
            if (validationCount == 2) {
                editMessageProperties(editText_email, textView_email_errorMsg, "", ColorController.Colors.WHITE, false);
                emailedValidated = true;
            }
        }
        return emailedValidated;

    }

    //Checks to see if the email is affilaited with an existing account
    public boolean checkIfEmailAlreadyUsed(String email) {
        ssd = new SqlServerDatabase();
        String firstName = ssd.executeSelectStatement("SELECT [dbo].[User].[FirstName] FROM [dbo].[User] WHERE email='" + email + "';", SqlServerDatabase.returns.String);
        if (firstName != null) {
            return true;
        } else {
            return false;
        }
    }

    //Method used to edit the prpoperities of a message textview such as visibility and its text value
    public void editMessageProperties(EditText editText, TextView textView, String message, ColorController.Colors color, boolean isVisible) {
        ColorController colorController = new ColorController();

        //Access the error msg that coresponds to the edit text  and set its text value
        textView.setText(message);
        colorController.setBackgroundTint(editText, color);
        if (isVisible == false) {
            textView.setVisibility(View.INVISIBLE);

        } else {
            textView.setVisibility(View.VISIBLE);
        }


    }


    //The boolean value was added as an arugment to check if the activity is the Login acitivty
    //if it is then I don't want the textview error message to be the default message
    public boolean checkEditTextFieldNonEmpty(EditText editText, TextView textView_errorMsg, boolean isLoginActivity) {
        String editText_value = editText.getText().toString();
        ColorController colorController = new ColorController();
        if (editText_value.matches("")) {
            colorController.setBackgroundTint(editText, ColorController.Colors.RED);

            if (isLoginActivity == true) {
                textView_errorMsg.setText("Email or password or both are empty");
            } else {
                textView_errorMsg.setText(R.string.this_field_is_required);
            }
            textView_errorMsg.setVisibility(View.VISIBLE);

            return false;
        } else {
            colorController.setBackgroundTint(editText, ColorController.Colors.WHITE);
            textView_errorMsg.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    //Source- https://stackoverflow.com/questions/43292673/java-how-to-check-if-a-string-contains-a-digit this was used for
    //the first condition in the if statement which checks if the string has any digits.
    public boolean validateFirstName(EditText editText_firstName, TextView textView_firstName_errorMsg) {
        String firstName = editText_firstName.getText().toString();
        boolean firstNameValidated = false;
        int validationCount = 1;
        if (checkEditTextFieldNonEmpty(editText_firstName, textView_firstName_errorMsg, false) == true) {

            if (firstName.matches(".*\\d+.*")) {
                editMessageProperties(editText_firstName, textView_firstName_errorMsg, "Invalid first name", ColorController.Colors.RED, true);
                validationCount--;

            }
            if (validationCount == 1) {
                editMessageProperties(editText_firstName, textView_firstName_errorMsg, "", ColorController.Colors.WHITE, false);
                firstNameValidated = true;
            }

        }

        return firstNameValidated;

    }

    //First checks if password contains any spacing and then checks if it contains digits
    public boolean validatePassword(EditText editText_password, TextView textView_password_errorMsg) {
        String password = editText_password.getText().toString();
        boolean passwordValidated = false;
        int validationCount = 1;
        if (checkEditTextFieldNonEmpty(editText_password, textView_password_errorMsg, false) == true) {

            if (password.matches(".*\\d+.*")) {

                if (password.length() <= 8) {
                    editMessageProperties(editText_password, textView_password_errorMsg, "Password length must be at least 8 characters long", ColorController.Colors.RED, true);
                    validationCount--;
                }

            } else {
                editMessageProperties(editText_password, textView_password_errorMsg, "Password must contain at least one number", ColorController.Colors.RED, true);
                validationCount--;

            }
            if (validationCount == 1) {
                editMessageProperties(editText_password, textView_password_errorMsg, "", ColorController.Colors.WHITE, false);
                passwordValidated = true;
            }

        }
        return passwordValidated;
    }

    //If the login method returns false then it will always set the message to say a vague message for security purposes
    public boolean login(EditText editText_email, EditText editText_password, TextView textview_login_failed_msg) {
        SqlServerDatabase ssd = new SqlServerDatabase();
        BlowfishController blowfishController = new BlowfishController();
        ColorController colorController = new ColorController();
        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();
        String userId = getUserIdFromSqlServerDatabase(email);

        //Checks if the account, with email, exists
        if (userId != null) {
            String blowfishKey = blowfishController.getBlowfishKeyFromSqlServerDatabase(userId);
            String encryptedPassword = getEncryptedPasswordFromSqlServerDatabase(userId);
            String decryptedPassword = blowfishController.decrypt(encryptedPassword, blowfishKey);


            if (decryptedPassword.equals(password)) {

                return true;

            } else {
                editMessageProperties(editText_email, textview_login_failed_msg,
                        "Username or password or both were incorrectly entered or the account does not exist. Please check and try again.",
                        ColorController.Colors.RED, true);
                colorController.setBackgroundTint(editText_password, ColorController.Colors.RED);
                return false;
            }
        } else {
            editMessageProperties(editText_email, textview_login_failed_msg,
                    "Username or password or both were incorrectly entered or the account does not exist. Please check and try again.",
                    ColorController.Colors.RED, true);
            colorController.setBackgroundTint(editText_password, ColorController.Colors.RED);
            return false;
        }
    }

    public String getEncryptedPasswordFromSqlServerDatabase(String userId) {
        SqlServerDatabase ssd = new SqlServerDatabase();
        return ssd.executeSelectStatement("select [dbo].[User].[EncryptedPassword] from [dbo].[User] where [dbo].[User].[UserId]= " + userId + "", SqlServerDatabase.returns.String);

    }

    public String getUserIdFromSqlServerDatabase(String email) {
        SqlServerDatabase ssd = new SqlServerDatabase();
        return ssd.executeSelectStatement("select [dbo].[User].[UserId] from [dbo].[User] where [dbo].[User].[Email]='" + email + "';", SqlServerDatabase.returns.Int);


    }

    //Store the user id in a shared preference file
    public boolean storeUserId(Context context, int userId) {
        try {
            SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("UserPref", Context.MODE_PRIVATE).edit();
            editor.putInt("userId", userId).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }


    //Retrieve the user id from the shared preference file
    public int getUserIdFromSharedPreferneces(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("userId", 0);
    }

    //Store the one time password(OTP) in a shared preference file
    public boolean storeOneTimePassword(Context context, String oneTimePassword){
        try{
            SharedPreferences.Editor editor = context.getSharedPreferences("OneTimePasswordPref", Context.MODE_PRIVATE).edit();
            editor.putString("oneTimePassword", oneTimePassword). apply();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //Retrieve the one time password from the shared preference file
    public String getOneTimePasswordFromSharedPreferneces(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("OneTimePasswordPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("oneTimePassword", "");
    }

    public boolean validateOneTimePassword(EditText editText_oneTimePassword,TextView textView_oneTimePassword_error_msg, String oneTimePassword){
        String oneTimePasswordEntered = editText_oneTimePassword.getText().toString();
        if (!oneTimePassword.equals(oneTimePasswordEntered)){
            editMessageProperties(editText_oneTimePassword, textView_oneTimePassword_error_msg, "One Time Password (OTP) incorrect", ColorController.Colors.RED, true);
            return false;
        } else{

            return true;
        }
    }

    public String generateOneTimePassword() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        BlowfishController blowfishController = new BlowfishController();
        for (int i = 0; i < 6; i++) {
            if (random.nextInt(2) == 1) {
                otp.append(blowfishController.generateRandomDigit());
            } else {
                otp.append(blowfishController.generateRandomLetter());

            }
        }
        return otp.toString().toUpperCase();
    }

    public boolean resetPassword(int userId,String newPassword){
        BlowfishController blowfishController = new BlowfishController();
        String blowfishKey = blowfishController.getBlowfishKeyFromSqlServerDatabase(Integer.toString(userId));
        String encryptedPassword = blowfishController.encrypt(newPassword, blowfishKey);
        boolean passwordUpdated = updatePassword(userId, encryptedPassword);

        if(passwordUpdated == true){
            return true;
        } else{
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword){
        SqlServerDatabase ssd = new SqlServerDatabase();
        return ssd.executeUpdateStatement("UPDATE [User] SET EncryptedPassword ='"+newPassword+"' WHERE UserId = '"+userId+"'");
    }


}
