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
import android.widget.CheckBox;
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
    TextView textview_toolbar_title;
    Spinner spinner;
    CheckBox checkbox_filter_books_by_rating;
    List<String> spinnerOptions;
    List<Book> temporaryBooks;
    List<Book> filteredBooks;
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
        textview_toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        spinner = view.findViewById(R.id.spinner);
        checkbox_filter_books_by_rating = view.findViewById(R.id.checkbox_filter_books_by_rating);
        spinnerOptions = new ArrayList<>();
        spinnerOptions.add("Select genre");
        books = bookController.getBooksBasedOnStatus(textview_toolbar_title);

        genres = genreController.getGenresOfBooks(books);
        genres = genreController.getUniqueGenres(genres);
        genres = helper.getValidValuesFromList(genres);
        spinnerOptions.addAll(genres);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, spinnerOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        recyclerView = view.findViewById(R.id.recyclerview_book_list_by_status);
        textview_no_books_msg = view.findViewById(R.id.textview_no_books_with_status_msg);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Check if there are books in book list. If there isn't then make the TextView ,for displaying the message that there are no books, visible. Otherwise, set the adapter to teh recycler view
        if(books.size() >0){
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
            adapter = new BookListAdapter(books,getContext());
            recyclerView.setAdapter(adapter);
        } else{
            textview_no_books_msg.setVisibility(View.VISIBLE);
        }

        spinner.setAdapter(spinnerAdapter);
        spinner.setDropDownVerticalOffset(120);
        //This listener will deal with event when a user selects an option from the spinner dropdown. If the selected option is not the default option then filter the books and re-initialise the adapter to
        //have the most updated list of books and after setting this adapter to the recycler view, the book displayed in the recycler view will be updated based on the selected option from the dropdown.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 ){
                    //Reload all books if not already
                    adapter = new BookListAdapter(books,getContext());
                    recyclerView.setAdapter(adapter);
                    filteredBooks = null;
                } else {
                    String genre = parent.getItemAtPosition(position).toString();
                    filteredBooks = bookController.getBooksBasedOnGenre(genre, books);
                    adapter = new BookListAdapter(filteredBooks,getContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //In order for the checkbox, after it being checked, to re-display the original books after unchecking it, I must temporarily store the list of books in a nother variable
        //So if the user unchecks the box, it will check whether it has stored the previous list of books before clicking the checkbox, if has not been stored then do nothing. Otherwise, load the
        //original data into the adapater class and re apply that adapter to the recycler view.
        checkbox_filter_books_by_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Is the checkbox checked?
                boolean checked = ((CheckBox)v).isChecked();
                List<Book> topBooks;
                if(checked){
                    temporaryBooks = filteredBooks;
                    if(filteredBooks != null){
                        topBooks = bookController.getBooksWithHighOverallRating(filteredBooks);
                    } else{
                        topBooks = bookController.getBooksWithHighOverallRating(books);
                    }

                    adapter = new BookListAdapter(topBooks,getContext());
                    recyclerView.setAdapter(adapter);
                } else{
                    //The temporaryBooks being null implies that the user has not selected a genre from the dropdown and hence why it should all the books. If it does not equal null then this implies
                    //that user actually selected an option from the dropdown and should revert to display all the books based on that selected genre.
                    if(temporaryBooks != null){
                        adapter = new BookListAdapter(temporaryBooks,getContext());
                        recyclerView.setAdapter(adapter);
                    } else{
                        adapter = new BookListAdapter(books,getContext());
                        recyclerView.setAdapter(adapter);

                    }

                }
            }
        });


    }


}
