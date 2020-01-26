package controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import extras.Helper;
import extras.MyValueFormatter;

//Followed tutorial https://www.youtube.com/watch?v=aPkkhLWofv0 by Sarthi Technology to create the barchart was invovled using the Github library published by Mike Phil

//Credit to Sarthi Technology https://www.youtube.com/watch?v=Bd76zMHdrDE to correctly position the x axis labels

public class VisualisationController {
    Helper helper;
    List<String> formattedColumns;
    Context context;
    AuthorController authorController;
    GenreController genreController;
    BarData barData;

    public VisualisationController(Context context) {
        this.context = context;
        authorController = new AuthorController(context);
        genreController = new GenreController(context);

    }

    //This method will initialise the barchart and display it with the data being passed in
    //Note that the image view being passed in will only apply
    //If there is no data then simply set the default message to say that there is no data available
    public void displayVisualisation(BarChart barChart, final TextView textview_bar_selected, final String barSelectedTitle, final ImageView imageview_view_more,final  ImageView imageview_go_back, List<Pair<String,Integer>> data){

        final List<Pair<String, Integer>> mData = data;
        if((data.size()==1)){
            barChart.setNoDataText("Nothing insightful to show");
        } else if(data.size()>1) {
            BarDataSet barDataSet1 = new BarDataSet(getBarEntries(data, true), "");
            BarDataSet barDataSet2 = new BarDataSet(getBarEntries(data, false), "");

            barData = new BarData();
            //If the top 2 data values have the same frequency then make all bar entires the same colour
            if(data.get(0).second.equals(data.get(1).second)){
                barDataSet1.setColor(Color.parseColor("#72b3e8"));
                barDataSet1.setValueTextColor(Color.parseColor("#72b3e8"));

                barDataSet2.setColor(Color.parseColor("#72b3e8"));
                barDataSet2.setValueTextColor(Color.parseColor("#72b3e8"));
            } else{
                barDataSet1.setColor(Color.parseColor("#00416C"));
                barDataSet1.setValueTextColor(Color.parseColor("#00416C"));

                barDataSet2.setColor(Color.parseColor("#72b3e8"));
                barDataSet2.setValueTextColor(Color.parseColor("#72b3e8"));
            }


            if (barSelectedTitle.equals("Genre selected: ")) {
                textview_bar_selected.setText("Tap on one of the bars to view the genre");
                barData.removeDataSet(0);
                barChart.invalidate();
                imageview_go_back.setVisibility(View.VISIBLE);
            } else {
                textview_bar_selected.setText("Tap on one of the bars to view the author");
                barData.removeDataSet(0);
                barChart.invalidate();
                imageview_go_back.setVisibility(View.INVISIBLE);
            }


            barData.setBarWidth(0.9f);
            barData.addDataSet(barDataSet1);
            barData.addDataSet(barDataSet2);
            barChart.setData(barData);
            barDataSet1.setValueFormatter(new MyValueFormatter());
            barDataSet1.setValueTextSize(14f);

            barDataSet2.setValueFormatter(new MyValueFormatter());
            barDataSet2.setValueTextSize(14f);

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
                    //barEntryIndex--;
                    String columnName = "";
                    if (barEntryIndex == 1) {
                        columnName = mData.get(0).first;
                    } else if (barEntryIndex == 3) {
                        columnName = mData.get(1).first;
                    } else if (barEntryIndex == 5) {
                        columnName = mData.get(2).first;
                    } else if (barEntryIndex == 7) {
                        columnName = mData.get(3).first;
                    } else if (barEntryIndex == 9) {
                        columnName = mData.get(4).first;
                    }

                    storeSelectedBarLabel(context, columnName);
                    textview_bar_selected.setText(barSelectedTitle + columnName);
                    if (imageview_view_more != null) {
                        imageview_view_more.setVisibility(View.VISIBLE);
                    }

                    //If the chart being display is for the genres of the authors books, then hide the "View more" button
                    if (barSelectedTitle.equals("Genre selected: ")) {
                        imageview_view_more.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }

    }

    public void updateVisualisation(BarChart barChart, final TextView textview_bar_selected, final String barSelectedTitle, final ImageView imageview_view_more,final  ImageView imageview_go_back, TextView textview_go_back, List<Pair<String,Integer>> data){
        barData.removeDataSet(0);
        barData.removeDataSet(1);
        imageview_view_more.setVisibility(View.INVISIBLE);
        imageview_go_back.setVisibility(View.VISIBLE);
        textview_go_back.setVisibility(View.VISIBLE);
        if(data.size() <2){
            barChart.setVisibility(View.INVISIBLE);
            textview_bar_selected.setText("Nothing to show");
        } else {
            displayVisualisation(barChart, textview_bar_selected, barSelectedTitle, imageview_view_more, imageview_go_back, data);
            if (barSelectedTitle.equals("Genre selected: ")) {
                textview_bar_selected.setText("Tap on one of the bars to view the genre");

            } else if (barSelectedTitle.equals("Author selected: ")) {
                textview_bar_selected.setText("Tap on one of the bars to view the author");
                imageview_go_back.setVisibility(View.INVISIBLE);
                textview_go_back.setVisibility(View.INVISIBLE);
            }
        }

    }

    //Uses the data that was passed into the displayVisualisation method to create an list of bar entries
    public ArrayList<BarEntry> getBarEntries(List<Pair<String,Integer>> data, boolean isFirst){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        if(isFirst == false){
            int x = 3;
            for(int i=1; i< data.size(); i++){
                barEntries.add(new BarEntry(x, data.get(i).second));
                x += 2f;
            }

        } else{
            barEntries.add(new BarEntry(1, data.get(0).second));

        }

        return barEntries;
    }

    //Store the selected bar label in a shared preference file
    public boolean storeSelectedBarLabel(Context context, String selectedBarLabel) {
        try {
            SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("SelectedBarLabelPref", Context.MODE_PRIVATE).edit();
            editor.putString("selectedBarLabel", selectedBarLabel).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }

    //Retrieve the selected bar label from the shared preference file
    public String getSelectedBarLabelFromSharedPreferneces(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("SelectedBarLabelPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("selectedBarLabel", "");
    }
}
