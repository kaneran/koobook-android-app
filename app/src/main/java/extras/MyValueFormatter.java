package extras;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MyValueFormatter extends ValueFormatter {

    //This edits the format, in which the bar values in the Bar chart, from double to integer
    @Override
    public String getFormattedValue(float value) {
        return ""+((int) value);
    }
}
