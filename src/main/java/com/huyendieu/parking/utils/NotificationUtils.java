package com.huyendieu.parking.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huyendieu.parking.model.notification.NotificationModel;

public class NotificationUtils {

    public static void sendNotification(String path, NotificationModel data) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(path);
        String pathChild = ref.push().getKey();
        ref.child(pathChild).setValueAsync(data);
    }

    public static void removeNotification(String path) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(path);
        ref.removeValueAsync();
    }

    public static void updateNotification(String path, String pathChild, Object data) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(path);
        ref.child(pathChild).setValueAsync(data);
    }
}
