package fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import adapters.SliderAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainSlider extends Fragment {
    ViewPager viewPager;


    SliderAdapter sliderAdapter;

    public MainSlider() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.main_slider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.slideviewpager);


        FragmentManager fm = getFragmentManager();
        sliderAdapter = new SliderAdapter(getChildFragmentManager());
        viewPager.setAdapter(sliderAdapter);
        viewPager.setCurrentItem(1);
    }


}
