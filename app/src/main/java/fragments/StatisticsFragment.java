package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.koobookandroidapp.R;

import java.util.List;

import controllers.AuthorController;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {
    CardView cardview_authors_liked;
    CardView cardview_authors_disliked;
    CardView cardview_genres_liked;
    CardView cardview_genres_disliked;
    AuthorsLikedFragment authorsLikedFragment;
    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authorsLikedFragment = new AuthorsLikedFragment();
        cardview_authors_liked = view.findViewById(R.id.cardview_authors_liked);
        cardview_authors_disliked = view.findViewById(R.id.cardview_authors_disliked);
        cardview_genres_liked = view.findViewById(R.id.cardview_genres_liked);
        cardview_genres_disliked = view.findViewById(R.id.cardview_genres_disliked);

        cardview_authors_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, authorsLikedFragment).commit();
            }
        });

        AuthorController authorController = new AuthorController(getContext());
        List<Pair<String,Integer>> pairs = authorController.getMostLikedAuthors();
    }
}
