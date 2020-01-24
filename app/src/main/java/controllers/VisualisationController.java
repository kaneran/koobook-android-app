package controllers;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import extras.Helper;

//Followed tutorial https://www.youtube.com/watch?v=aPkkhLWofv0 by Sarthi Technology to create the barchart was invovled using the Github library published by Mike Phil

//Credit to Sarthi Technology https://www.youtube.com/watch?v=Bd76zMHdrDE to correctly position the x axis labels

public class VisualisationController {
    Helper helper;
    List<String> formattedColumns;
    Context context;
    AuthorController authorController;

    public VisualisationController(Context context) {
        this.context = context;
    }

    //This method will initialise the barchart and display it with the data being passed in
    //Note that the image view being passed in will only apply
    public void displayVisualisation(BarChart barChart, final TextView textview_bar_selected, final String barSelectedTitle, final ImageView imageview_view_more, List<Pair<String,Integer>> data){
        helper = new Helper();
        authorController = new AuthorController(context);
        final List<Pair<String,Integer>> mData = data;
        final BarDataSet barDataSet1 = new BarDataSet(getbarEntries(mData),"DataSet 1");
        final BarData barData = new BarData();

        barData.setBarWidth(0.1f);
        barData.addDataSet(barDataSet1);
        barChart.setData(barData);

        barChart.getAxisRight().setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getLegend().setEnabled(false);

        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);

        barChart.setScaleEnabled(false);
        barData.setBarWidth(barChart.getXAxis().getGridLineWidth());

        barChart.getXAxis().setAxisMinimum(0.5f);
        barChart.getXAxis().setEnabled(false);

        barChart.invalidate();


        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int barEntryIndex = Math.round(e.getX());
                barEntryIndex--;
                String columnName = mData.get(barEntryIndex).first;
                textview_bar_selected.setText(barSelectedTitle+ columnName);
                if(imageview_view_more != null){
                    imageview_view_more.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void updateVisualisation(){

    }

    //Uses the data that was passed into the displayVisualisation method to create an list of bar entries
    public ArrayList<BarEntry> getbarEntries(List<Pair<String,Integer>> data){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, data.get(0).second));
        barEntries.add(new BarEntry(2.1f, data.get(1).second));
        barEntries.add(new BarEntry(3.2f, data.get(2).second));
        barEntries.add(new BarEntry(4.3f, data.get(3).second));
        barEntries.add(new BarEntry(5.4f, data.get(4).second));
        return barEntries;
    }
}
