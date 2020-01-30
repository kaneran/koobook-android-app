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

import controllers.BookController;
import controllers.GenreController;
import controllers.VisualisationController;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenresDislikedFragment extends Fragment {
    TextView textview_toolbar_title;
    BarChart barChart;
    TextView textview_genres_disliked_main_title;
    TextView textview_most_disliked_genre;
    TextView textview_most_disliked_genre_selected;
    TextView textview_genres_disliked_barchart_title;
    GenreController genreController;
    VisualisationController visualisationController;
    BookController bookController;
    List<Pair<String,Integer>> data;

    public GenresDislikedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres_disliked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textview_toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        bookController = new BookController(getContext());
        barChart = view.findViewById(R.id.barchart_most_disliked_genre);
        textview_genres_disliked_main_title = view.findViewById(R.id.textview_genres_disliked_main_title);
        textview_most_disliked_genre = view.findViewById(R.id.textview_most_disliked_genre);
        textview_most_disliked_genre_selected = view.findViewById(R.id.textview_most_disliked_genre_selected);
        textview_genres_disliked_barchart_title = view.findViewById(R.id.textview_genres_disliked_barchart_title);
        genreController = new GenreController(getContext());
        visualisationController = new VisualisationController(getContext());
        textview_toolbar_title.setText("Most disliked genres");
        data = genreController.getMostDislikedGenres();
        textview_most_disliked_genre.setText(genreController.getTopGenreFromPairData(data));
        visualisationController.displayVisualisation(barChart, textview_most_disliked_genre_selected,"Genre selected: ", null,null, data);
    }
}
