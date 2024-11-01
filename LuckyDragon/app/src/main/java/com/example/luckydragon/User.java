/**
 * Defines the User model class.
 */

package com.example.luckydragon;

import android.util.Log;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a User object.
 * <p>
 * Issues:
 *   - This is only a basic implementation. Additional functionality should be added as needed.
 *   - Email and phone number may be optional. Additional constructors should be defined for these cases.
 */
public class User extends Observable implements Serializable {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String deviceID;
    private String name;
    private String email;
    private String phoneNumber;

    private String facility;

    private Boolean isEntrant = Boolean.FALSE;
    private Boolean isOrganizer = Boolean.FALSE;
    private Boolean isAdmin = Boolean.FALSE;

    /**
     * Creates a User object with empty string values and a deviceID.
     * @param deviceID: the user's device ID
     */
    public User(String deviceID) {
        this.deviceID = deviceID;
        this.name = "";
        this.email = "";
        this.phoneNumber = "";
        db
                .collection("users")
                .document(deviceID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Load user data
                        Map<String, Object> userData = documentSnapshot.getData();
                        assert userData != null;
                        setData(userData);
                    } else {
                        // Create a new document for this user
                        db.collection("users")
                                .document(deviceID)
                                .set(deviceID);
                    }
                    notifyObservers();
                });
    }

    /**
     * Creates a User object based on an existing instance
     * @param user: the user
     */
    public User(User user) {
        this.deviceID = user.getDeviceID();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.isEntrant = user.isEntrant();
        this.isOrganizer = user.isOrganizer();
        this.isAdmin = user.isAdmin();
    }

    /**
     * Creates a User object.
     * @param deviceID: the user's device ID
     * @param name: the user's name
     * @param email: the user's email
     * @param phoneNumber: the user's phone number
     */
    public User(String deviceID, String name, String email, String phoneNumber) {
        this.deviceID = deviceID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Set user data using a map from firestore
     * @param userData: the firestore map
     */
    public void setData(Map<String, Object> userData) {
        email = String.format("%s", userData.get("email"));
        name = String.format("%s", userData.get("name"));
        phoneNumber = String.format("%s", userData.get("phoneNumber"));

        facility = String.format("%s", userData.get("facility"));

        isEntrant = userData.get("isEntrant") != null
                && userData.get("isEntrant").toString().equals("true");
        isOrganizer = userData.get("isOrganizer") != null
                && userData.get("isOrganizer").toString().equals("true");
        isAdmin = userData.get("isAdmin") != null
                && userData.get("isAdmin").toString().equals("true");

        notifyObservers();
    }

    /**
     * Return user data for saving to firestore
     * @return hash map for saving to firestore
     */
    public HashMap<String, Object> getUserData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isEntrant", isEntrant);
        map.put("isOrganizer", isOrganizer);
        map.put("isAdmin", isAdmin);

        map.put("facility", facility);

        map.put("name", name);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        return map;
    }

    // TODO: Implement, send error messages
    public Boolean isValid() {
        return !name.isEmpty();
    }

    /**
     * Get the user's device ID.
     * @return user's device ID as String
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * Get the user's name.
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's email.
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's phone number.
     * @return the user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the user's name.
     * @param name: the user's new name
     */
    public void setName(String name) {
        this.name = name;
        notifyObservers();
    }

    /**
     * Set the user's email.
     * @param email: the user's new email
     */
    public void setEmail(String email) {
        this.email = email;
        notifyObservers();
    }

    /**
     * Set the user's phone number.
     * @param phoneNumber: the user's new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyObservers();
    }

    /**
     * Gets the facility name for the organizer.
     * @return the organizer's facility name
     */
    public String getFacility() {
        if (!isOrganizer) {
            throw new RuntimeException("Not an organizer!");
        }
        return facility;
    }

    /**
     * Sets the facility name for the organizer.
     * @param facility: the new facility name
     */
    public void setFacility(String facility) {
        if (!isOrganizer) {
            throw new RuntimeException("Not an organizer!");
        }
        this.facility = facility;
    }

    public Boolean isEntrant() {
        return isEntrant;
    }

    public void setEntrant(Boolean entrant) {
        isEntrant = entrant;
    }

    public Boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(Boolean organizer) {
        isOrganizer = organizer;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}