package com.example.luckydragon;

import android.util.Log;

public class OrganizerProfileView extends Observer {
    private final OrganizerProfileFragment organizerProfileFragment;

    public OrganizerProfileView(User user, OrganizerProfileFragment organizerProfileFragment) {
        this.organizerProfileFragment = organizerProfileFragment;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        organizerProfileFragment.setFacilityName();
        organizerProfileFragment.updateEventsList();
    }
}