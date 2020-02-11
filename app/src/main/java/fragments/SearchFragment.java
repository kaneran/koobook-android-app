package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import controllers.BookController;
import controllers.ColorController;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    EditText edittext_isbn, edittext_title, edittext_author;
    TextView textview_search_error_msg;
    Button button_search;
    BookController bookController;
    ColorController colorController;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edittext_isbn = view.findViewById(R.id.edittext_isbn);
        edittext_title = view.findViewById(R.id.edittext_title);
        edittext_author = view.findViewById(R.id.edittext_author);
        button_search = view.findViewById(R.id.button_search);
        textview_search_error_msg = view.findViewById(R.id.textview_search_error_msg);
        bookController = new BookController(getContext());
        colorController = new ColorController();
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isbn = edittext_isbn.getText().toString();
                String title = edittext_title.getText().toString();
                String author = edittext_author.getText().toString();
                //If the user clicks "Search" without entering anything in the fields then display the error message and set the background tint color of all EditText fields to be red.
                if(isbn.matches("") && title.matches("") && author.matches("")){
                    colorController.setBackgroundTint(edittext_isbn, ColorController.Colors.RED);
                    colorController.setBackgroundTint(edittext_author, ColorController.Colors.RED);
                    colorController.setBackgroundTint(edittext_title, ColorController.Colors.RED);
                    textview_search_error_msg.setVisibility(View.VISIBLE);
                } else {
                    bookController.searchBook(isbn, title, author);
                }
            }
        });
    }
}
