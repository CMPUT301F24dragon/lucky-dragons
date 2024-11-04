package com.example.luckydragon;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupController extends Controller {
    public SignupController(User observable) {
        super(observable);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    public void extractName(EditText editName) {
        // TODO: input validation
        String name = editName.getText().toString();
        getObservable().setName(name);
    }

    public void extractEmail(EditText editEmail) {
        // TODO: input validation
        String email = editEmail.getText().toString();
        getObservable().setEmail(email);
    }

    public void extractPhoneNumber(EditText editPhone) {
        // TODO: input validation
        String phoneNumber = editPhone.getText().toString();
        getObservable().setPhoneNumber(phoneNumber);
    }

    public void setNotifications(SwitchMaterial switchNotifications) {
        getObservable().setNotifications(switchNotifications.isChecked());
    }

    public void setProfilePicture(Bitmap image) {
        getObservable().uploadProfilePicture(image);
    }

    public void becomeEntrant() {
        getObservable().setEntrant(true);
    }
}
