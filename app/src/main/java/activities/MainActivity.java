package activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toolbar;


import com.example.koobookandroidapp.R;

import fragments.CameraViewFragment;
import fragments.HomeFragment;
import fragments.SearchFragment;
import fragments.SettingsFragment;

//Credit to "yoursTRULY" from https://www.youtube.com/watch?v=oeKtwd1DBfg for tutorial on creating bottom navigation menu with animated transitions
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment =  new HomeFragment();
    CameraViewFragment cameraViewFragment = new CameraViewFragment();
    SearchFragment searchFragment = new SearchFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    Toolbar toolbar;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Make the toolbar visible
        //toolbar.getContext().setTheme(R.style.ThemeOverlay_AppCompat_Dark_ActionBar);

        //The home icon will initially be selected and the item selected listerner is assigned to the bottom menu element.
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.bringToFront();
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);


    }


    //This will deal with when an icon, from the bottom menu, is selected and will open the fragment that correpsonds to the selected icon.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, new HomeFragment()).commit();
                toolbar.setTitle("Home");
                break;


            case R.id.navigation_scan:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, cameraViewFragment).commit();
                toolbar.setTitle("Scan book");
                break;

            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, searchFragment).commit();
                toolbar.setTitle("Search");
                break;

            case R.id.navigation_settings:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, settingsFragment).commit();
                toolbar.setTitle("Settings");
                break;
        }
        return true;
    }
}
