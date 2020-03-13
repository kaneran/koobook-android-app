package controllers;

import android.arch.persistence.room.Room;
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
import extras.Helper;

public class UserController {
    EditText editText_password;
    EditText editText_confirmedPassword;
    SqlServerDatabase ssd;
    AppDatabase db;
    boolean passwordsMatch;
    ColorController colorController;
    BlowfishController blowfishController;
    Helper helper;

    public UserController() {
        colorController = new ColorController();
        blowfishController = new BlowfishController();
        ssd = new SqlServerDatabase();
        helper = new Helper();
    }

    //This method works by getting the EditText elements from the list of EditText elements(passed into the method's arguments) and checks if the text value, entered in each EditText, matches.
    //If it does not match then modify the TextView(passed into the method's arguments) to say the password don't match. It then sets visibility of that TextView to be visible It then uses the Color controller
    // to set the background tint of each EditText element to be red and the methods set the flag to be false. If the password match then this flag will be true and this flag is returned from the method.
    public boolean checkIfPasswordsMatch(ArrayList<EditText> passwords, TextView passwordDoesNotMatchTextView, boolean passwordsValidated) {
        colorController = new ColorController();
        editText_password = passwords.get(0);
        editText_confirmedPassword = passwords.get(1);
        passwordsMatch = true;


        if(passwordsValidated == true){

            //Check if passwords does not match
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


    //Uses the existing hashmap(passed into the method's arguments) to only extract the users details and Returns it as a hashmap where the key will be the index and the value is the
    //value of the user detail (name, email, password). The use of indexing is to maintain the order of the details to be used later. This method works by iterating through each EditText element in the set of EditText
    //from the HashMap(passed into the method's arguments). Switch case was used to get the id of the EditText such that if it matches any of the ids used to hold the user details, then use that along with an index
    //and insert it into the newly initialised hashmap. After iterating through each EditText element then the method Returns this hashmap.
    public HashMap<Integer, String> getUserDetails(HashMap<EditText, TextView> editTextTextViewHashMap) {
        HashMap<Integer, String> userDetailsHashMap = new HashMap<>();
        for (EditText editText : editTextTextViewHashMap.keySet()) {
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

    //Collects all required fields and stores in SQL server database and room database. This method works by using the Blowfish controller to randomly generate an encryption key. This key is then
    //used to encrypt the password. It then inserts a new User record into the Sql server database using the encrypted password along with the user's first name and password(passed into the method's arguments).
    //I then uses recently generated encryption key and the email(used to get the user id from the User table in the SQL server and use that as the foreign key) to insert a new Blowfish record. The method then Returns true
    //to denote that these processes were successful. If an exception was caught during the main execution then it Returns false.
    public boolean createUserAccount(String firstName, String email) {
        try {
            BlowfishController blowfishController = new BlowfishController();
            String key = blowfishController.generateKey();
            String encryptedPassword = blowfishController.encrypt(editText_password.getText().toString(), key);

            insertUserAccountInSqlServerDatabase(firstName, email, encryptedPassword);
            blowfishController.insertBlowfishKeyInSqlServerDatabase(key, email);
            return true;
        } catch (Exception e) {
            return false;

        }
    }



    //This method works by executing an statement to insert a new record,using the data from the method's arguments, into the User table in the Sql server database. If statement was successfully executed
    //then the method will return true. If an exception was thrown during the main execution then it will return false.
    public boolean insertUserAccountInSqlServerDatabase(String firstName, String email, String encryptedPassword) {
        try {
            ssd.executeUpdateStatement("insert into [dbo].[User] (FirstName,Email,EncryptedPassword) values ('" + firstName + "','" + email + "','" + encryptedPassword + "');");
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //Checks if emails does not contain any spacing and has the characters that are usually in emails. This method works by first getting the email string from the EditText element that was passed
    //into the method's argument. It first checks whether the email is empty. If it this then the method immediately Returns false. If it isn't then it will then check to see if the email contains
    //the symbols "@" and "." which are usually in emails. If it does not contains these symbols then the TextView element(passed into the method's arguments) will be modified by making it visible and getting to display the message
    //"Invalid email". Also, the EditText element(passed into the method's arguments) is modified by setting its background tint to red and validation count is decremented. If then checks whether email already exists in the Sql server database.
    //If it does exist in the database then the same process for decrementing the validation count and modifying the TextView and EditText elements will occur except that the message set for the TextView will be "Email already used".
    //It then checks whether the validation count was decremented, if it was not then TextView will be modified by making is invisible and the EditText will be modified by setting the background tint to white to inform
    //the user that there nothing wrong with the email entered. If then sets the flag to equal true and this flag is returned by this method.
    public boolean validateEmail(EditText editText_email, TextView textView_email_error_msg) {
        boolean emailedValidated = false;
        int validationCount = 2;
        String email = editText_email.getText().toString();

        if (helper.checkEditTextFieldNonEmpty(editText_email, textView_email_error_msg, false) == true) {

            if (!email.contains("@") && !email.contains(".")) {
                helper.editMessageProperties(editText_email, textView_email_error_msg, "Invalid email", ColorController.Colors.RED, true);
                validationCount--;
            }
            if (checkIfEmailAlreadyUsed(email) == true) {
                helper.editMessageProperties(editText_email, textView_email_error_msg, "Email already used", ColorController.Colors.RED, true);
                validationCount--;
            }
            //Both validation checks passed
            if (validationCount == 2) {
                helper.editMessageProperties(editText_email, textView_email_error_msg, "", ColorController.Colors.WHITE, false);
                emailedValidated = true;
            }
        }
        return emailedValidated;

    }

    //Checks to see if the email is affilaited with an existing account by using the email(passed into the method's argument) to execute a select query to get the user's first name from  the User table in the SQL server database.
    //If the query returned a name then this implies than the email has already been used and the method Returns true. If the query returned null then the method Returns false
    public boolean checkIfEmailAlreadyUsed(String email) {
        String firstName = ssd.executeSelectStatement("SELECT [dbo].[User].[FirstName] FROM [dbo].[User] WHERE email='" + email + "';", SqlServerDatabase.Returns.String);
        if (firstName != null) {
            return true;
        } else {
            return false;
        }
    }



    //Source- https://stackoverflow.com/questions/43292673/java-how-to-check-if-a-string-contains-a-digit this was used for the first condition in the if statement which checks if the string has any digits.
    //This method works by initialising a validation count. It then gets the value entered into the EditText element(passed into the method's argument) and uses it to check if the value is non empty. If the value is non-empty then it checks
    //if the value contains any digits. If it does then EditText element is modified by setting its background tint color to red and the TextView(passed into the method's argument) is modified by displaying the message "Invalid first name" and makes it invisible.
    //After the modifications, the validation count is decremented. If then checks if the validation count was decremented and if it was then this implies that the previous validation check failed. Therefore, it will do nothing. If
    //the count was not decremented then modify the TextView element by making it invisible and modifying the background tint color of the EditText element to be white.
    public boolean validateFirstName(EditText editText_firstName, TextView textView_firstName_error_msg) {
        String firstName = editText_firstName.getText().toString();
        boolean firstNameValidated = false;
        int validationCount = 1;
        if (helper.checkEditTextFieldNonEmpty(editText_firstName, textView_firstName_error_msg, false) == true) {

            if (firstName.matches(".*\\d+.*")) {
                helper.editMessageProperties(editText_firstName, textView_firstName_error_msg, "Invalid first name", ColorController.Colors.RED, true);
                validationCount--;

            }
            if (validationCount == 1) {
                helper.editMessageProperties(editText_firstName, textView_firstName_error_msg, "", ColorController.Colors.WHITE, false);
                firstNameValidated = true;
            }

        }

        return firstNameValidated;

    }

    //This method works by getting the value from the EditText element(passed into the method's argument) and initialising a validation count. It then uses the value to check whether it is non empty. If
    //If it is non-empty then it checks if the password contains digits. If it does not contain digits then TextView(passed into the method's argument) element is modified by displaying
    // the message "Password must contain at least one number" and the EditText element is modified by setting the background tint colour to be red validation. After the modifications, the count is decremented.
    // If it does contain digits then proceed to check if the length of the password is greater than 8 characters. If it is not then the count will be decremented and the same modification process will occur for the EditText and TextView elements except that the TextView
    //will display the message "Password length must be at least 8 characters long". It then checks if the validation count was decremented, if it was not then this implies that the validation checks passed. Therefore, the TextView will be made invisible
    //and the background tint color of the EditText will be white.
    public boolean validatePassword(EditText editText_password, TextView textView_password_error_msg) {
        String password = editText_password.getText().toString();
        boolean passwordValidated = false;
        int validationCount = 1;
        if (helper.checkEditTextFieldNonEmpty(editText_password, textView_password_error_msg, false) == true) {

            if (password.matches(".*\\d+.*")) {

                if (password.length() <= 8) {
                    helper.editMessageProperties(editText_password, textView_password_error_msg, "Password length must be at least 8 characters long", ColorController.Colors.RED, true);
                    validationCount--;
                }

            } else {
                helper.editMessageProperties(editText_password, textView_password_error_msg, "Password must contain at least one number", ColorController.Colors.RED, true);
                validationCount--;

            }
            if (validationCount == 1) {
                helper.editMessageProperties(editText_password, textView_password_error_msg, "", ColorController.Colors.WHITE, false);
                passwordValidated = true;
            }

        }
        return passwordValidated;
    }

    //If the login method Returns false then it will always set the message to say a vague message for security purposes
    //This method works by getting the getting email and password that were entered in the EditText elements(passed into the method's arguments). It then uses the email to get the userId from the User table in the
    //the SQL server database. It then checks to see if the userId is not null. If it is not null then the Blowfish controller is used to get Blowfish key from the Blowfish table in the SQL server dabatbase. It then
    //uses the userId to get the encrypted password from the User table. It then passes the encrypted password and blowfish key into the decrypt method from the Blowfish controller and this will return the decrypted password which
    //is in plain text form. It then checks to see if the decrypted password matches the password that the user entered. If it matches then the method Returns true. If it doesn't the EditText elementis modified by
    //setting the background tint colour to be red and the TextView is modified by getting it to display the message "Username or password or both were incorrectly entered or the account does not exist. Please check and try again."
    //and making it visible. After the modifications, the method Returns false.  If the userId retrieved was null then the same modification process will occur for the TextView and EditText elements(passed into the method's arguments) and Returns false.
    public boolean login(EditText editText_email, EditText editText_password, TextView textview_login_failed_msg) {
        //Get values from edit text elements
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
                //Make the outline color for both text fields to be red
                helper.editMessageProperties(editText_email, textview_login_failed_msg,
                        "Username or password or both were incorrectly entered or the account does not exist. Please check and try again.",
                        ColorController.Colors.RED, true);
                colorController.setBackgroundTint(editText_password, ColorController.Colors.RED);
                return false;
            }
        } else {
            //Make the outline color for both text fields to be red
            helper.editMessageProperties(editText_email, textview_login_failed_msg,
                    "Username or password or both were incorrectly entered or the account does not exist. Please check and try again.",
                    ColorController.Colors.RED, true);
            colorController.setBackgroundTint(editText_password, ColorController.Colors.RED);
            return false;
        }
    }

    //Uses the userId(passed into the method's argument) to execute a select query to get the encrypted password from the User table in the SQL Server database and Returns it as a string
    public String getEncryptedPasswordFromSqlServerDatabase(String userId) {
        return ssd.executeSelectStatement("select [dbo].[User].[EncryptedPassword] from [dbo].[User] where [dbo].[User].[UserId]= " + userId + "", SqlServerDatabase.Returns.String);

    }

    //Uses the email(passed into the method's argument) to execute a select query to get the user Id from the User table in the SQL Server database and Returns it as a string
    public String getUserIdFromSqlServerDatabase(String email) {
        return ssd.executeSelectStatement("select [dbo].[User].[UserId] from [dbo].[User] where [dbo].[User].[Email]='" + email + "';", SqlServerDatabase.Returns.Int);
    }

    //This method works by storing the user id(passed into the method's argument) in a shared preference file called "UserPref" and Returns true after the main execution completes. If an exception was thrown during the main execution
    //then it Returns false.
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


    //This method works by retrieving the user id from the shared preference file named "UserPref"
    public int getUserIdFromSharedPreferneces(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("userId", 0);
    }

    //This method works by storing the one time password(passed into the method's argument) in a shared preference file called "OneTimePasswordPref" and Returns true after the main execution completes. If an exception was thrown during the main execution
    //then it Returns false.
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

    //This method works by retrieving the user id from the shared preference file named "UserPref"
    public String getOneTimePasswordFromSharedPreferneces(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("OneTimePasswordPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("oneTimePassword", "");
    }

    //This method works by getting the value entered in the EditText element(passed into the method's argument). This is then uses to check if it equals to the actual one time password(passed into the method's argument).
    //If it does not match then modify the EditText by setting it background tint color to red and modifying the TextView element(passed into the method's argument) by getting it display the message
    //"One Time Password (OTP) incorrect" and making it visible. After the modifications, the method Returns false. If the entered one time password matched then the method Returns true.
    public boolean validateOneTimePassword(EditText editText_oneTimePassword,TextView textView_oneTimePassword_error_msg, String oneTimePassword){
        String oneTimePasswordEntered = editText_oneTimePassword.getText().toString();
        if (!oneTimePassword.equals(oneTimePasswordEntered)){
            helper.editMessageProperties(editText_oneTimePassword, textView_oneTimePassword_error_msg, "One Time Password (OTP) incorrect", ColorController.Colors.RED, true);
            return false;
        } else{

            return true;
        }
    }

    //This method randomly genrates a one time password being six characters long and could contain both digits and letters. This generated string is made to upper case and returned.
    public String generateOneTimePassword() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            if (random.nextInt(2) == 1) {
                otp.append(blowfishController.generateRandomDigit());
            } else {
                otp.append(blowfishController.generateRandomLetter());

            }
        }
        return otp.toString().toUpperCase();
    }

    //This method works by using the userId(passed into the method's arguments) to get the blowfish key from the Blowfish table in the SQL server database. The blowfish controller is then used to
    //to encrypt the password(passed into the method's arguments) using the retrieved blowfish key. It then uses the userId and the encrypted password to execute an update statement to modify the
    //the "EncryptedPassword" attribute from the User table in the SQL server database. The outcome of whether the password was updated is set to the boolean flag. If this flag equals to true then the
    //method Returns true. Otherwise, the method will return false.
    public boolean resetPassword(int userId,String newPassword){
        String blowfishKey = blowfishController.getBlowfishKeyFromSqlServerDatabase(Integer.toString(userId));
        String encryptedPassword = blowfishController.encrypt(newPassword, blowfishKey);
        boolean passwordUpdated = updatePasswordInSqlServerDatabase(userId, encryptedPassword);
        if(passwordUpdated == true){
            return true;
        } else{
            return false;
        }
    }

    //This method works by using the userId and password(passed into the method's arguments) are used to execute an update query to the User table which updates the EncryptedPassword field to be the password that passed into this method
    public boolean updatePasswordInSqlServerDatabase(int userId, String newPassword){
        return ssd.executeUpdateStatement("UPDATE [User] SET EncryptedPassword ='"+newPassword+"' WHERE UserId = '"+userId+"'");
    }


}
