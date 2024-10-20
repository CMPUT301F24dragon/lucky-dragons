package com.example.luckydragon;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class OrganizerProfileFragment extends Fragment {
    public OrganizerProfileFragment() {
        super(R.layout.fragment_organizer_profile);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Add on click listener for "Add Event" button
        Button addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener((View v) -> {
            DialogFragment addEventDialog = new AddEventDialogFragment();
            addEventDialog.show(getActivity().getSupportFragmentManager(), "AddEventDialogFragment");
        });
    }
}
