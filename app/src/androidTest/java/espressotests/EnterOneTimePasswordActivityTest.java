package espressotests;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.koobookandroidapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import activities.EnterOneTimePasswordActivity;
import activities.ForgotPasswordActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EnterOneTimePasswordActivityTest {

    @Rule
    public final IntentsTestRule<EnterOneTimePasswordActivity> eotp = new IntentsTestRule<>(EnterOneTimePasswordActivity.class);


    @Test
    public void clickNextWithoutEnteringOTP(){
        onView(withId(R.id.next_btn)).perform(click());

        onView(withId(R.id.textview_onetimepassword_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_onetimepassword_error_msg)).check(matches(withText("This field is required")));
    }

    @Test
    public void clickNextWithIncorrectOTP(){
        onView(withId(R.id.edittext_onetimepassword)).perform(typeText("8332SQ"), closeSoftKeyboard());
        onView(withId(R.id.next_btn)).perform(click());

        onView(withId(R.id.textview_onetimepassword_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_onetimepassword_error_msg)).check(matches(withText("One Time Password (OTP) incorrect")));
    }
}
