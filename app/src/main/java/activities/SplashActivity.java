package activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.koobookandroidapp.R;

//Credit to https://www.youtube.com/watch?v=f6n4jrx6J48 for the tutorial on creating the splash animation
public class SplashActivity extends AppCompatActivity {
    Animation anim;
    TextView textview_welcome_msg;
    private static int splashTimeOut= 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textview_welcome_msg = findViewById(R.id.textview_welcome_msg);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, 5000);

        anim = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        textview_welcome_msg.startAnimation(anim);
    }
}
