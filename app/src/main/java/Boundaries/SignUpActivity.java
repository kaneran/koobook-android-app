package Boundaries;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import User.UserController;

public class SignUpActivity extends AppCompatActivity {
    TextView firstNamerequiredTextView;
    EditText firstNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstNamerequiredTextView = findViewById(R.id.firstNameRequired);
        firstNameEditText = findViewById(R.id.firstNameEditText);
    }

    public void signUpButtonClicked(View v){

        UserController userController = new UserController();
        userController.checkIfUserEnteredInformationInAllFields(firstNamerequiredTextView,firstNameEditText);
    }
}
