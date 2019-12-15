package controllers;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.widget.EditText;

import java.util.HashMap;

public class ColorController {

    HashMap<Colors,String> colorMap;

    public void setBackgroundTint(EditText editText, Colors color){
        colorMap = new HashMap<>();
        colorMap.put(Colors.RED, "#D81B60");
        colorMap.put(Colors.WHITE,"#FFFFFF");
        String colorString = colorMap.get(color);

        //Credit to John C on https://stackoverflow.com/questions/40838069/programmatically-changing-underline-color-of-edittext
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(colorString));
        ViewCompat.setBackgroundTintList(editText, colorStateList);

    }

    enum Colors {
        RED,
        WHITE
    }

}
