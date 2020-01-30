package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import fragments.*;

import fragments.MainSlider;
import com.example.koobookandroidapp.R;

//Tutorial followed- https://www.youtube.com/watch?v=oeKtwd1DBfg
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    TextView textview_toolbar_title;
    public static String toolbar_title;
    MainSlider mainSlider = new MainSlider();
    HomeFragment homeFragment = new HomeFragment();
    CameraViewFragment cameraViewFragment = new CameraViewFragment();
    SearchFragment searchFragment = new SearchFragment();
    AboutFragment aboutFragment = new AboutFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =  findViewById(R.id.toolbar);
        textview_toolbar_title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        textview_toolbar_title.setText("Home");
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, mainSlider).commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, mainSlider).commit();
                textview_toolbar_title.setText("Home");
                toolbar.setVisibility(View.GONE);
                return true;

            case R.id.navigation_scan:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, cameraViewFragment).commit();
                textview_toolbar_title.setText("Scan book");
                toolbar.setVisibility(View.GONE);
                return true;

            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, searchFragment).commit();
                textview_toolbar_title.setText("Search book(s)");
                toolbar.setVisibility(View.GONE);
                return true;

        }
        return false;
    }




}
