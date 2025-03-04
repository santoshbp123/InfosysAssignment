package com.example.infosysassignment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class MainActivity extends AppCompatActivity implements SpeedMonitor {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private float maxSpeed = 0; // Speed limit for the current user
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_rental);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the user's maximum speed from Firebase Firestore
        getUserMaxSpeedFromFirestore();

        // Start location updates to monitor speed
        startLocationUpdates();
    }

    @Override
    public void setMaxSpeed(float speed) {
        maxSpeed = speed;
    }

    @Override
    public void trackSpeed(float currentSpeed) {
        if (currentSpeed > maxSpeed) {
            // Notify the rental company via Firebase
            sendFleetNotification("Speed limit exceeded by user!");
            // Show a warning to the user
            showUserWarning();
        }
    }

    @Override
    public void sendFleetNotification(String message) {
        // Send notification to the rental company via Firebase Cloud Messaging
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder()
                .setMessageId(Integer.toString((int) System.currentTimeMillis()))
                .addData("message", message)
                .build());
    }

    @Override
    public void showUserWarning() {
        // Display a warning to the user that speed limit is exceeded
        Toast.makeText(this, "Warning! Speed limit exceeded.", Toast.LENGTH_SHORT).show();
    }

    private void getUserMaxSpeedFromFirestore() {
        // Assume the user has a document in the "users" collection
        String userId = "currentUserId"; // Replace with actual user ID
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            maxSpeed = document.getFloat("maxSpeed"); // Get max speed from Firestore
                            setMaxSpeed(maxSpeed); // Set the max speed for this user
                        }
                    }
                });
    }

    private void startLocationUpdates() {
        // Check for location permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000);  // 1 second interval
            locationRequest.setFastestInterval(500);  // 0.5 second fastest interval
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        float currentSpeed = location.getSpeed(); // Speed in meters/second
                        trackSpeed(currentSpeed); // Track speed
                    }
                }
            }, null);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

   // sending notifications via AWS
   /* public void sendFleetNotificationAWS(String message) {
        AmazonSNS snsClient = AmazonSNSClient.builder().build();
        PublishRequest publishRequest = new PublishRequest()
                .withMessage(message)
                .withTopicArn("YOUR_SNS_TOPIC_ARN");
        PublishResult result = snsClient.publish(publishRequest);
    }*/
}