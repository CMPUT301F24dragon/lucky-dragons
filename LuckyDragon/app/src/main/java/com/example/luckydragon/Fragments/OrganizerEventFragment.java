package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.EntrantArrayAdapter;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.OrganizerEventView;

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

        // Set up waitlist list view
        ListView waitlistListView = view.findViewById(R.id.eventWaitlistListView);
        // Need a user array adapter
        entrantArrayAdapter = new EntrantArrayAdapter(event.getWaitlistUsers(), requireActivity().getApplicationContext(), this);
        waitlistListView.setAdapter(entrantArrayAdapter);
        TextView noWaitlisteesTextView = view.findViewById(R.id.noWaitlisteesTextView);
        if (event.getWaitlistUsers().size() == 0) {
            noWaitlisteesTextView.setVisibility(View.VISIBLE);
        }

        // Set up InviteList list view
        ListView invitelistListView = view.findViewById(R.id.eventInvitelistListView);
        // Need a user array adapter
        entrantArrayAdapter = new EntrantArrayAdapter(event.getInviteelistUsers(), requireActivity().getApplicationContext(), this);
        invitelistListView.setAdapter(entrantArrayAdapter);
        TextView noinviteesTextView = view.findViewById(R.id.noinviteesTextView);
        if (event.getInviteelistUsers().size() == 0) {
            noinviteesTextView.setVisibility(View.VISIBLE);
        }

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

        // Initialize view
        organizerEventView = new OrganizerEventView(event, this);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // the view associated with this fragment must stop observing when the fragment is destroyed
        organizerEventView.stopObserving();
    }
}
