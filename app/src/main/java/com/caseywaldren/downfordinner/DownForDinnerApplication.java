package com.caseywaldren.downfordinner;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by Casey on 12/10/2015.
 */
public class DownForDinnerApplication extends Application {

    public static final String APP_ID =
            "5xN8tLyH92vrTgrzDX44CH1v1NAfN9qt0l2kW4wI";
    public static final String CLIENT_ID =
            "o4VP01eOEJtQloPodH0QSV4koUVjGFLDI4KR8VuQ";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, APP_ID, CLIENT_ID);

        ParseInstallation.getCurrentInstallation().saveInBackground();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        //  ParsePush.subscribeInBackground("DownForDinner");

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }
}
