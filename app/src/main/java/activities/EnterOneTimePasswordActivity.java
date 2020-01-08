package activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koobookandroidapp.R;

import controllers.UserController;
import extras.JavaMail;

public class EnterOneTimePasswordActivity extends AppCompatActivity {
    EditText edittext_onetimepassword;
    TextView textview_onetimepassword_error_msg;
    String oneTimePasswordEntered;
    boolean oneTimePasswordValidated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_one_time_password);
        edittext_onetimepassword = findViewById(R.id.edittext_onetimepassword);
        textview_onetimepassword_error_msg = findViewById(R.id.textview_onetimepassword_error_msg);
    }

    public void nextButtonClicked(View v){
        UserController userController = new UserController();
        oneTimePasswordEntered = edittext_onetimepassword.getText().toString();

        //Validate one time password first
        oneTimePasswordValidated = userController.checkEditTextFieldNonEmpty(edittext_onetimepassword, textview_onetimepassword_error_msg, false);

        if(oneTimePasswordValidated == true){
            //reset boolean value
            oneTimePasswordValidated = false;
            String actualOneTimePassword = userController.getOneTimePasswordFromSharedPreferneces(this);
            oneTimePasswordValidated = userController.validateOneTimePassword(edittext_onetimepassword,textview_onetimepassword_error_msg,actualOneTimePassword);
            if(oneTimePasswordValidated == true){

                Toast.makeText(this, "One time password entered correctly", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ResetPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

        }


    }
}
