package activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import controllers.UserController;
import extras.JavaMail;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edittext_email;
    TextView textview_email_error_msg;
    String email;
    String oneTimePassword;
    String user_id;
    boolean emailValidated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        edittext_email = findViewById(R.id.edittext_email);
        textview_email_error_msg = findViewById(R.id.textview_email_error_msg);
    }

    public void nextButtonClicked(View v){
        UserController userController = new UserController();
        email = edittext_email.getText().toString();

        //Validate email first
        emailValidated = userController.checkEditTextFieldNonEmpty(edittext_email, textview_email_error_msg, false);

        if(emailValidated == true){
            oneTimePassword = userController.generateOneTimePassword();
            user_id = userController.getUserIdFromSqlServerDatabase(email);
            userController.storeUserId(this, Integer.parseInt(user_id));
            userController.storeOneTimePassword(this, oneTimePassword);

            //Send mail using the one time password
            JavaMail javaMail = new JavaMail(this, email, "Koobook authentication", "Please use the following One Time Password(OTP): "+oneTimePassword);
            javaMail.execute();
        }


    }
}
