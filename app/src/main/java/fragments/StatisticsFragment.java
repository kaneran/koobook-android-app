package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.List;

import activities.MainActivity;
import controllers.AuthorController;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {
    TextView[] dots;
    LinearLayout dots_layout;
    TextView textview_toolbar_title;
    CardView cardview_authors_liked;
    CardView cardview_authors_disliked;
    CardView cardview_genres_liked;
    CardView cardview_genres_disliked;
    CardView cardview_my_preferences;
    AuthorsLikedFragment authorsLikedFragment;
    AuthorsDislikedFragment authorsDislikedFragment;
    GenresLikedFragment genresLikedFragment;
    GenresDislikedFragment genresDislikedFragment;
    SummaryFragment summaryFragment;
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
        textview_toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        authorsLikedFragment = new AuthorsLikedFragment();
        authorsDislikedFragment = new AuthorsDislikedFragment();
        genresLikedFragment = new GenresLikedFragment();
        genresDislikedFragment = new GenresDislikedFragment();
        summaryFragment = new SummaryFragment();
        cardview_authors_liked = view.findViewById(R.id.cardview_authors_liked);
        cardview_authors_disliked = view.findViewById(R.id.cardview_authors_disliked);
        cardview_genres_liked = view.findViewById(R.id.cardview_genres_liked);
        cardview_genres_disliked = view.findViewById(R.id.cardview_genres_disliked);
        cardview_my_preferences = view.findViewById(R.id.cardview_my_preferences);
        dots_layout = view.findViewById(R.id.statistics_dot_layout);
        addDotsIndicator();

        textview_toolbar_title.setText("Statistics");

        cardview_authors_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, authorsLikedFragment).addToBackStack(null).commit();
                MainActivity.toolbar_title ="Statistics";
            }
        });

        cardview_genres_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, genresLikedFragment).addToBackStack(null).commit();
                MainActivity.toolbar_title ="Statistics";
            }
        });

        cardview_authors_disliked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, authorsDislikedFragment).addToBackStack(null).commit();
                MainActivity.toolbar_title ="Statistics";
            }
        });

        cardview_genres_disliked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, genresDislikedFragment).addToBackStack(null).commit();
                MainActivity.toolbar_title ="Statistics";
            }
        });
        cardview_my_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, summaryFragment).addToBackStack(null).commit();
            }
        });

    }

    public void addDotsIndicator(){
        dots = new TextView[3];
        for(int i =0; i< dots.length; i++){
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            if(i==2) {
                dots[i].setTextColor(getResources().getColor(R.color.white));
            } else{
                dots[i].setTextColor(getResources().getColor(R.color.violet));
            }
            dots_layout.addView(dots[i]);
        }
    }
}
