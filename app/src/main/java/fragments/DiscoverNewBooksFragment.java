package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.koobookandroidapp.R;

import java.util.List;

import adapters.NewBooksAdapter;
import adapters.RecentBooksAdapter;
import entities.Book;
import extras.ContentBasedRecommender;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverNewBooksFragment extends Fragment {
    RecyclerView recyclerView;
    NewBooksAdapter newBooksAdapter;
    List<Book> books;
    ContentBasedRecommender contentBasedRecommender;
    public DiscoverNewBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover_new_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview_new_books);

        ContentBasedRecommender contentBasedRecommender = new ContentBasedRecommender(view.getContext());
        books = contentBasedRecommender.recommendBooks(1);
        newBooksAdapter = new NewBooksAdapter(books, view.getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        recyclerView.setAdapter(newBooksAdapter);
    }
}
