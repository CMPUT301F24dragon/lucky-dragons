package com.example.luckydragon;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Database {
    public interface GetUserCallback {
        public void setProfileInfoAndCreateFragment(Map<String, Object> userData);
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private GetUserCallback getUserCallback;

    public Database(GetUserCallback callback) {
        getUserCallback = callback;
    }


    public void getUser(String deviceID) {
        DocumentReference docRef = db.collection("users").document(deviceID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if (userDocument.exists()) {
                        Map<String, Object> userData = userDocument.getData();
                        userData.put("DeviceID", deviceID);
                        getUserCallback.setProfileInfoAndCreateFragment(userData);
                    } else {
                        //throw new RuntimeException("User does not exist!");
                    }
                } else {
                    //throw new RuntimeException("Database read failed.");
                }
            }
        });
    }
}
