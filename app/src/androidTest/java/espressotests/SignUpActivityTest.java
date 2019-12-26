package espressotests;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import activities.LoginActivity;
import activities.MainActivity;
import activities.SignUpActivity;
import controllers.UserController;
import dataaccess.sqlserver.SqlServerDatabase;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {
    @Rule
    public final IntentsTestRule<SignUpActivity> signUp = new IntentsTestRule<>(SignUpActivity.class);



    @Test
    public void signUpWithValidInformation(){

        String dummyFirstName = "DummyTestUser";
        String dummyEmail = dummyFirstName + "@.com";
        String dummyPassword = dummyFirstName + "123456";

        onView(withId(R.id.edittext_email)).perform(typeText(dummyEmail), closeSoftKeyboard());
        onView(withId(R.id.edittext_firstname)).perform(typeText(dummyFirstName), closeSoftKeyboard());
        onView(withId(R.id.edittext_password)).perform(typeText(dummyPassword), closeSoftKeyboard());
        onView(withId(R.id.edittext_confirmpassword)).perform(swipeDown(), typeText(dummyPassword), closeSoftKeyboard());
        onView(withId(R.id.sign_up_btn)).perform(click());

        intended(hasComponent(new ComponentName(getTargetContext(), LoginActivity.class)));
        SqlServerDatabase ssd = new SqlServerDatabase();
        ssd.executeUpdateStatement("DELETE FROM [dbo].[User] WHERE FirstName ='DummyTestUser'");
    }

    @Test
    public void signUpWithInvalidFirstName(){
        String invalidName = "TestUser123";

        onView(withId(R.id.edittext_firstname)).perform(typeText(invalidName), closeSoftKeyboard());
        onView(withId(R.id.sign_up_btn)).perform(click());

        onView(withId(R.id.textview_firstname_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_firstname_error_msg)).check(matches(withText("Invalid first name")));

    }

    @Test
    public void signUpWithInvalidEmail(){
        String invalidEmail = "TestUser123";

        onView(withId(R.id.edittext_email)).perform(typeText(invalidEmail), closeSoftKeyboard());
        onView(withId(R.id.sign_up_btn)).perform(click());

        onView(withId(R.id.textview_email_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_email_error_msg)).check(matches(withText("Invalid email")));

    }

    @Test
    public void signUpWithPasswordThatHasSmallPasswordLength(){
        String invalidPassword = "Test12";

        onView(withId(R.id.edittext_password)).perform(typeText(invalidPassword), closeSoftKeyboard());
        onView(withId(R.id.edittext_confirmpassword)).perform(typeText(invalidPassword), closeSoftKeyboard());
        onView(withId(R.id.sign_up_btn)).perform(click());

        onView(withId(R.id.textview_password_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_passwords_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_password_error_msg)).check(matches(withText("Password length must be at least 8 characters long")));
        onView(withId(R.id.textview_passwords_error_msg)).check(matches(withText("Password length must be at least 8 characters long")));

    }
    @Test
    public void signUpWithPasswordThatDoesNotContainNumbers(){
        String invalidPassword = "Test";

        onView(withId(R.id.edittext_password)).perform(typeText(invalidPassword), closeSoftKeyboard());
        onView(withId(R.id.edittext_confirmpassword)).perform(typeText(invalidPassword), closeSoftKeyboard());
        onView(withId(R.id.sign_up_btn)).perform(click());

        onView(withId(R.id.textview_password_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_passwords_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_password_error_msg)).check(matches(withText("Password must contain at least one number")));
        onView(withId(R.id.textview_passwords_error_msg)).check(matches(withText("Password must contain at least one number")));

    }

    @Test
    public void signUpWithoutProvidingUserData(){
        onView(withId(R.id.sign_up_btn)).perform(click());

        onView(withId(R.id.textview_firstname_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_email_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_password_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_passwords_error_msg)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_password_error_msg)).check(matches(withText("This field is required")));
        onView(withId(R.id.textview_password_error_msg)).check(matches(withText("This field is required")));
        onView(withId(R.id.textview_firstname_error_msg)).check(matches(withText("This field is required")));
        onView(withId(R.id.textview_email_error_msg)).check(matches(withText("This field is required")));

    }

    @Test
    public void navigateToLoginScreen(){
        onView(withId(R.id.loginLinkText)).perform(click());

        intended(hasComponent(new ComponentName(getTargetContext(), LoginActivity.class)));
    }


}
