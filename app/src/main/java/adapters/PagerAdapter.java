package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.BriefSummaryTabFragment;
import fragments.RatingTabFragment;
import fragments.ReviewsTabFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;

    public PagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    //Setup the fragment to be loaded into the View holder based on the tab selected
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                BriefSummaryTabFragment briefSummaryTabFragment = new BriefSummaryTabFragment();
                return briefSummaryTabFragment;

            case 1:
                RatingTabFragment ratingTabFragment = new RatingTabFragment();
                return ratingTabFragment;

            case 2:
                ReviewsTabFragment reviewsTabFragment = new ReviewsTabFragment();
                return reviewsTabFragment;

             default:
                 return null;
        }

    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
