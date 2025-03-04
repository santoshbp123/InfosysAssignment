package com.example.infosysassignment;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle the FCM message
        String message = remoteMessage.getData().get("message");
        // Process the notification (e.g., show a notification or log the message)
    }
}
