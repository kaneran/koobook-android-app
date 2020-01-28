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
import android.widget.ImageView;
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
public class GenresLikedFragment extends Fragment {
    Toolbar toolbar;
    BarChart barChart;
    TextView textview_genres_liked_main_title;
    TextView textview_most_liked_genre;
    TextView textview_most_liked_genre_selected;
    TextView textview_genres_liked_barchart_title;
    ImageView imageview_find_more_books;
    GenreController genreController;
    VisualisationController visualisationController;
    BookController bookController;
    List<Pair<String,Integer>> data;


    public GenresLikedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres_liked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = getActivity().findViewById(R.id.toolbar);
        bookController = new BookController(getContext());
        barChart = view.findViewById(R.id.barchart_most_liked_genre);
        textview_genres_liked_main_title = view.findViewById(R.id.textview_genres_liked_main_title);
        textview_most_liked_genre = view.findViewById(R.id.textview_most_liked_genre);
        textview_most_liked_genre_selected = view.findViewById(R.id.textview_most_liked_genre_selected);
        textview_genres_liked_barchart_title = view.findViewById(R.id.textview_genres_liked_barchart_title);
        imageview_find_more_books = view.findViewById(R.id.imageview_find_more_books);
        genreController = new GenreController(getContext());
        visualisationController = new VisualisationController(getContext());
        toolbar.setTitle("Most liked genres");
        data = genreController.getMostLikedGenres();
        textview_most_liked_genre.setText(genreController.getTopGenreFromPairData(data));
        visualisationController.displayVisualisation(barChart, textview_most_liked_genre_selected,"Genre selected: ", imageview_find_more_books,null, data);

        imageview_find_more_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String genre = visualisationController.getSelectedBarLabelFromSharedPreferneces(getContext());
                bookController.searchBook("",genre,"");
            }
        });

    }

}
