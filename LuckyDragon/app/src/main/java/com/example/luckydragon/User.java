package com.example.luckydragon;

public class User {
    private String deviceID;
    private String name;
    private String email;
    private String phoneNumber;

    public String getDeviceID() {
        return deviceID;
    }

    public User(String deviceID) {
        this.deviceID = deviceID;
    }

    public User(String deviceID, String name, String email) {
        this.deviceID = deviceID;
        this.name = name;
        this.email = email;
    }

    public User(String deviceID, String name, String email, String phoneNumber) {
        this.deviceID = deviceID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
