package Colour;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.widget.EditText;

public class ColorManager {

    public void setBackgroundTint(EditText editText, String colorString){

        //Credit to John C on https://stackoverflow.com/questions/40838069/programmatically-changing-underline-color-of-edittext
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(colorString));
        ViewCompat.setBackgroundTintList(editText, colorStateList);

    }

}
