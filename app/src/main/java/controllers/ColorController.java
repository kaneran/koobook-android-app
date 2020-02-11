package controllers;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.widget.EditText;

import java.util.HashMap;

public class ColorController {

    HashMap<Colors,String> colorMap;

    //This method works by adding certain color type along with its HEX value to the hashmap which stores such values
    //It then uses the color(passed into the method's argument) to get the HEX value of the color from the hashmap.
    //This HEX value was then parsed and used to create a ColorStateList instance. Finally, the ViewComp was used to set the background
    //tint of the edittext(passed into the method's argument) to the intended color.
    public void setBackgroundTint(EditText editText, Colors color){
        colorMap = new HashMap<>();
        colorMap.put(Colors.RED, "#D81B60");
        colorMap.put(Colors.WHITE,"#FFFFFF");
        String colorString = colorMap.get(color);

        //Credit to John C on https://stackoverflow.com/questions/40838069/programmatically-changing-underline-color-of-edittext
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(colorString));
        ViewCompat.setBackgroundTintList(editText, colorStateList);

    }

    //Used to specify the options of colors which are the possible colors of the background tints of the edit text fields in the
    //login and sign up screen.
    public enum Colors {
        RED,
        WHITE
    }

}
