package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koobookandroidapp.R;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.HashMap;

import adapters.NewBooksAdapter;
import controllers.BookController;
import entities.Book;
import extras.ContentBasedRecommender;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookRecommendationsFragment extends Fragment {
    RecyclerView recyclerView;
    NewBooksAdapter newBooksAdapter;
    Object[] books;
    ContentBasedRecommender contentBasedRecommender;
    BookController bookController;
    FlexboxLayoutManager layoutManager;
    TextView textview_no_books_recommended_msg;
    LayoutInflater inflater;
    View mView;
    public BookRecommendationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_recommendations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview_new_books);
        layoutManager = new FlexboxLayoutManager();

        textview_no_books_recommended_msg = view.findViewById(R.id.textview_no_books_recommended_msg);

        //View
        inflater = getLayoutInflater();
        mView = inflater.inflate(R.layout.recommended_book_info_toast, null);

        //Setup layout manager
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.STRETCH);

        contentBasedRecommender = new ContentBasedRecommender(getContext());
        bookController = new BookController(getContext());

        books = contentBasedRecommender.recommendBooks(1);
        if(books.length > 0) {
            newBooksAdapter = new NewBooksAdapter(books, getContext(), mView, recyclerView);
            //newBooksAdapter.insertColour();
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(newBooksAdapter);

            //If there are no recommended books then display the message to inform the user of the situation
        } else{
            textview_no_books_recommended_msg.setVisibility(View.VISIBLE);
        }
    }
}
