package com.caseywaldren.downfordinner;

import android.util.Log;

import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Casey on 12/11/2015.
 */
public final class ParseUtils {

    public static final String CHANNEL_DINNER_REQUESTS = "DinnerRequests";
    public static final String CHANNEL_DINNER_UPDATES = "DinnerUpdates";
    public static final String CLASS_APP_STATUS = "AppStatus";
    public static final String ID_APP_STATUS = "tuD10rMajj";
    public static final String CLASS_SUGGESTIONS = "Suggestions";
    public static final String TAG_PARSE_QUERY = "PARSE_QUERY";
    private static final String BASE_INTENT_PACKAGE = "com.caseywaldren.downfordinner.intent";
    public static final String INTENT_SOMEONE_DOWN_FOR_DINNER = BASE_INTENT_PACKAGE + ".SOMEONE_IS_DOWN_FOR_DINNER";
    public static final String INTENT_SUGGESTION_ADDED = BASE_INTENT_PACKAGE + ".SUGGESTION_ADDED";
    public static final String INTENT_UPDATE_TIME_COUNT = BASE_INTENT_PACKAGE + ".UPDATE_TIME_COUNT";
    public static final String INTENT_PLANS_CREATED = BASE_INTENT_PACKAGE + ".PLANS_CREATED";
    public static final String INTENT_UPDATE_VOTE_COUNT = BASE_INTENT_PACKAGE + ".UPDATE_VOTE_COUNT";
    public static final String INTENT_BEGIN_CHOOSE_TIME = BASE_INTENT_PACKAGE + ".BEGIN_CHOOSE_TIME";
    public static final String INTENT_SOMEONE_DROPPED_OUT = BASE_INTENT_PACKAGE + ".SOMEONE_DROPPED_OUT";
    private ParseUtils() {

    }

    public static void sendParsePush(String channel, String title, String alert) throws JSONException {
        JSONObject data = new JSONObject("{\"title\": \"" + title +
                "\", \"alert\":\"" + alert +
                "\", \"user\":\"" + ParseUser.getCurrentUser().getUsername() +
                "\", \"chan\":\"" + channel + "\"}");
        Log.i("PUSH_INFO", data.getString("chan"));

        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();

    }

    public static void sendParsePush(String channel, String title, String alert, String action) throws JSONException {
        JSONObject data = new JSONObject("{\"title\": \"" + title +
                "\", \"alert\":\"" + alert +
                "\", \"user\":\"" + ParseUser.getCurrentUser().getUsername() +
                "\", \"action\":\"" + action +
                "\", \"chan\":\"" + channel + "\"}");
        Log.i("PUSH_INFO", data.getString("chan"));
        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();

    }


}
