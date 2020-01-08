package espressotests;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.koobookandroidapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import activities.ForgotPasswordActivity;
import activities.LoginActivity;
import activities.MainActivity;
import activities.ResetPasswordActivity;
import activities.SignUpActivity;
import activities.SplashActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
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
public class LoginActivityTest {

    @Rule
    public final IntentsTestRule<LoginActivity> login = new IntentsTestRule<>(LoginActivity.class);


    @Test
    public void loginWithoutProvidingUserData(){
        onView(withId(R.id.login_btn)).perform(click());

        onView(withId(R.id.textview_login_failed_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_login_failed_msg)).check(matches(withText("Email or password or both are empty")));
    }

    @Test
    public void userDoesNotExist(){
        String email = "Test@Test.com";
        String password = "Test123456";

        onView(withId(R.id.edittext_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.edittext_password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login_btn)).perform(click());

        onView(withId(R.id.textview_login_failed_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_login_failed_msg)).check(matches(withText("Username or password or both were incorrectly entered or the account does not exist. Please check and try again.")));
    }

    @Test
    public void navigateToSignUpScreen(){
        onView(withId(R.id.sign_up_btn)).perform(click());

        intended(hasComponent(new ComponentName(getTargetContext(), SignUpActivity.class)));
    }

    @Test
    public void navigateToForgotPasswordScreen(){
        onView(withId(R.id.textview_forgotpassword)).perform(click());

        intended(hasComponent(new ComponentName(getTargetContext(), ForgotPasswordActivity.class)));
    }


    @Test
    public void loginWithValidInformation(){

        String testEmail = "TestUser@Test.com";
        String testPassword = "Test123456";

        onView(withId(R.id.edittext_email)).perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.edittext_password)).perform(typeText(testPassword), closeSoftKeyboard());
        onView(withId(R.id.login_btn)).perform(click());


        intended(hasComponent(new ComponentName(getTargetContext(), SplashActivity.class)));
        try{
            //Wait 5 seconds which is how long the SplashAcitivty will be acitve for before loading the MainActivity
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
        }

        intended(hasComponent(new ComponentName(getTargetContext(), MainActivity.class)));
    }
}
