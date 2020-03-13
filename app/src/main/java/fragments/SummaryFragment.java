package fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.List;

import controllers.AuthorController;
import controllers.GenreController;
import controllers.ReviewsController;
import entities.Book;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment {
    TextView textview_most_liked_genre, textview_most_disliked_genre, textview_most_liked_author, textview_most_disliked_author, textview_reviews;
    AuthorController authorController;
    GenreController genreController;
    ReviewsController reviewsController;
    TextView textview_toolbar_title;
    List<String> reviews;
    Button button_get_definition_of_book;
    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textview_toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        textview_most_liked_genre = view.findViewById(R.id.textview_summary_most_liked_genre);
        textview_most_disliked_genre = view.findViewById(R.id.textview_summary_most_disliked_genre);
        textview_most_liked_author = view.findViewById(R.id.textview_summary_most_liked_author);
        textview_most_disliked_author = view.findViewById(R.id.textview_summary_most_disliked_author);
        textview_reviews = view.findViewById(R.id.textview_summary_reviews);
        button_get_definition_of_book = view.findViewById(R.id.button_get_definition_of_book);

        authorController = new AuthorController(view.getContext());
        genreController = new GenreController(view.getContext());
        reviewsController = new ReviewsController(view.getContext());

        textview_toolbar_title.setText("Summary");

        List<Pair<String,Integer>> mostLikedGenreData = new ArrayList<>();
        List<Pair<String,Integer>> mostDislikedGenreData = new ArrayList<>();
        List<Pair<String,Integer>> mostLikedAuthorData = new ArrayList<>();
        List<Pair<String,Integer>> mostDislikedAuthorData = new ArrayList<>();


        //I encountered a weird problem where the previous data is added to the new data which is incorrect, hence I had to create a new instance after collecting the data
        mostLikedGenreData = genreController.getMostLikedGenres();
        genreController = new GenreController(view.getContext());
        mostDislikedGenreData = genreController.getMostDislikedGenres();
        mostLikedAuthorData = authorController.getMostLikedAuthors();
        authorController = new AuthorController(view.getContext());
        mostDislikedAuthorData = authorController.getMostDislikedAuthors();

        textview_most_liked_genre.setText(genreController.getTopGenreFromPairData(mostLikedGenreData));
        textview_most_disliked_genre.setText(genreController.getTopGenreFromPairData(mostDislikedGenreData));
        textview_most_liked_author.setText(authorController.getTopAuthorFromPairData(mostLikedAuthorData));
        textview_most_disliked_author.setText(authorController.getTopAuthorFromPairData(mostDislikedAuthorData));

        button_get_definition_of_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                reviewsController.execute();

                while(reviewsController.getPositiveReviewsList() == null){
                    //Do nothing until it Returns a list of reviews or an empty list

                }
                reviews = reviewsController.getPositiveReviewsList();
                if(reviews.size()>0) {
                    for (String review : reviews) {
                        //Have are 2 lines between each review to ensure adequate spacing
                        sb.append(review + "\n\n");
                    }

                    textview_reviews.setText(sb.toString());
                } else{
                    textview_reviews.setText("Unavailable");
                }
                button_get_definition_of_book.setVisibility(View.INVISIBLE);
            }
        });

    }
}
