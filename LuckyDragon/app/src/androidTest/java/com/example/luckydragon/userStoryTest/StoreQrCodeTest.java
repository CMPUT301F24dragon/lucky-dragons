package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.EventList;
import com.example.luckydragon.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Contains tests for US 01.07.01 Entrant - be identified by my device, so that I don't have to use a username and password
 * Entrant - be identified by my device, so that I don't have to use a username and password
 */
public class StoreQrCodeTest extends MockedDb {
    // Mock organizer with an existing facility
    @Override
    protected HashMap<String, Object> getMockUserData() {
        // Define test user
        HashMap<String, Object> testUserData = new HashMap<>();
        // Personal info
        testUserData.put("name", "John Doe");
        testUserData.put("email", "jdoe@ualberta.ca");
        // Roles
        testUserData.put("isEntrant", true);
        testUserData.put("isOrganizer", true);
        testUserData.put("isAdmin", false);
        // Facility
        testUserData.put("facility", "The Sports Centre");

        return testUserData;
    }

    @Override
    protected void loadMockEventData(Map<String, Map<String, Object>> events) {}

    /**
     * USER STORY TEST
     * US 01.07.01 Entrant - be identified by my device, so that I don't have to use a username and password
     * User opens app and selects 'Organizer'.
     * User has an existing facility.
     * The user's facility is displayed correctly.
     * User clicks "Add Event" button.
     * Add Event Dialog is displayed.
     * User enters event details.
     * User clicks create event.
     * Event is created and a QR code is generated.
     * The QR code data is stored in Event and would be saved to db by save() call.
     */
    @Test
    public void storeQrCodeTest() {
        // Define test event data
        String testEventName = "Piano Lesson";
        String testAttendeeLimit = "5";

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);
        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User is not admin, so admin button should not show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));

            // User clicks "Organizer"
            onView(withId(R.id.organizerButton)).perform(click());

            // Profile activity should open and organizer profile should be displayed
            onView(withId(R.id.organizerProfileLayout)).check(matches(isDisplayed()));

            // User clicks "Add Event"
            onView(withId(R.id.addEventButton)).perform(click());

            // Add event dialog is displayed
            onView(withId(R.id.eventNameEditText)).check(matches(isDisplayed()));

            // Organizer enters event details
            // We will use default date and time since they use material components that would be hard to simulate
            onView(withId(R.id.eventNameEditText)).perform(typeText(testEventName));
            onView(withId(R.id.attendeeLimitEditText)).perform(typeText(testAttendeeLimit));

            // Click CREATE
            onView(withText("CREATE")).perform(click());

            // Check that the event shows on the organizer profile
            onData(anything()).inAdapterView(withId(R.id.organizerProfileEventsListview)).atPosition(0).
                    onChildView(withId(R.id.eventRowEventName)).check(matches(withText(testEventName)));

            // Check that the event is in the organizer's list and that the qr code has been generated
            // If this passes, then the QR code would be saved to db by Event.save() if it was not mocked out
            boolean eventIsPresent = false;
            EventList eventList = globalApp.getEvents();
            for(Event e : eventList.getEventList()) {
                if(Objects.equals(e.getName(), testEventName) && (e.getAttendeeSpots() == Integer.parseInt(testAttendeeLimit))) {
                    eventIsPresent = true;
                    assertNotNull(e.getQrHash());
                }
            }
            assertTrue(eventIsPresent);
        }
    }
}
