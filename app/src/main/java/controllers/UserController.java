package controllers;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class UserController {
    TextView firstNamerequiredTextView;
    EditText firstNameEditText;
    EditText passwordEditText;
    EditText confirmedPasswordEditText;


    public void checkIfUserEnteredInformationInAllFields(HashMap<EditText,TextView> map, TextView passwordDoesNotMatchTextView){
        ColorController colorController = new ColorController();
        for( EditText editText: map.keySet()){
            TextView fieldRequiredTextView = map.get(editText);
            if(editText.getText().toString().matches("")){


                //If the text view is for "confirmed password" then I must override its string value
                fieldRequiredTextView.setText(R.string.this_field_is_required);
                fieldRequiredTextView.setVisibility(View.VISIBLE);
                colorController.setBackgroundTint(editText,"#D81B60");

            } else{
                colorController.setBackgroundTint(editText,"#FFFFFF");
                fieldRequiredTextView.setVisibility(View.INVISIBLE);

                //Did this to ensure that the overwritten message for the text view was correct
                if(fieldRequiredTextView.equals(passwordDoesNotMatchTextView)){
                    fieldRequiredTextView.setText(R.string.passwords_does_not_match);

                }

            }
        }

    }

    public boolean checkIfPasswordsMatch(ArrayList<EditText> passwords, TextView passwordDoesNotMatchTextView){
        ColorController colorController = new ColorController();

        if(passwordDoesNotMatchTextView.getText().toString().matches("This field is required")){
            return false;
        } else{

            passwordEditText = passwords.get(0);
            confirmedPasswordEditText = passwords.get(1);
            if(!passwordEditText.getText().toString().matches(confirmedPasswordEditText.getText().toString())){
                passwordDoesNotMatchTextView.setText(R.string.passwords_does_not_match);
                passwordDoesNotMatchTextView.setVisibility(View.VISIBLE);
                colorController.setBackgroundTint(confirmedPasswordEditText,"#D81B60");
                colorController.setBackgroundTint(passwordEditText,"#D81B60");
                return false;
            } else{
                BlowfishController blowfishController = new BlowfishController();
                String key = blowfishController.generateKey();

                return true;
            }

        }

    }
}
