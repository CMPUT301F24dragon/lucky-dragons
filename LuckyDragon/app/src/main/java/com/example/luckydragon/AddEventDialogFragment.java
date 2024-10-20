package com.example.luckydragon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;

public class AddEventDialogFragment extends DialogFragment {
    @Nullable private Integer timeHours = null;
    @Nullable private Integer timeMinutes = null;
    @Nullable private String date = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflator = requireActivity().getLayoutInflater();

        builder.setView(inflator.inflate(R.layout.dialog_create_event_material, null))
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog dialog = getDialog();
                        // get field values
                        TextInputEditText eventNameEditText = dialog.findViewById(R.id.eventNameEditText);
                        TextInputEditText facilityEditText = dialog.findViewById(R.id.facilityEditText);
                        TextInputEditText waitlistLimitEditText = dialog.findViewById(R.id.waitlistLimitEditText);
                        TextInputEditText attendeeLimitEditText = dialog.findViewById(R.id.attendeeLimitEditText);

                        String eventName = eventNameEditText.getText().toString();
                        String facilityName = facilityEditText.getText().toString();
                        String waitlistLimitStr = waitlistLimitEditText.getText().toString();
                        String attendeeLimitStr = attendeeLimitEditText.getText().toString();

                        // Validate input
                        if(eventName.isEmpty() || facilityName.isEmpty() || attendeeLimitStr.isEmpty()) {
                            Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // create event
                        Event event = new Event(eventName, facilityName, waitlistLimitStr.isEmpty() ? null : Integer.parseInt(waitlistLimitStr),
                                Integer.parseInt(attendeeLimitStr), date, timeHours, timeMinutes);

                        // add event to database if one with the same info does not already exist
                        String eventHashCode = String.format("%s", event.hashCode());
                        DocumentReference eventRef = db.collection("events").document(eventHashCode);
                        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()) {
                                        Toast.makeText(getContext(), "You have already created an event with the same information!", Toast.LENGTH_LONG).show();
                                    } else {
                                        eventRef.set(event.toHashMap());
                                    }
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // cancel
                    }
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        // Set up time picker
        MaterialTextView timeTextView = dialog.findViewById(R.id.timeTextView);
        timeTextView.setOnClickListener(v -> {
            MaterialTimePicker picker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setHour(12)
                            .setMinute(10)
                            .setTitleText("Select Event Time")
                            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                            .build();
            picker.addOnPositiveButtonClickListener(p -> {
                timeHours = picker.getHour();
                timeMinutes = picker.getMinute();
                if(timeHours == 0) {
                    timeTextView.setText(String.format("%02d:%02d AM", timeHours + 12, timeMinutes));
                }
                else if(timeHours < 12) {
                    timeTextView.setText(String.format("%02d:%02d AM", timeHours, timeMinutes));
                } else if(timeHours == 12) {
                    timeTextView.setText(String.format("%02d:%02d PM", timeHours, timeMinutes));
                } else {
                    timeTextView.setText(String.format("%02d:%02d PM", timeHours - 12, timeMinutes));
                }
            });
            picker.show(getParentFragmentManager(), "Event time picker");
        });

        // Set up date picker
        MaterialTextView dateTextView = dialog.findViewById(R.id.dateTextView);
        dateTextView.setOnClickListener(v -> {
            MaterialDatePicker<Long> picker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select Event Date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build();
            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override public void onPositiveButtonClick(Long selection) {
                    Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    utc.setTimeInMillis(selection);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    date = format.format(utc.getTime());
                    dateTextView.setText(date);
                }
            });

            picker.show(getParentFragmentManager(), "Event date picker");
        });
    }


}
