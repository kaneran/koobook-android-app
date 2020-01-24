package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koobookandroidapp.R;
import com.github.mikephil.charting.charts.BarChart;

import java.util.List;

import controllers.AuthorController;
import controllers.VisualisationController;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorsLikedFragment extends Fragment {
    BarChart barChart;
    TextView textview_most_liked_author;
    TextView textview_most_liked_author_selected;
    ImageView imageview_view_more;
    AuthorController authorController;
    VisualisationController visualisationController;
    List<Pair<String,Integer>> data;
    public AuthorsLikedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authors_liked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barChart = view.findViewById(R.id.barchart_most_liked_author);
        textview_most_liked_author = view.findViewById(R.id.textview_most_liked_author);
        textview_most_liked_author_selected = view.findViewById(R.id.textview_most_liked_author_selected);
        imageview_view_more = view.findViewById(R.id.imageview_view_more);
        authorController = new AuthorController(getContext());
        visualisationController = new VisualisationController(getContext());

        data = authorController.getMostLikedAuthors();
        textview_most_liked_author.setText(data.get(0).first);
        visualisationController.displayVisualisation(barChart, textview_most_liked_author_selected,"Author selected: ", imageview_view_more, data);

        imageview_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualisationController.updateVisualisation();
            }
        });
    }
}
