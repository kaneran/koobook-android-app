package espressotests;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.koobookandroidapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import activities.LoginActivity;
import activities.MainActivity;
import fragments.CameraViewFragment;
import fragments.SearchFragment;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final IntentsTestRule<MainActivity> main = new IntentsTestRule<>(MainActivity.class);



    @Test
    public void navigateToCameraPreviewScreen(){

        onView(withId(R.id.navigation_search)).perform(click());
        onView(withId(R.id.camera_view)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnToolbarMenu(){


        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText("About")).check(matches(isDisplayed()));
        onView(withText("Help")).check(matches(isDisplayed()));
    }

}
