package com.caseywaldren.downfordinner;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseUser;

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
                ParseUser.getCurrentUser().put("downForDinner", true);

                break;
        }

        ParseUser.getCurrentUser().saveInBackground();
        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.cancel(intent.getExtras().getInt(DinnerPushBroadcastReceiver.KEY_NOTIFICATION_ID));
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));


    }
}
