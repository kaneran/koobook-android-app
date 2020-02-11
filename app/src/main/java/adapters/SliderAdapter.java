package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragments.HomeFragment;
import fragments.LibraryFragment;

import fragments.StatisticsFragment;

public class SliderAdapter extends FragmentPagerAdapter {

    public SliderAdapter(FragmentManager fm) {
        super(fm);
    }

    //This allows the user to slide between the 3 different fragments
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                LibraryFragment libraryFragment = new LibraryFragment();
                return libraryFragment;
            case 1:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 2:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                return statisticsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
