package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.List;

import controllers.AuthorController;
import controllers.BookController;
import controllers.GenreController;
import controllers.ReviewsController;
import controllers.UserController;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment {
    TextView textview_most_liked_genre, textview_most_disliked_genre, textview_most_liked_author, textview_most_disliked_author, textview_reviews, textview_books_liked_count, textview_books_disliked_count;
    AuthorController authorController;
    GenreController genreController;
    ReviewsController reviewsController;
    BookController bookController;
    UserController userController;
    TextView textview_toolbar_title;
    List<String> reviews;
    int books_liked_count;
    int books_disliked_count;
    int userId;
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
        textview_most_liked_genre = view.findViewById(R.id.textview_favourite_genre);
        textview_most_disliked_genre = view.findViewById(R.id.textview_least_favourite_genre);
        textview_most_liked_author = view.findViewById(R.id.textview_favourite_author);
        textview_most_disliked_author = view.findViewById(R.id.textview_least_favourite_author);
        textview_reviews = view.findViewById(R.id.textview_summary_positive_reviews);
        textview_books_liked_count = view.findViewById(R.id.textview_books_liked_count);
        textview_books_disliked_count = view.findViewById(R.id.textview_books_disliked_count);

        authorController = new AuthorController(view.getContext());
        genreController = new GenreController(view.getContext());
        reviewsController = new ReviewsController(view.getContext());
        bookController = new BookController(view.getContext());
        userController = new UserController();

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

        userId = userController.getUserIdFromSharedPreferneces(view.getContext());
        books_liked_count = bookController.getBooksUsingStatus(userId, BookController.BookStatus.Liked, null).size();
        books_disliked_count = bookController.getBooksUsingStatus(userId, BookController.BookStatus.Disliked, null).size();

        if(books_liked_count>0) {
            textview_books_liked_count.setText(books_liked_count + " books");
        }

        if(books_disliked_count>0) {
            textview_books_disliked_count.setText(books_disliked_count + " books");
        }

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


    }
}
