package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.BriefSummaryTabFragment;
import fragments.RatingTabFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;

    public PagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                BriefSummaryTabFragment briefSummaryTabFragment = new BriefSummaryTabFragment();
                return briefSummaryTabFragment;

            case 1:
                RatingTabFragment ratingTabFragment = new RatingTabFragment();
                return ratingTabFragment;

             default:
                 return null;
        }

    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
