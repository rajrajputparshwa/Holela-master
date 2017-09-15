package com.holela.Firebase;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.holela.Activity.MainActivity;
import com.holela.Activity.Tabs;
import com.holela.R;
import com.holela.Utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String type;
    String postid;

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
    /*    Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
*/
        //Calling method to generate notification
      //  sendNotification(remoteMessage.getNotification().getBody());

        remoteMessage.getCollapseKey();
        sendNotification(remoteMessage.getData());

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts


    private void sendNotification(Map<String, String> messageBody) {
        Log.i("Message Body", "" + messageBody);
        String message = "", imageUrl = "";
        String type, statusStr = "";
        try {

            statusStr = messageBody.get("type");

            switch (statusStr) {
                case "1":
                    message = messageBody.get("message");
                    Log.e("Normal--->", message);
                    break;

                case "2":
                    message = messageBody.get("message");
                    Log.e("Normal--->", message);
                    break;

                case "3":
                    message = messageBody.get("message");
                    Log.e("Normal--->", message);
                    break;

                case "4":
                    message = messageBody.get("message");
                    Log.e("Normal--->", message);
                    break;

                case "5":
                    message = messageBody.get("message");
                    Log.e("Normal--->", message);
                    break;

                case "6":
                    message = messageBody.get("message");
                    Log.e("Normal--->", message);
                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "";
        }
        Log.e("hellloo", "msg aayoo ");
        Log.i("Message Body", "" + messageBody);

        Intent noti_Intent = null;
        switch (statusStr) {
            case "1":
                noti_Intent = new Intent(this, Tabs.class);
                noti_Intent.putExtra("fragmentcode", Config.Fragment_ID.MainFragment);

                noti_Intent.putExtra("scroll",2);
                noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(noti_Intent);  Open Screen automatically
                break;
            case "2":
                noti_Intent = new Intent(this, Tabs.class);
                noti_Intent.putExtra("fragmentcode", Config.Fragment_ID.MainFragment);
                noti_Intent.putExtra("scroll",2);
                noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(noti_Intent);
                break;

            case "3":
                noti_Intent = new Intent(this, Tabs.class);
                noti_Intent.putExtra("fragmentcode", Config.Fragment_ID.MainFragment);

                noti_Intent.putExtra("scroll",2);
                noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(noti_Intent);
                break;

            case "4":
                noti_Intent = new Intent(this, Tabs.class);
                noti_Intent.putExtra("fragmentcode", Config.Fragment_ID.MainFragment);

                noti_Intent.putExtra("scroll",2);
                noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(noti_Intent);
                break;

            case "5":
                noti_Intent = new Intent(this, Tabs.class);
                noti_Intent.putExtra("fragmentcode", Config.Fragment_ID.MainFragment);

                noti_Intent.putExtra("scroll",2);
                noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(noti_Intent);
                break;

            case "6":
                noti_Intent = new Intent(this, Tabs.class);
                noti_Intent.putExtra("fragmentcode", Config.Fragment_ID.MainFragment);

                noti_Intent.putExtra("scroll",2);
                noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(noti_Intent);
                break;


        }

        noti_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent intent = PendingIntent.getActivity(this, 0, noti_Intent, PendingIntent.FLAG_ONE_SHOT);

       /* if (statusStr.equals("1")) {
            noti_one(message, intent);
        } else {
            notification(message, intent, imageUrl);
        }*/
        noti_one(message,intent);
    }


    public void noti_one(String message, PendingIntent intent) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("Holela")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.text_app_icon)
                .setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.text_app_icon);
        } else {
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}