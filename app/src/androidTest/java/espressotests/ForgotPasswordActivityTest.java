package espressotests;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.koobookandroidapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import activities.ForgotPasswordActivity;
import activities.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ForgotPasswordActivityTest {

    @Rule
    public final IntentsTestRule<ForgotPasswordActivity> fp = new IntentsTestRule<>(ForgotPasswordActivity.class);


    @Test
    public void clickNextWithoutEnteringEmail(){
        onView(withId(R.id.next_btn)).perform(click());

        onView(withId(R.id.textview_email_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_email_error_msg)).check(matches(withText("This field is required")));
    }

}
