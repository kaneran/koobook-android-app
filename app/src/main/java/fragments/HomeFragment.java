package fragments;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import activities.LoginActivity;
import activities.MainActivity;
import controllers.BookController;
import controllers.UserController;
import dataaccess.setup.AppDatabase;
import extras.Helper;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    LinearLayout library_layout;
    LinearLayout preferences_layout;
    LinearLayout dots_layout;
    ImageView imageview_camera;
    ImageView imageview_search;
    Animation anim;
    BookController bookController;
    SearchFragment searchFragment;
    CameraViewFragment cameraViewFragment;
    AppDatabase db;
    private TextView[] dots;
    Helper helper;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraViewFragment = new CameraViewFragment();
        searchFragment = new SearchFragment();
        bookController = new BookController(view.getContext());
        imageview_camera = view.findViewById(R.id.imageview_camera);
        imageview_search = view.findViewById(R.id.imageview_search);
        library_layout = view.findViewById(R.id.library_layout);
        dots_layout = view.findViewById(R.id.home_dot_layout);
        preferences_layout = view.findViewById(R.id.preferences_layout);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
        imageview_camera.startAnimation(anim);
        addDotsIndicator();
        imageview_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, cameraViewFragment).commit();
            }
        });

        imageview_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, searchFragment).commit();
            }
        });

    }

    public void addDotsIndicator(){
        dots = new TextView[3];
        for(int i =0; i< dots.length; i++){
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            if(i==1) {
                dots[i].setTextColor(getResources().getColor(R.color.white));
            } else{
                dots[i].setTextColor(getResources().getColor(R.color.greyBlue));
            }
            dots_layout.addView(dots[i]);
        }
    }


}
