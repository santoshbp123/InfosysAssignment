package com.example.infosysassignment;

public interface SpeedMonitor {

    void setMaxSpeed(float speed); // Set the max speed limit
    void trackSpeed(float currentSpeed); // Track current speed
    void sendFleetNotification(String message); // Notify fleet if speed is exceeded
    void showUserWarning(); // Show warning to the user
}
