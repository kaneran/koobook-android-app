package espressotests;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.koobookandroidapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import activities.EnterOneTimePasswordActivity;
import activities.ResetPasswordActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ResetPasswordActivityTest {

    @Rule
    public final IntentsTestRule<ResetPasswordActivity> reset = new IntentsTestRule<>(ResetPasswordActivity.class);

    @Test
    public void clickResetWithoutEnteringPasswords(){
        onView(withId(R.id.reset_btn)).perform(click());

        onView(withId(R.id.textview_password_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_password_error_msg)).check(matches(withText("This field is required")));
        onView(withId(R.id.textview_passwords_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_passwords_error_msg)).check(matches(withText("This field is required")));
    }

    @Test
    public void clickResetWithMismatchingPasswords(){
        onView(withId(R.id.edittext_password)).perform(typeText("hduahwudhuwadu32"), closeSoftKeyboard());
        onView(withId(R.id.edittext_confirmpassword)).perform(typeText("xxcxbcb3mnm2"), closeSoftKeyboard());
        onView(withId(R.id.reset_btn)).perform(click());

        onView(withId(R.id.textview_password_error_msg)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textview_passwords_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_passwords_error_msg)).check(matches(withText("Passwords does not match")));
    }
}
