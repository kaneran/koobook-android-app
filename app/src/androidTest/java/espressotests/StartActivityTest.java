package espressotests;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.koobookandroidapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import activities.LoginActivity;
import activities.SignUpActivity;
import activities.StartActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class StartActivityTest {
    @Rule public final IntentsTestRule<StartActivity> start = new IntentsTestRule<>(StartActivity.class);


    @Test
    public void navigateToSignUpScreen(){
        onView(withId(R.id.sign_up_btn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(),SignUpActivity.class)));

    }

    @Test
    public void navigateToLoginScreen(){
        onView(withId(R.id.login_btn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), LoginActivity.class)));

    }
}
