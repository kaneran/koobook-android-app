package activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koobookandroidapp.MainActivity;
import com.example.koobookandroidapp.R;

import controllers.UserController;

public class LoginActivity extends AppCompatActivity {
    EditText edittext_email;
    EditText edittext_password;
    TextView textview_login_failed_msg;
    boolean emailEmpty;
    boolean passwordEmpty;
    boolean loginSuccessful;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edittext_email = findViewById(R.id.edittext_email);
        edittext_password = findViewById(R.id.edittext_password);
        textview_login_failed_msg = findViewById(R.id.textview_login_failed_msg);
    }

    public void signUpButtonClicked(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }


    public void loginButtonClicked(View v){
        UserController userController = new UserController();
        emailEmpty = userController.checkEditTextFieldNonEmpty(edittext_email, textview_login_failed_msg);
        passwordEmpty = userController.checkEditTextFieldNonEmpty(edittext_password, textview_login_failed_msg);

        if(emailEmpty && passwordEmpty == true){
            loginSuccessful = userController.login(edittext_email, edittext_password, textview_login_failed_msg);

            //Generate notification and navigate to the main activity
            if(loginSuccessful == true){
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

        }
    }
}
