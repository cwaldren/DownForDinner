package com.caseywaldren.downfordinner;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONException;

/**
 * Created by Casey on 12/10/2015.
 */
public class DinnerRequestBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        switch (intentAction) {
            case DinnerPushBroadcastReceiver.ACTION_NO_DINNER:
                Log.i("DINNER_RESPONSE", "NO DINNER!");
                ParseUser.getCurrentUser().put("downForDinner", false);
                break;
            case DinnerPushBroadcastReceiver.ACTION_YES_DINNER:
                Log.i("DINNER_RESPONSE", "YES DINNER!");
                ParsePush.subscribeInBackground(ParseUtils.CHANNEL_DINNER_UPDATES);
                ParseUser.getCurrentUser().put("downForDinner", true);
                String alertText = ParseUser.getCurrentUser().getUsername() + " is down. ";
                try {
                    ParseUtils.sendParsePush(ParseUtils.CHANNEL_DINNER_UPDATES, "Dinner Update", alertText, ParseUtils.INTENT_SOMEONE_DOWN_FOR_DINNER);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

        ParseUser.getCurrentUser().saveInBackground();
        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.cancel(intent.getExtras().getInt(DinnerPushBroadcastReceiver.KEY_NOTIFICATION_ID));
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));




    }
}
