package activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.UserController;

public class SignUpActivity extends AppCompatActivity {
    TextView firstNameRequiredTextView;
    TextView emailRequiredTextView;
    TextView passwordRequiredTextView;
    TextView passwordDoesNotMatchTextView;
    EditText firstNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    ArrayList<EditText> passwords;
    HashMap<EditText,TextView> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        map = new HashMap<>();
        firstNameRequiredTextView = findViewById(R.id.firstNameRequiredTextView);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        map.put(firstNameEditText, firstNameRequiredTextView);


        emailRequiredTextView = findViewById(R.id.emailRequiredTextView);
        emailEditText = findViewById(R.id.emailEditText);
        map.put(emailEditText, emailRequiredTextView);

        passwordRequiredTextView = findViewById(R.id.passwordsRequiredTextView);
        passwordEditText = findViewById(R.id.passwordEditText);
        map.put(passwordEditText, passwordRequiredTextView);


        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        passwordDoesNotMatchTextView = findViewById(R.id.passwordsDoesNotMatchTextView);

        //I added the "Password does not match text view to the hashmap because I plan to access it
        // and modify the string dpeending on whether the user entered mismatching passwords
        // or didn't enter anything at all."
        map.put(confirmPasswordEditText, passwordDoesNotMatchTextView);


        passwords = new ArrayList<>();
        passwords.add(passwordEditText);
        passwords.add(confirmPasswordEditText);
    }

    public void signUpButtonClicked(View v){

        UserController userController = new UserController();
        userController.checkIfUserEnteredInformationInAllFields(map, passwordDoesNotMatchTextView);
        userController.checkIfPasswordsMatch(passwords, passwordDoesNotMatchTextView);
    }
}
