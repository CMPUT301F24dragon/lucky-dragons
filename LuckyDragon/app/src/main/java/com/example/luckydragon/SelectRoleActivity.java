package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;
import java.util.Objects;

/**
 * Notes:
 * This activity gets the user profile from the database and passes it to the profile activity.
 * This will allow profile information in ProfileActivity to be displayed immediately, rather than with a delay.
 * ProfileActivity itself shouldn't need database access, but sub-fragments like EntrantProfileFragment and OrganizerProfileFragment will.
 * All users should be shown Entrant and Organizer roles, but the Admin button will only show for users that were set to Administrator in the database.
 * If a user has no profile in the database they should be taken to SignupActivity to make one.
 * After making one, we could bring them back to this page to select their role.
 */


public class SelectRoleActivity extends AppCompatActivity {
    private User user;
    private SelectRoleController selectRoleController;
    private SelectRoleView selectRoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.select_role_page);
        super.onCreate(savedInstanceState);

        // Get user
        user = ((GlobalApp) getApplication()).getUser();
        selectRoleView = new SelectRoleView(user, this);
        user.addObserver(selectRoleView);
        selectRoleController = new SelectRoleController(user);

        // Set controller if testing
//        Intent intent = getIntent();
//        SelectRoleController passedInController = (SelectRoleController) intent.getSerializableExtra("controller");
//        if (passedInController == null) {
//            selectRoleController = new SelectRoleController(user);
//        } else {
//            selectRoleController = passedInController;
//            passedInController.setObservable(user);
//        }
    }

    public void initializeView() {
        // Set content view (don't do this until after user has been fetched from db)
        setContentView(R.layout.select_role_page); // set content to role page

        // Set up on entrant click listener
        Button entrantButton = findViewById(R.id.entrantButton);
        if (!entrantButton.hasOnClickListeners()) {
            entrantButton.setOnClickListener(v -> {
                // Convert user to entrant
                user = new Entrant(user);

                if (user.isEntrant()) {
                    // Create profile intent
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    profileIntent.putExtra("role", "ENTRANT");
                    // Start profile activity
                    startActivity(profileIntent);
                } else {
                    // Send to entrant signup
                    Intent signupIntent = new Intent(this, SignupActivity.class);
                    startActivity(signupIntent);
                }
            });
        }

        // Set up on organizer click listener
        Button organizerButton = findViewById(R.id.organizerButton);
        if (!organizerButton.hasOnClickListeners()) {
            organizerButton.setOnClickListener(v -> {
                if (user.isOrganizer()) {
                    // Create profile intent
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    // Convert user to organizer and pass into intent
                    profileIntent.putExtra("role", "ORGANIZER");
                    // Start profile activity
                    startActivity(profileIntent);
                } else {
                    // TODO: Send to organizer signup
                }
            });
        }
    }

    public void hideAdminButton() {
        Button adminButton = findViewById(R.id.adminButton);
        adminButton.setVisibility(View.GONE);
    }
}