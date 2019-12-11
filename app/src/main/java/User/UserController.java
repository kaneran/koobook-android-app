package User;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import Colour.ColorManager;

public class UserController {
    TextView firstNamerequiredTextView;
    EditText firstNameEditText;
    EditText passwordEditText;
    EditText confirmedPasswordEditText;


    public void checkIfUserEnteredInformationInAllFields(HashMap<EditText,TextView> map, TextView passwordDoesNotMatchTextView){
        ColorManager colorManager = new ColorManager();
        for( EditText editText: map.keySet()){
            TextView fieldRequiredTextView = map.get(editText);
            if(editText.getText().toString().matches("")){


                //If the text view is for "confirmed password" then I must override its string value
                fieldRequiredTextView.setText(R.string.this_field_is_required);
                fieldRequiredTextView.setVisibility(View.VISIBLE);
                colorManager.setBackgroundTint(editText,"#D81B60");

            } else{
                colorManager.setBackgroundTint(editText,"#FFFFFF");
                fieldRequiredTextView.setVisibility(View.INVISIBLE);

                //Did this to ensure that the overwritten message for the text view was correct
                if(fieldRequiredTextView.equals(passwordDoesNotMatchTextView)){
                    fieldRequiredTextView.setText(R.string.passwords_does_not_match);

                }

            }
        }

    }

    public boolean checkIfPasswordsMatch(ArrayList<EditText> passwords, TextView passwordDoesNotMatchTextView){
        ColorManager colorManager = new ColorManager();

        if(passwordDoesNotMatchTextView.getText().toString().matches("This field is required")){
            return false;
        } else{

            passwordEditText = passwords.get(0);
            confirmedPasswordEditText = passwords.get(1);
            if(!passwordEditText.getText().toString().matches(confirmedPasswordEditText.getText().toString())){
                passwordDoesNotMatchTextView.setText(R.string.passwords_does_not_match);
                passwordDoesNotMatchTextView.setVisibility(View.VISIBLE);
                colorManager.setBackgroundTint(confirmedPasswordEditText,"#D81B60");
                colorManager.setBackgroundTint(passwordEditText,"#D81B60");
                return false;
            } else{
                BlowfishController blowfishController = new BlowfishController();
                String key = blowfishController.generateKey();

                return true;
            }

        }

    }
}
