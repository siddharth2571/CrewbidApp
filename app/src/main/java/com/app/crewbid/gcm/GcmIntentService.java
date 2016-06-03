package com.app.crewbid.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.app.crewbid.MainFragmentActivity;
import com.app.crewbid.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by iconflux-chuni on 7/19/2015.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " +
                //   extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                String message = intent.getExtras().getString("message");
                if (!TextUtils.isEmpty(message)) {
                    if (Build.VERSION.SDK_INT <= 15) {
                        normalNotification(GcmIntentService.this, message, message.split(":")[0].trim().toLowerCase());
                    } else {
                        createNotification(GcmIntentService.this, message, message.split(":")[0].trim().toLowerCase());
                    }

                }
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void createNotification(Context context, String message, String user_name) {
        String display_Txt = "Data String";
        String conversation_string = "";


        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent intent = new Intent(context, MainFragmentActivity.class);
        intent.putExtra("FromGcm", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);

        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name)).setContentText(conversation_string)
                .setLights(0xFFFF0000, 500, 500)
                .setContentText(display_Txt)
                .setContentIntent(pendingIntent).setAutoCancel(true)
                .addAction(0, conversation_string, null)
                .setTicker("message from " + user_name)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(display_Txt));

        Notification notification = builder.getNotification();

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify(R.drawable.ic_launcher, notification);
    }

    private void normalNotification(Context context, String message, String user_name) {
        // prepare intent which is triggered if the
// notification is selected

        Intent intent = new Intent(context, MainFragmentActivity.class);
        intent.putExtra("FromGcm", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(this)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }
}
