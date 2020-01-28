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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.List;

import adapters.BookListAdapter;
import controllers.BookController;
import controllers.GenreController;
import controllers.UserController;
import entities.Book;
import extras.Helper;


/**
 * A simple {@link Fragment} subclass.
 */

//Credit to https://codedocu.com/Details?d=1722&a=12&f=238&l=0 for the tutorial on creating the dropdown menu
public class BookListByStatusFragment extends Fragment {
    BookController bookController;
    GenreController genreController;
    UserController userController;
    Helper helper;
    List<Book> books;
    List<String> genres;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    TextView textview_no_books_msg;
    Toolbar toolbar;
    Spinner spinner;
    List<String> spinnerOptions;
    int userId;


    public BookListByStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_list_by_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookController = new BookController(view.getContext());
        genreController = new GenreController(view.getContext());
        userController = new UserController();
        userId = userController.getUserIdFromSharedPreferneces(view.getContext());
        helper = new Helper();
        toolbar = getActivity().findViewById(R.id.toolbar);
        spinner = view.findViewById(R.id.spinner);
        spinnerOptions = new ArrayList<>();
        spinnerOptions.add("Genre");
        books = bookController.getBooksBasedOnStatus(toolbar);

        genres = genreController.getGenresOfBooks(books);
        genres = genreController.getUniqueGenres(genres);
        genres = helper.getValidValuesFromList(genres);
        spinnerOptions.addAll(genres);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, spinnerOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        recyclerView = view.findViewById(R.id.recyclerview_book_list_by_status);
        textview_no_books_msg = view.findViewById(R.id.textview_no_books_with_status_msg);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(books.size() >0){
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
            adapter = new BookListAdapter(books,getContext());
            recyclerView.setAdapter(adapter);
        } else{
            textview_no_books_msg.setVisibility(View.VISIBLE);
        }

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 ){
                    //Do nothing
                } else {
                    String genre = parent.getItemAtPosition(position).toString();
                    books = bookController.getBooksBasedOnGenre(userId,genre);
                    adapter = new BookListAdapter(books,getContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


}
