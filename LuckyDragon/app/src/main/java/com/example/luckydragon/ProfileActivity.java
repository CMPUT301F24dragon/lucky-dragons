package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Map;

public class ProfileActivity extends AppBarActivity {
    enum Mode {
        ENTRANT,
        ORGANIZER,
        ADMIN
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Mode mode;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        // Get android device id
        // Reference: https://stackoverflow.com/questions/60503568/best-possible-way-to-get-device-id-in-android
        String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        // Check if user exists in database
        DocumentReference docRef = db.collection("users").document(deviceID);
        // Create user object
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if(userDocument.exists()) {
                        Map<String, Object> userData = userDocument.getData();
                        if(userData != null) {
                            Boolean isAdministrator = (Boolean) userData.get("Administrator");
                            Boolean isOrganizer = (Boolean) userData.get("Organizer");
                            Boolean isEntrant = (Boolean) userData.get("Entrant");

                            if(Boolean.TRUE.equals(isAdministrator)) {
                                // Set administrator
                            } else if(Boolean.TRUE.equals(isOrganizer)) {
                                // Set organizer
                                mode = Mode.ORGANIZER;
                                user = new Organizer(
                                        deviceID,
                                        String.format("%s %s", userData.get("FirstName"), userData.get("LastName")),
                                        String.format("%s", userData.get("Email")),
                                        String.format("%s", userData.get("PhoneNumber"))
                                );
                            } else if(Boolean.TRUE.equals(isEntrant)) {
                                // Set entrant
                            } else {
                                throw new RuntimeException("User must be administrator, organizer, or entrant. They are none of the three.");
                            }
                        } else {
                            throw new RuntimeException("User has no data.");
                        }
                    }
                } else {
                    throw new RuntimeException("Database read failed.");
                }

                // Set profile info views
                TextView nameView = findViewById(R.id.nameTextView);
                TextView emailView = findViewById(R.id.emailTextView);
                TextView phoneNumberView = findViewById(R.id.phoneNumberTextView);
                nameView.setText(user.getName());
                emailView.setText(user.getEmail());
                phoneNumberView.setText(user.getPhoneNumber());

                // Create profile fragment
                if(mode == Mode.ADMIN) {
                    // Create admin profile fragment
                } else if(mode == Mode.ORGANIZER) {
                    // Create organizer profile fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fragment_container_view, OrganizerProfileFragment.class, null)
                            .commit();
                } else if(mode == Mode.ENTRANT) {
                    // Create entrant profile fragment
                } else {
                    throw new RuntimeException("User mode not set.");
                }
            }
        });
    }
}
