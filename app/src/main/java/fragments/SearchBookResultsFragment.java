package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.List;

import adapters.SearchResultsAdapter;
import controllers.BookController;
import entities.Book;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBookResultsFragment extends Fragment {
    BookController bookController;
    List<Book> books;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    TextView textview_no_search_results_msg;
    Toolbar toolbar;
    TextView textview_toolbar_title;

    public SearchBookResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_book_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookController = new BookController(view.getContext());
        textview_toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        books = bookController.getBooksFromBooksDataString();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        textview_toolbar_title.setText("Search results: "+ books.size() +" books found");
        recyclerView = view.findViewById(R.id.recyclerview_search_book_results);
        textview_no_search_results_msg = view.findViewById(R.id.textview_no_search_results_msg);
        adapter = new SearchResultsAdapter(books,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        if(!(books.size() ==0)){
            textview_no_search_results_msg.setVisibility(View.INVISIBLE);
        } else{
            textview_no_search_results_msg.setVisibility(View.VISIBLE);
        }
    }
}
