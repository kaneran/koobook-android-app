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

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    EditText edittext_isbn, edittext_title, edittext_author;
    TextView textview_search_error_msg;
    Button button_search;
    BookController bookController;
    LoadingScreenFragment loadingScreenFragment;

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
        loadingScreenFragment = new LoadingScreenFragment();
        edittext_isbn = view.findViewById(R.id.edittext_isbn);
        edittext_title = view.findViewById(R.id.edittext_title);
        edittext_author = view.findViewById(R.id.edittext_author);
        button_search = view.findViewById(R.id.button_search);
        bookController = new BookController(getContext());
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isbn = edittext_isbn.getText().toString();
                String title = edittext_title.getText().toString();
                String author = edittext_author.getText().toString();
                bookController.searchBook(isbn, title, author);
            }
        });
    }
}
