package activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import controllers.UserController;
import dataaccess.setup.AppDatabase;
import entities.User;
import extras.Helper;

public class LoginActivity extends AppCompatActivity {
    EditText edittext_email;
    EditText edittext_password;
    TextView textview_login_failed_msg;
    boolean emailNonEmpty;
    boolean passwordNonEmpty;
    boolean loginSuccessful;
    boolean userIdStored;
    AppDatabase db;
    int userId;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edittext_email = findViewById(R.id.edittext_email);
        edittext_password = findViewById(R.id.edittext_password);
        textview_login_failed_msg = findViewById(R.id.textview_login_failed_msg);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
    }

    public void signUpButtonClicked(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    public void forgotPasswordLinkClicked(View v){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    public void loginButtonClicked(View v){
        UserController userController = new UserController();
        Helper helper = new Helper();

        //Validate the entered email and password
        emailNonEmpty = helper.checkEditTextFieldNonEmpty(edittext_email, textview_login_failed_msg, true);
        passwordNonEmpty = helper.checkEditTextFieldNonEmpty(edittext_password, textview_login_failed_msg, true);

        if(emailNonEmpty && passwordNonEmpty == true){
            loginSuccessful = userController.login(edittext_email, edittext_password, textview_login_failed_msg);

            //Generate notification and navigate to the main activity if the login process was successful
            //Store the userId for future use i.e Getting the liked books based on that userId
            if(loginSuccessful == true){
                user = db.userDao().getUser(edittext_email.getText().toString());
                userId = db.userDao().getUserId(edittext_email.getText().toString());
                userIdStored = userController.storeUserId(getApplicationContext(),userId);
                if(userIdStored == true){
                    Intent intent = new Intent(this, SplashActivity.class);
                    //Ensure that the current acitivty is disposed such that the user cannot navigate back to this activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }

        }
    }
}
