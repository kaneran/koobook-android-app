package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.koobookandroidapp.R;

import controllers.BookController;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingScreenFragment extends Fragment {
    BookReviewFragment bookReviewFragment;
    BookController bookController;
    boolean bookExists;

    public LoadingScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_loading_screen, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookReviewFragment = new BookReviewFragment();
        final FragmentManager fragmentManager = getFragmentManager();
        bookController = new BookController(getContext());

        bookExists = bookController.checkIfBookExistsInDatabase(getContext());
        if(bookExists == false){
            bookController.execute();

        } else{
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, bookReviewFragment).commit();
        }
    }
}
