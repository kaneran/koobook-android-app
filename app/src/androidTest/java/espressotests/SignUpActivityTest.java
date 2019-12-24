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

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {
    @Rule
    public final IntentsTestRule<SignUpActivity> signUp = new IntentsTestRule<>(SignUpActivity.class);



    @Test
    public void signUpWithValidInformation(){

        String dummyFirstName = "Kai";
        String dummyEmail = dummyFirstName + "@.com";
        String dummyPassword = dummyFirstName + "123";
        onView(withId(R.id.edittext_email)).perform(typeText(dummyEmail));
        onView(withId(R.id.edittext_firstname)).perform(typeText(dummyFirstName));
        onView(withId(R.id.edittext_confirmpassword)).perform(scrollTo()).perform(typeText(dummyPassword));
        onView(withId(R.id.edittext_password)).perform(typeText(dummyPassword));




        onView(withId(R.id.sign_up_btn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), LoginActivity.class)));

    }
}
