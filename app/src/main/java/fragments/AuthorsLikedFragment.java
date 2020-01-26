package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
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
import controllers.GenreController;
import controllers.VisualisationController;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorsLikedFragment extends Fragment {
    BarChart barChart;
    TextView textview_authors_liked_main_title;
    TextView textview_most_liked_author;
    TextView textview_most_liked_author_selected;
    TextView textview_go_back;
    TextView textview_authors_liked_barchart_title;
    ImageView imageview_view_more;
    ImageView imageview_go_back;
    AuthorController authorController;
    GenreController genreController;
    VisualisationController visualisationController;
    AuthorsLikedFragment authorsLikedFragment;
    List<Pair<String,Integer>> data;
    int authorBookCount;


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
        authorsLikedFragment = new AuthorsLikedFragment();
        barChart = view.findViewById(R.id.barchart_most_liked_author);
        textview_authors_liked_main_title = view.findViewById(R.id.textview_authors_liked_main_title);
        textview_most_liked_author = view.findViewById(R.id.textview_most_liked_author);
        textview_most_liked_author_selected = view.findViewById(R.id.textview_most_liked_author_selected);
        textview_authors_liked_barchart_title = view.findViewById(R.id.textview_authors_liked_barchart_title);
        textview_go_back = view.findViewById(R.id.textview_go_back);
        imageview_view_more = view.findViewById(R.id.imageview_view_more);
        imageview_go_back = view.findViewById(R.id.imageview_go_back);
        authorController = new AuthorController(getContext());
        genreController = new GenreController(getContext());
        visualisationController = new VisualisationController(getContext());
        data = authorController.getMostLikedAuthors();
        textview_most_liked_author.setText(authorController.getMostLikedAuthorText(data));
        visualisationController.displayVisualisation(barChart, textview_most_liked_author_selected,"Author selected: ", imageview_view_more,imageview_go_back, data);

        imageview_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String author = visualisationController.getSelectedBarLabelFromSharedPreferneces(getContext());
                textview_authors_liked_main_title.setText("Most liked genre written by \n" + author);
                data = genreController.getTopGenresOfBooksWrittenByAuthorAndLikedByUser(author);
                textview_most_liked_author.setText(genreController.getMostLikedGenreText(data));
                textview_authors_liked_barchart_title.setText("Genres of "+author+"'s books you liked");
                visualisationController.updateVisualisation(barChart, textview_most_liked_author_selected,"Genre selected: ", imageview_view_more,imageview_go_back,textview_go_back, data);
            }
        });

        imageview_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, authorsLikedFragment).commit();

            }
        });
    }
}
