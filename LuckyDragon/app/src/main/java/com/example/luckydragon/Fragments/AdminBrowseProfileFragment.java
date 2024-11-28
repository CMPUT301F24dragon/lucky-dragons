package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.AdminBrowseProfileController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.BrowseProfileView;
import com.example.luckydragon.Views.ViewProfilesView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

/**
 * A fragment used by administrators to browse user profiles. This
 * fragment provides a UI to view user details and it interacts with
 * {@link AdminBrowseProfileController} to perform administrator actions
 * such as deleting an user.
 */
public class AdminBrowseProfileFragment extends Fragment {

    private User user;
    private AdminBrowseProfileController userController;
    private Button adminRemoveFacility;
    private LinearLayout adminBrowseFacilityContainer;
    private MaterialTextView adminFacilityTextView;

    public AdminBrowseProfileFragment() {
        super(R.layout.fragment_admin_browse_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        GlobalApp globalApp = ((GlobalApp) requireActivity().getApplication());
        user = globalApp.getUserToView();

        adminRemoveFacility = view.findViewById(R.id.adminRemoveFacilityButton);
        adminFacilityTextView = view.findViewById(R.id.adminFacilityTextView);
        adminBrowseFacilityContainer = view.findViewById(R.id.adminBrowseFacilityContainer);

        // Create controller
        userController = new AdminBrowseProfileController(user);
        // Set up delete profile button on click listener
        Button adminDeleteProfileButton = view.findViewById(R.id.adminDeleteProfileButton);
        adminDeleteProfileButton.setOnClickListener(v -> {
            if (Objects.equals(user.getDeviceId(), globalApp.getUser().getDeviceId())) {
                Toast.makeText(getContext(), "Cannot delete your own profile", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
            else {
                userController.deleteUser();
                Toast.makeText(getContext(), "Profile Deleted Successfully", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });

        // Set up remove profile picture button on click listener
        Button adminRemoveProfilePictureButton = view.findViewById(R.id.adminRemoveProfilePictureButton);
        adminRemoveProfilePictureButton.setOnClickListener(v -> {
            // Check if the user being viewed is the current logged in user
            if (Objects.equals(user.getDeviceId(), globalApp.getUser().getDeviceId())) {
                Toast.makeText(getContext(), "Cannot remove your own picture, go to edit profile to remove picture", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
            else {
                // Cannot remove profile picture when there is no uploaded profile picture
                if (user.getUploadedProfilePicture() == null) {
                    Toast.makeText(getContext(), "User has default profile picture, cannot remove default picture", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                }
                else {
                    // Remove profile picture
                    userController.removeProfilePicture();
                    Toast.makeText(getContext(), "Profile picture removed successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                }
            }
        });
        adminRemoveFacility.setOnClickListener(v -> {
            userController.removeFacility();
            Toast.makeText(getContext(), "Facility removed and all events associated with it", Toast.LENGTH_SHORT).show();
            requireActivity().finish();

        });

        // Now start observing user
        new BrowseProfileView(user, this);
    }

    /**
     * Sets the visibility of the facility info
     * @param visibility the visibility state
     */
    public void setFacilityContainerVisibility(int visibility) {
        adminBrowseFacilityContainer.setVisibility(visibility);
    }

    /**
     * Sets the visibility of the facility remove button
     * @param visibility the visibility state
     */
    public void setRemoveFacilityButtonVisiblity(int visibility) {
        adminRemoveFacility.setVisibility(visibility);
    }

    /**
     * Sets the admin facility text view to the facility name.
     * @param facilityName The name of the facility.
     */
    public void setFacilityName(String facilityName) {
        adminFacilityTextView.setText(facilityName);
    }
}
