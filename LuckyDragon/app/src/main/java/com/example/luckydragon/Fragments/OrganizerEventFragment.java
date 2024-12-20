package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.EntrantArrayAdapter;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.Models.UserList;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.OrganizerEventView;

import java.util.ArrayList;

/**
 * This is the fragment containing the organizer-specific event info.
 * Displays the different lists of entrants (waiting list, attendees list, etc).
 * ISSUES:
 *   - Need to add a view to update this when changes are made (e.g. waitlist capacity is changed).
 *   - When view is added, should add an onDestroy() method telling the view to stop observing.
 */
public class OrganizerEventFragment extends Fragment {
    private Event event;
    private EntrantArrayAdapter entrantArrayAdapter;
    private OrganizerEventView organizerEventView;
    private UserList userList;

    public EntrantArrayAdapter waitListUsersAdapter;
    public EntrantArrayAdapter inviteeListUsersAdapter;
    public EntrantArrayAdapter cancelledListUsersAdapter;
    public EntrantArrayAdapter attendeeListUsersAdapter;

    private ListView waitListUsersListView;
    private ListView inviteeListUsersListView;
    private ListView cancelledListUsersListView;
    private ListView attendeeListUsersListView;


    /**
     * Creates an OrganizerEventFragment.
     */
    public OrganizerEventFragment() {
        super(R.layout.fragment_organizer_event);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get event
        GlobalApp globalApp = (GlobalApp) requireActivity().getApplication();
        event = globalApp.getEventToView();

        waitListUsersListView = view.findViewById(R.id.eventWaitlistListView);
        inviteeListUsersListView = view.findViewById(R.id.eventInvitelistListView);
        cancelledListUsersListView = view.findViewById(R.id.eventCancelledlistListView);
        attendeeListUsersListView = view.findViewById(R.id.eventAttendeelistListView);

        waitListUsersAdapter = new EntrantArrayAdapter(new ArrayList<>(), requireActivity().getApplicationContext(),
                this, "waitList");
        inviteeListUsersAdapter = new EntrantArrayAdapter(new ArrayList<>(), requireActivity().getApplicationContext(),
                this, "inviteeList");
        cancelledListUsersAdapter = new EntrantArrayAdapter(new ArrayList<>(), requireActivity().getApplicationContext(),
                this, "cancelledList");
        attendeeListUsersAdapter = new EntrantArrayAdapter(new ArrayList<>(), requireActivity().getApplicationContext().getApplicationContext(),
                this, "attendeeList");

        setupAdapter(waitListUsersAdapter, waitListUsersListView);
        setupAdapter(inviteeListUsersAdapter, inviteeListUsersListView);
        setupAdapter(cancelledListUsersAdapter, cancelledListUsersListView);
        setupAdapter(attendeeListUsersAdapter, attendeeListUsersListView);

        // get all event data
        userList = ((GlobalApp) requireActivity().getApplication()).getUsers();

        if(event.hasGeolocation()) {
            Button seeMapButton = view.findViewById(R.id.seeMapButton);
            // Make "See Map" button visible
            seeMapButton.setVisibility(View.VISIBLE);
            // Initialize "See Map" on click listener
            seeMapButton.setOnClickListener(v -> {
                // Open fragment
                OrganizerMapFragment organizerMapFragment = new OrganizerMapFragment(event);
                organizerMapFragment.show(getActivity().getSupportFragmentManager(), "OrganizerMapFragment");
            });
        }

        // Add on click listener for "Send Notification" Button
        Button sendNotifButton = view.findViewById(R.id.sendNotifButton);
        sendNotifButton.setOnClickListener((View v) -> {
            DialogFragment sendNotifDialog = new OrganizerNotificationDialogFragment();
            sendNotifDialog.show(getChildFragmentManager(), "OrganizerNotificationDialogFragment");
        });

        // Initialize view
        organizerEventView = new OrganizerEventView(userList, this);
    }

    /**
     * Calls to update the adapters for each of the four listViews.
     */
    public void notifyAdapter() {
        updateAdapter(waitListUsersAdapter, waitListUsersListView, "waitList");
        updateAdapter(inviteeListUsersAdapter, inviteeListUsersListView, "inviteeList");
        updateAdapter(cancelledListUsersAdapter, cancelledListUsersListView, "cancelledList");
        updateAdapter(attendeeListUsersAdapter, attendeeListUsersListView, "attendeeList");
    }

    /**
     * Setups an adapter with its corresponding listView.
     * @param adapter the adapter for the listView
     * @param listView the corresponding listView
     */
    private void setupAdapter(EntrantArrayAdapter adapter, ListView listView) {
        // set the listview's adapter
        listView.setAdapter(adapter);
    }

    /**
     * Given an adapter, its listView, and the type of list, updates the
     * adapter to store the current users that are in the listType for this event.
     * @param adapter the adapter for the listView
     * @param listView the corresponding listView
     * @param listType the type of list, can be one of: <attendee|wait|invitee|cancelled>list
     */
    private void updateAdapter(EntrantArrayAdapter adapter, ListView listView, String listType) {
        // if the fragment is not on an activity, we don't need to update the adapter
        if (!isAdded()) return;

        ArrayList<User> userData = new ArrayList<>();
        for (User user : userList.getUserList()) {
            if ((listType.equals("attendeeList") && event.onAttendeeList(user.getDeviceId()))
                    ||  (listType.equals("waitList") && event.onWaitList(user.getDeviceId()))
                    ||  (listType.equals("inviteeList") && event.onInviteeList(user.getDeviceId()))
                    ||  (listType.equals("cancelledList") && event.onCancelledList(user.getDeviceId()))
                    ||  (listType.equals("attendeeList") && event.onAttendeeList(user.getDeviceId()))) {
                userData.add(user);
            }
        }
        adapter.clear();
        adapter.addAll(userData);
        adapter.notifyDataSetChanged();
    }

    /**
     * Updates the displayed waitlist capacity.
     */
    public void updateWaitlistCapacity() {
        TextView waitlistCapacityTextView = getView().findViewById(R.id.waitlistCapacityTextView);
        String waitlistLimit = "No Limit";
        if(event.getWaitListSpots() != -1) {
            waitlistLimit = String.format("%s", event.getWaitListSpots());
        }
        waitlistCapacityTextView.setText(String.format("Capacity: %s", waitlistLimit));
    }

    public void leaveWaitlist(String deviceId) {
        if (event.hasGeolocation()) {
            event.leaveWaitlistWithLocation(deviceId);
        } else {
            event.leaveWaitList(deviceId);
        }
        event.joinCancelledList(deviceId);
        event.save();
        organizerEventView.update(userList);
    }

    public void leaveInviteelist(String deviceId) {
        event.leaveInviteeList(deviceId);
        event.joinCancelledList(deviceId);
        event.save();
        organizerEventView.update(userList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // the view associated with this fragment must stop observing when the fragment is destroyed
        organizerEventView.stopObserving();
    }
}
