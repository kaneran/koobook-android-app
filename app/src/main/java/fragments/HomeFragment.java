package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.koobookandroidapp.R;

import activities.LoginActivity;
import activities.MainActivity;
import controllers.BookController;
import controllers.UserController;
import dataaccess.setup.AppDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    LoginActivity loginActivity;
    CardView cardview_books_liked;
    CardView cardview_review_books;
    CardView cardview_my_preferences;
    CardView cardview_statistics;
    UserController userController;
    BookController bookController;
    BookListByStatusFragment bookListByStatusFragment;
    StatisticsFragment statisticsFragment;
    SummaryFragment summaryFragment;
    AppDatabase db;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookListByStatusFragment = new BookListByStatusFragment();
        statisticsFragment = new StatisticsFragment();
        summaryFragment = new SummaryFragment();
        bookController = new BookController(view.getContext());
        cardview_books_liked = view.findViewById(R.id.cardview_books_liked);
        cardview_review_books = view.findViewById(R.id.cardview_review_books);
        cardview_statistics = view.findViewById(R.id.cardview_statistics);
        cardview_my_preferences = view.findViewById(R.id.cardview_my_preferences);

        //If the user clicks the "Books Liked" option, then the book list type, being "Liked" will be saved in a preference file
        //and the Booklist fragment will be dispayed using fragment manager
        cardview_books_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookController.storeBookListType(v.getContext(), BookController.BookListType.Liked);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, bookListByStatusFragment).addToBackStack(null).commit();
                MainActivity.toolbar_title = "Home";
            }
        });

        cardview_review_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookController.storeBookListType(v.getContext(), BookController.BookListType.NeedsReviewing);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, bookListByStatusFragment).addToBackStack(null).commit();
                MainActivity.toolbar_title = "Home";
            }
        });

        cardview_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, statisticsFragment).addToBackStack(null).commit();
                MainActivity.toolbar_title = "Home";
            }
        });

        cardview_my_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, summaryFragment).addToBackStack(null).commit();
                MainActivity.toolbar_title = "Home";
            }
        });
    }
}
