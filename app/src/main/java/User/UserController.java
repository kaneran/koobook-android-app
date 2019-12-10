package User;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

public class UserController {
    TextView firstNamerequiredTextView;
    EditText firstNameEditText;


    public void checkIfUserEnteredInformationInAllFields(TextView firstNamerequiredTextView, EditText firstNameEditText ){
        if(firstNameEditText.getText().toString().matches("")){
            firstNamerequiredTextView.setVisibility(View.VISIBLE);

        }

    }
}
