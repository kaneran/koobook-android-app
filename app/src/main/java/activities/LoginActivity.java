package activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import controllers.UserController;

public class LoginActivity extends AppCompatActivity {
    EditText edittext_email;
    EditText edittext_password;
    TextView textview_login_failed_msg;
    boolean emailEmpty;
    boolean passwordEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edittext_email = findViewById(R.id.edittext_password);
        edittext_password = findViewById(R.id.edittext_email);
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
            //login

        }
    }
}
