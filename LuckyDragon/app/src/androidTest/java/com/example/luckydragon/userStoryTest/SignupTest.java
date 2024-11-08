package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
<<<<<<< HEAD
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
=======
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
>>>>>>> origin/main
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.mockito.ArgumentMatchers.any;
<<<<<<< HEAD
=======
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import static java.util.Objects.nonNull;
>>>>>>> origin/main

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
<<<<<<< HEAD
import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;

=======
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Event;
>>>>>>> origin/main
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.R;
import com.example.luckydragon.SelectRoleActivity;
<<<<<<< HEAD

import org.junit.Test;

import java.util.HashMap;
=======
import com.example.luckydragon.TestHelpers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
>>>>>>> origin/main

public class SignupTest extends MockedDb {
    @Override
    protected HashMap<String, Object> getMockData() {
        return null;  // User does not exist yet
    }
    @Override
    protected HashMap<String, Object> getMockEventData() {
       return null;  // all events do not exist
    }

    /**
     * USER STORY TEST
     * > US 01.02.01 Entrant - provide my personal information
     *      such as name, email and optional phone number in the app.
     * User opens app and selects "Entrant".
     * User is shown the signup page
     * User enters in their info. INCLUDING PHONE #
     * User selects confirm.
     * User is now in their profile with their provided info displayed.
     */
    @Test
    public void testEntrantSignupWPhone() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            String name = "Jake Paul";
            String email = "jake@youtube.com";
            String phone = "1231231234";
            // Click entrant button
            onView(withId(R.id.entrantButton)).perform(click());

            // Check we are in signup
            onView(withId(R.id.signupName)).check(matches(isDisplayed()));

            // Enter in info
            onView(withId(R.id.signupName)).perform(ViewActions.typeText(name));
            onView(withId(R.id.signupEmail)).perform(ViewActions.typeText(email));
            onView(withId(R.id.signupPhone)).perform(ViewActions.typeText(phone));

            // Click confirm
            onView(withText("Submit")).perform(click());

            // Check that profile displays info
            onView(withId(R.id.nameTextView)).check(matches(withText(name)));
            onView(withId(R.id.emailTextView)).check((matches(withText(email))));
            onView(withId(R.id.phoneNumberTextView)).check((matches(withText(phone))));
        }
    }
    /**
     * USER STORY TEST
     * > US 01.02.01 Entrant - provide my personal information
     *      such as name, email and optional phone number in the app.
     * User opens app and selects "Entrant".
     * User is shown the signup page
     * User enters in their info. WITHOUT PHONE #
     * User selects confirm.
     * User is now in their profile with their provided info displayed.
     */
    @Test
    public void testEntrantSignupNoPhone() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            String name = "Jake Paul";
            String email = "jake@youtube.com";
            String phone = "1231231234";

            // Click entrant button
            onView(withId(R.id.entrantButton)).perform(click());

            // Check we are in signup
            onView(withId(R.id.signupName)).check(matches(isDisplayed()));

            // Enter in info
            onView(withId(R.id.signupName)).perform(ViewActions.typeText(name));
            onView(withId(R.id.signupEmail)).perform(ViewActions.typeText(email));
            onView(withId(R.id.signupPhone)).perform(ViewActions.typeText(phone));
            onView(withId(R.id.signupPhone)).perform(ViewActions.clearText());

            // Click confirm
            onView(withText("Submit")).perform(click());  // TODO: Somehow cant find view without typing in signup phone. Maybe delay?

            // Check that profile displays info
            onView(withId(R.id.nameTextView)).check(matches(withText(name)));
            onView(withId(R.id.emailTextView)).check((matches(withText(email))));
            onView(withId(R.id.phoneNumberTextView)).check((matches(withText(""))));
        }

    }
}
