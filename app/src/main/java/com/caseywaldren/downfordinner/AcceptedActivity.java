package com.caseywaldren.downfordinner;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AcceptedActivity extends AppCompatActivity {

    @Bind(R.id.tvRestaurantName)
    TextView tvRestaurantName;

    @Bind(R.id.tvTime)
    TextView tvTime;

    @Bind(R.id.btnReset)
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted);

        setTitle("Accepted");

        ButterKnife.bind(this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseUtils.CLASS_APP_STATUS);
        query.getInBackground(ParseUtils.ID_APP_STATUS, new GetCallback<ParseObject>() {
            public void done(final ParseObject status, ParseException e) {
                if (e == null) {
                    tvRestaurantName.setText(status.getString("restaurant"));
                    tvTime.setText(status.getString("time"));
                } else {
                    Log.i(ParseUtils.TAG_PARSE_QUERY, "Failed to fetch status");
                }
            }
        });
        ParsePush.unsubscribeInBackground(ParseUtils.CHANNEL_DINNER_UPDATES);
        ParseUser.getCurrentUser().put("downForDinner", false);
        ParseUser.getCurrentUser().saveInBackground();

        NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.cancel(45555332);

    }


    @OnClick(R.id.btnReset)
    public void resetApp() {

        //delete all suggestions

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Suggestions");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        objects.get(i).deleteInBackground();
                    }
                } else {
                    Log.i(ParseUtils.TAG_PARSE_QUERY, "failed to retrieve objects");
                }
            }
        });


        Intent beginIdleActivity = new Intent(this, IdleActivity.class);
        startActivity(beginIdleActivity);
        finish();

    }
}
