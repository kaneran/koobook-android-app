package activities;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controllers.UserController;
import dataaccess.setup.AppDatabase;
import entities.User;

public class SignUpActivity extends AppCompatActivity {
    TextView textView_firstName_error_msg;
    TextView textView_email_error_msg;
    TextView textView_password_error_msg;
    TextView textView_passwords_error_msg;
    EditText editText_firstName;
    EditText editText_email;
    EditText editText_password;
    EditText editText_confirmPassword;
    ArrayList<EditText> passwords;
    HashMap<EditText,TextView> map;
    Boolean doPasswordsMatch;
    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();

        map = new HashMap<>();



        textView_firstName_error_msg = findViewById(R.id.textview_firstname_error_msg);
        editText_firstName = findViewById(R.id.edittext_firstname);
        map.put(editText_firstName, textView_firstName_error_msg);

        textView_email_error_msg = findViewById(R.id.textview_email_error_msg);
        editText_email = findViewById(R.id.edittext_email);
        map.put(editText_email, textView_email_error_msg);


        textView_password_error_msg = findViewById(R.id.textview_password_error_msg);
        editText_password = findViewById(R.id.edittext_password);
        map.put(editText_password, textView_password_error_msg);


        editText_confirmPassword = findViewById(R.id.edittext_confirmpassword);
        textView_passwords_error_msg = findViewById(R.id.textview_passwords_error_msg);

        //I added the "Password does not match text view to the hashmap because I plan to access it
        // and modify the string depending on whether the user entered mismatching passwords
        // or didn't enter anything at all."
        map.put(editText_confirmPassword, textView_passwords_error_msg);

        passwords = new ArrayList<>();
        passwords.add(editText_password);
        passwords.add(editText_confirmPassword);


    }

    public void signUpButtonClicked(View v){

        UserController userController = new UserController();
        userController.checkIfUserEnteredInformationInAllFields(map, textView_passwords_error_msg);
        doPasswordsMatch= userController.checkIfPasswordsMatch(passwords, textView_passwords_error_msg);
        HashMap<Integer,String> userDetails = userController.getUserDetails();
        boolean accountCreated = userController.createUserAccount(userDetails);

        db.userDao().insertUserAccont(new User(0,userDetails.get(1), userDetails.get(0)));
        List<User> usr = db.userDao().test("Kane");
        }
    }
