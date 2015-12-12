package com.caseywaldren.downfordinner;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Casey on 12/10/2015.
 */
public class DinnerPushBroadcastReceiver extends ParsePushBroadcastReceiver {

    public static final String KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID";
    public static final String ACTION_YES_DINNER = "com.caseywaldren.intent.YES_DINNER";
    public static final String ACTION_NO_DINNER = "com.caseywaldren.intent.NO_DINNER";

    private JSONObject getPushData(Intent intent) {
        try {
            return new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
        } catch (JSONException e) {
            Log.e(ParseUtils.TAG_PARSE_QUERY, "Unexpected JSONException when receiving push data: ", e);
            return null;
        }
    }

    protected Notification getUpdateNotification(Context context, Intent intent) {
        JSONObject pushData = this.getPushData(intent);
        if (pushData == null) {
            return null;
        }

        // Title will be something like "Dinner Update", while alert is "Bob wants dinner"
        String title = pushData.optString("title", "Dinner Update");
        String alert = pushData.optString("alert", "Someone changed their choice.");

        //Required for Parse  to route to pushReceive
        Intent contentIntent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_OPEN);
        contentIntent.putExtras(intent.getExtras());
        contentIntent.setPackage(context.getPackageName());

        PendingIntent pContentIntent = PendingIntent.getBroadcast(context, 0,
                contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder parseBuilder = new NotificationCompat.Builder(context);

        parseBuilder.setContentTitle(title)
                .setContentText(alert)
                .setTicker(alert)
                .setSmallIcon(this.getSmallIconId(context, intent))
                .setLargeIcon(this.getLargeIcon(context, intent))
                .setContentIntent(pContentIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);

        return parseBuilder.build();

    }
    protected NotificationCompat.Builder getMyNotification(Context context, Intent intent) {
        JSONObject pushData = this.getPushData(intent);
        if (pushData == null) {
            return null;
        }

        String title = pushData.optString("title", "Dinner Notification");
        String alert = pushData.optString("alert", "Respond with your choice.");
        String tickerText = alert;

        Bundle extras = intent.getExtras();

        Random random = new Random();
        int contentIntentRequestCode = random.nextInt();
        int deleteIntentRequestCode = random.nextInt();

        // Security consideration: To protect the app from tampering, we require that intent filters
        // not be exported. To protect the app from information leaks, we restrict the packages which
        // may intercept the push intents.
        String packageName = context.getPackageName();


        Intent contentIntent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_OPEN);
        contentIntent.putExtras(extras);
        contentIntent.setPackage(packageName);


        Intent deleteIntent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_DELETE);
        deleteIntent.putExtras(extras);
        deleteIntent.setPackage(packageName);

        PendingIntent pContentIntent = PendingIntent.getBroadcast(context, contentIntentRequestCode,
                contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pDeleteIntent = PendingIntent.getBroadcast(context, deleteIntentRequestCode,
                deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // The purpose of setDefaults(Notification.DEFAULT_ALL) is to inherit notification properties
        // from system defaults
        NotificationCompat.Builder parseBuilder = new NotificationCompat.Builder(context);
        parseBuilder.setContentTitle(title)
                .setContentText(alert)
                .setTicker(tickerText)
                .setSmallIcon(this.getSmallIconId(context, intent))
                .setLargeIcon(this.getLargeIcon(context, intent))
                .setContentIntent(pContentIntent)
                .setDeleteIntent(pDeleteIntent)
                .setAutoCancel(true)
                        // #0
                .setDefaults(Notification.DEFAULT_ALL);

        return parseBuilder;
    }

    @Override
    public void onPushReceive(Context context, Intent intent) {
        //    super.onPushReceive(context, intent);
        JSONObject pushData = null;
        try {
            pushData = new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
        } catch (JSONException e) {
            //PLog.e(TAG, "Unexpected JSONException when receiving push data: ", e);
        }

        // If the push data includes an action string, that broadcast intent is fired.
        String action = null;
        if (pushData != null) {
            action = pushData.optString("action", null);
        }
        if (action != null) {
            Log.i("PushBroadcastRec", "Going to broadcast " + action);

            Bundle extras = intent.getExtras();
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtras(extras);
            broadcastIntent.setAction(action);
            broadcastIntent.setPackage(context.getPackageName());
            context.sendBroadcast(broadcastIntent);
        }
        Random random = new Random();
        try {
            Log.i("PUSH_CONTENTS", pushData.getString("chan"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (pushData.optString("user").equals(ParseUser.getCurrentUser().getUsername())) {
            Log.i("PUSH_REC", "Got a self push");
            return;
        }
        if (pushData.optString("chan").equals(ParseUtils.CHANNEL_DINNER_UPDATES)) {
            Log.i("PUSH_REC", "Got a dinner plan update");
            NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mgr.notify(45555332, getUpdateNotification(context, intent));
            return;
        }



        String packageName = context.getPackageName();

        int id = 124151412;
        Intent yesIntent = new Intent(DinnerPushBroadcastReceiver.ACTION_YES_DINNER);
        yesIntent.putExtras(intent.getExtras());
        yesIntent.setPackage(packageName);
        yesIntent.putExtra(KEY_NOTIFICATION_ID, id);

        PendingIntent pendingYesIntent = PendingIntent.getBroadcast(context, random.nextInt(), yesIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent noIntent = new Intent(DinnerPushBroadcastReceiver.ACTION_NO_DINNER);
        noIntent.putExtras(intent.getExtras());
        noIntent.putExtra(KEY_NOTIFICATION_ID, id);
        noIntent.setPackage(packageName);

        PendingIntent pendingNoIntent = PendingIntent.getBroadcast(context, random.nextInt(), noIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = getMyNotification(context, intent);
        builder.addAction(R.drawable.ic_clear_white_24dp, "NO", pendingNoIntent)
                .addAction(R.drawable.ic_check_white_24dp, "YES", pendingYesIntent);

        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.notify(id, builder.build());

    }


    @Override
    public void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }


    @Override
    public Class<? extends Activity> getActivity(Context context, Intent intent) {
        JSONObject pushData = this.getPushData(intent);
        assert pushData != null;
        if (pushData.optString("title").equals("Dinner Request!")) {
            return InitialResponseActivity.class;
        } else {
            try {
                String action = pushData.getString("action");
                switch (action) {
                    case ParseUtils.INTENT_SOMEONE_DOWN_FOR_DINNER:
                        return WaitActivity.class;
                    case ParseUtils.INTENT_BEGIN_CHOOSE_TIME:
                        return TimeActivity.class;
                    case ParseUtils.INTENT_PLANS_CREATED:
                        return AcceptedActivity.class;
                    case ParseUtils.INTENT_UPDATE_TIME_COUNT:
                        return TimeActivity.class;
                    case ParseUtils.INTENT_UPDATE_VOTE_COUNT:
                        return ChoiceActivity.class;
                    default:
                        return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return IdleActivity.class;
    }

}
