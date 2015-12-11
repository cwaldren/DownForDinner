package com.caseywaldren.downfordinner;

import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Casey on 12/11/2015.
 */
public final class ParseUtils {

    private ParseUtils() {

    }

    private static final String BASE_INTENT_PACKAGE = "com.caseywaldren.downfordinner.intent";

    public static final String CHANNEL_DINNER_REQUESTS = "DinnerRequests";
    public static final String CHANNEL_DINNER_UPDATES = "DinnerUpdates";
    public static final String CLASS_APP_STATUS = "AppStatus";
    public static final String ID_APP_STATUS = "tuD10rMajj";
    public static final String CLASS_SUGGESTIONS = "Suggestions";
    public static final String TAG_PARSE_QUERY = "PARSE_QUERY";

    public static final String INTENT_SOMEONE_DOWN_FOR_DINNER = BASE_INTENT_PACKAGE + ".SOMEONE_IS_DOWN_FOR_DINNER";
    public static final String INTENT_SUGGESTION_ADDED = BASE_INTENT_PACKAGE + ".SUGGESTION_ADDED";

    public static void sendParsePush(String channel, String title, String alert) throws JSONException {
        JSONObject data = new JSONObject("{\"title\": \"" + title +
                "\", \"alert\":\"" + alert +
                "\", \"user\":\"" + ParseUser.getCurrentUser().getUsername() + "\" }");
        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();

    }

    public static void sendParsePush(String channel, String title, String alert, String action) throws JSONException {
        JSONObject data = new JSONObject("{\"title\": \"" + title +
                "\", \"alert\":\"" + alert +
                "\", \"user\":\"" + ParseUser.getCurrentUser().getUsername() +
                "\", \"action\":\"" + action + "\" }");
        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();

    }


}
