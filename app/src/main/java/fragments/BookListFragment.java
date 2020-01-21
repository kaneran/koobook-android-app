package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.List;

import adapters.BookAdapter;
import controllers.BookController;
import entities.Book;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment {
    BookController bookController;
    List<Book> books;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    TextView textview_no_books_msg;
    Toolbar toolbar;


    public BookListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookController = new BookController(view.getContext());
        toolbar = getActivity().findViewById(R.id.toolbar);
        books = bookController.getBooksBasedOnStatus(toolbar);
        recyclerView = view.findViewById(R.id.recyclerview);
        textview_no_books_msg = view.findViewById(R.id.textview_no_books_msg);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(books.size() >0){
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
            adapter = new BookAdapter(books,getContext());
            recyclerView.setAdapter(adapter);
        } else{
            textview_no_books_msg.setVisibility(View.VISIBLE);
        }


    }
}
