package com.caseywaldren.downfordinner;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

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
                ParsePush.subscribeInBackground("DinnerUpdates");
                ParseUser.getCurrentUser().put("downForDinner", true);
                ParsePush push = new ParsePush();
                String alertText = ParseUser.getCurrentUser().getUsername() + " is down. ";
                try {
                    JSONObject data = new JSONObject("{\"title\": \"Dinner Update\", \"alert\":\"" + alertText + "\" }");
                    push.setChannel("DinnerUpdates");
                    push.setData(data);
                    push.sendInBackground();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                context.sendBroadcast(new Intent(WaitActivity.SOMEONE_IS_DOWN_FOR_DINNER));

                break;
        }

        ParseUser.getCurrentUser().saveInBackground();
        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.cancel(intent.getExtras().getInt(DinnerPushBroadcastReceiver.KEY_NOTIFICATION_ID));
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));




    }
}
