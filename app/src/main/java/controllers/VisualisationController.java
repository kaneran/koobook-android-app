package controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koobookandroidapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import extras.Helper;
import extras.MyValueFormatter;
import fragments.AuthorsLikedFragment;

//Followed tutorial https://www.youtube.com/watch?v=aPkkhLWofv0 by Sarthi Technology to create the barchart was invovled using the Github library published by Mike Phil

//Credit to Sarthi Technology https://www.youtube.com/watch?v=Bd76zMHdrDE to correctly position the x axis labels

public class VisualisationController {
    AuthorsLikedFragment authorsLikedFragment;
    Context context;
    BarData barData;


    public VisualisationController(Context context) {
        this.context = context;
        authorsLikedFragment = new AuthorsLikedFragment();
    }

    //This method uses the fragment manager to update the view to display the "AuthorsLiked" fragment
    public void restoreOriginalDataVisualisation(){
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, authorsLikedFragment).commit();
    }


    //This method will initialise the barchart and display it with the data being passed in
    //Note that the image view being passed in will only apply
    //If there is no data then simply set the default message to say that there is no data available

    public void displayVisualisation(BarChart barChart, final TextView textview_bar_selected, final String barSelectedTitle, final ImageView imageview_view_more,final  ImageView imageview_go_back, List<Pair<String,Integer>> data){

        final List<Pair<String, Integer>> mData = data;

        //Check if the data passed into the method's arguments contains more than one pair of genres/frequencies. If it doesn't then that means that the user only liked books a certain genre of means and there nothing valuable
        //to show in the bar chart. Hence why the I configured the bar chart set the message to be "Nothing insightful to show" when it has no data. If it does contain more than one then then two bar data sets will be created.

        if((data.size()==1)){
            barChart.setNoDataText("Nothing insightful to show");
        } else if(data.size()>1) {

            //The first bar data set will be used to get the Bar data set only for the first pair and the second bar data set is for the other pairs. I separated the pairs because this was
            // the only way in to modify the colour of a particular bar
            BarDataSet barDataSet1 = new BarDataSet(getBarEntries(data, true), "");
            BarDataSet barDataSet2 = new BarDataSet(getBarEntries(data, false), "");

            barData = new BarData();
            //If the top 2 data values have the same frequency then make all bar entires the same colour
            if(data.get(0).second.equals(data.get(1).second)){
                barDataSet1.setColor(Color.parseColor("#72b3e8"));
                barDataSet1.setValueTextColor(Color.parseColor("#72b3e8"));

                barDataSet2.setColor(Color.parseColor("#72b3e8"));
                barDataSet2.setValueTextColor(Color.parseColor("#72b3e8"));

                //Else make the first bar to be a different color from the other bars
            } else{
                //Modify color of first bar
                barDataSet1.setColor(Color.parseColor("#00E676"));
                barDataSet1.setValueTextColor(Color.parseColor("#00E676"));

                //Modify color of the other bars
                barDataSet2.setColor(Color.parseColor("#72b3e8"));
                barDataSet2.setValueTextColor(Color.parseColor("#72b3e8"));
            }

            //Depending on the Bar selected title(passed into the method's argument), the TextView element(also passed into the method's argument) will be display the relevent message along the lines of "Tap on one of the bar to view the [genre/author]"
            if (barSelectedTitle.equals("Genre selected: ")) {
                textview_bar_selected.setText("Tap on one of the bars to view the genre");
                barData.removeDataSet(0);
                barChart.invalidate();


            } else {
                textview_bar_selected.setText("Tap on one of the bars to view the author");
                barData.removeDataSet(0);
                barChart.invalidate();

                //Set the "Go back" button to be invisible initially
                if(imageview_go_back != null){
                    imageview_go_back.setVisibility(View.INVISIBLE);
                }
            }

            //Modified the presentation and layout of the barchart
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


            //If one of the bars is selected then it first get the index of the bar with respect to its position on the x axis. It then uses this index to check whether it equals any of the 5 possible values being 1,3, 5,7 or 9.
            //once the index matches one of those values then get the corresponding column name from the data(passed into the method's arguments. It then stores this column name into a shared preference file
            //and modifies the TextView element (passed into the method's argument) to display the message to inform the user on which bar they selected. Also, if the "view more" button is not null then make it visible.
            //It then checks if the chart being display is for the genres of the authors books. If it is then hide the "View more" button
            //The go back button being null will indicate which Fragment page I am in i.e the fragment that displays the most like genres overall
            //or the fragment that displays the most liked genres based on a given author. Therefore, if the bar chart being displays is for the genres of the author's book and the "Go back" button is not null,
            //then it will be made visible.
            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    //Get x axis value of the selected bar
                    int barEntryIndex = Math.round(e.getX());
                    String columnName = "";
                    //Use x axis value to get the genre/author label
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

                    //Store genre/author label in preference file
                    storeSelectedBarLabel(context, columnName);

                    //Update textview to show the label that the user selected
                    textview_bar_selected.setText(barSelectedTitle + columnName);

                    if (imageview_view_more != null) {
                        imageview_view_more.setVisibility(View.VISIBLE);
                    }


                    if (barSelectedTitle.equals("Genre selected: ") && imageview_go_back != null) {
                        imageview_view_more.setVisibility(View.INVISIBLE);
                    } else if(barSelectedTitle.equals("Genre selected: ") && imageview_go_back == null){
                        if(imageview_view_more != null) {
                            imageview_view_more.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }

    }

    //This method works by first removing the data sets from the bar data. It then makes the "View more"(passed into the method's argument) button invisible. It then makes the "Go back"(passed into the method's argument)
    //button and its associative TextView(passed into the method's argument) visible. It then checks to see if the data(passed into the method's argument) contains only one or zero paris of data.
    //If this is met then the bar chart will be made invisible and the getting TextView element ,which holds the value on what bar was selected, to display the message "Nothing to show".If the check is not met then
    //use the this methods arguments and pass it into the displayVisualation() method. After executing that method, it checks whether the bar selected title string(passed into the method's arguments) is equal to
    // to "Genre selected: ". If it does then get the TextView element to display the message "Tap on one of the bars to view the genre". If it does not equal that but equals "Author selected: " then get the same TextView element
    //to display the message "Tap on one of the bars to view the author". Additionally, it will also make the "Go back" button and textview to be invisible as this section will be visible when the chart is displaying
    //datta relating to the genres written by a given author.
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

    //This method works by usingdata that was passed method's arguments to create an list of bar entries. If the boolean flag isFirst(passed into the method's argument) equals true, then only use the first data pair
    //to create the list of bar entries. If it equals false then use  all, but excluding the first data pair, data pairs to create the list of bar entries. This list is then returned by this method.
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

    //This method works by storing the selected bar label in a shared preference file named "SelectedBarLabelPref"
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

    //This method works by retrieving the selected bar label from the shared preference file named "SelectedBarLabelPref"
    public String getSelectedBarLabelFromSharedPreferneces(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("SelectedBarLabelPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("selectedBarLabel", "");
    }


}
