package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.koobookandroidapp.R;
import com.github.mikephil.charting.charts.BarChart;

import java.util.List;

import controllers.AuthorController;
import controllers.VisualisationController;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorsDislikedFragment extends Fragment {
    TextView textview_toolbar_title;
    BarChart barChart;
    TextView textview_authors_disliked_main_title;
    TextView textview_most_disliked_author;
    TextView textview_most_disliked_author_selected;
    TextView textview_authors_disliked_barchart_title;
    AuthorController authorController;
    VisualisationController visualisationController;
    List<Pair<String,Integer>> data;

    public AuthorsDislikedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authors_disliked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textview_toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        barChart = view.findViewById(R.id.barchart_most_disliked_author);
        textview_authors_disliked_main_title = view.findViewById(R.id.textview_authors_disliked_main_title);
        textview_most_disliked_author = view.findViewById(R.id.textview_most_disliked_author);
        textview_most_disliked_author_selected = view.findViewById(R.id.textview_most_disliked_author_selected);
        textview_authors_disliked_barchart_title = view.findViewById(R.id.textview_authors_disliked_barchart_title);
        authorController = new AuthorController(getContext());
        visualisationController = new VisualisationController(getContext());
        textview_toolbar_title.setText("Most disliked authors");
        data = authorController.getMostDislikedAuthors();
        textview_most_disliked_author.setText(authorController.getTopAuthorFromPairData(data));
        visualisationController.displayVisualisation(barChart, textview_most_disliked_author_selected,"Author selected: ", null,null, data);
    }

}
