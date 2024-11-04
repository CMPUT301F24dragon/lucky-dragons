package com.example.luckydragon;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventView extends Observer {
    private TextView eventNameView;
    private TextView facilityNameView;
    private TextView dateAndTimeView;
    private TextView waitlistSpotsView;
    private TextView attendeeSpotsView;
    private TextView currentlyJoinedView;

    private TextView notSelectedText;
    private Button cancel;
    private Button decline;
    private Button accept;
    private Button signUp;

    private String deviceId;

    public EventView(Event event, String deviceId, EventActivity eventActivity) {
        this.deviceId = deviceId;

        eventNameView = eventActivity.findViewById(R.id.eventName);
        facilityNameView = eventActivity.findViewById(R.id.facilityName);
        dateAndTimeView = eventActivity.findViewById(R.id.dateAndTime);
        waitlistSpotsView = eventActivity.findViewById(R.id.waitlistSpots);
        attendeeSpotsView = eventActivity.findViewById(R.id.attendeeSpots);
        currentlyJoinedView = eventActivity.findViewById(R.id.currentlyJoined);

        signUp = eventActivity.findViewById(R.id.signUpButton);
        notSelectedText = eventActivity.findViewById(R.id.eventNotSelectedText);
        cancel = eventActivity.findViewById(R.id.eventCancel);
        decline = eventActivity.findViewById(R.id.eventDecline);
        accept = eventActivity.findViewById(R.id.eventAccept);

        startObserving(event);
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        eventNameView.setText(getObservable().getName());
        facilityNameView.setText(getObservable().getFacility());
        dateAndTimeView.setText(getObservable().getDateAndTime());
        waitlistSpotsView.setText(String.format("Waitlist Spots: %s", getObservable().getWaitListSpots()));
        attendeeSpotsView.setText(String.format("Attendee Spots: %s", getObservable().getAttendeeSpots()));
        currentlyJoinedView.setText(String.format("Currently Joined: %s", getObservable().getCurrentlyJoined()));
        // Switch mode to waitlist view
        if (getObservable().onWaitList(deviceId)) {
            // TODO:
            signUp.setVisibility(View.GONE);
            notSelectedText.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
            decline.setVisibility(View.GONE);
            accept.setVisibility(View.GONE);
        } else if (true) {

        }
        // Default mode is signup view
    }
}