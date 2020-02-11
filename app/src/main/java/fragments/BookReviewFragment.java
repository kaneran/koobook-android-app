package fragments;


import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import controllers.BookController;
import dialogs.DislikeBookDialog;
import dialogs.LikeBookDialog;
import dialogs.ReviewBookLaterDialog;


/**
 * A simple {@link Fragment} subclass.
 */
////Source- https://www.youtube.com/watch?v=zcnT-3F-9JA
public class BookReviewFragment extends Fragment implements RatingTabFragment.OnFragmentInteractionListener, BriefSummaryTabFragment.OnFragmentInteractionListener, ReviewsTabFragment.OnFragmentInteractionListener{

    TabLayout tabLayout;
    TextView textview_toolbar_title;
    Toolbar toolbar;
    Button button_like;
    Button button_reviewlater;
    Button button_dislike;

    public BookReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BookController bookController = new BookController(getContext());
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        textview_toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        button_like = getView().findViewById(R.id.button_like);
        button_reviewlater = getView().findViewById(R.id.button_reviewlater);
        button_dislike = getView().findViewById(R.id.button_dislike);
        bookController.displayBookInformation(view,textview_toolbar_title);
        tabLayout = getView().findViewById(R.id.tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("Brief"));
        tabLayout.addTab(tabLayout.newTab().setText("Rating"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = getView().findViewById(R.id.pager);
        final PagerAdapter adapter = new adapters.PagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //This listener will deal with events where the user selects a different tab.
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeBookDialog likeBookDialog = new LikeBookDialog();
                likeBookDialog.show(getFragmentManager(),"Liked dialog");
            }
        });

        button_reviewlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewBookLaterDialog reviewBookLaterDialog = new ReviewBookLaterDialog();
                reviewBookLaterDialog.show(getFragmentManager(), "Review later dialog");
            }
        });

        button_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DislikeBookDialog dislikeBookDialog = new DislikeBookDialog();
                dislikeBookDialog.show(getFragmentManager(), "Disliked book dialog");
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
