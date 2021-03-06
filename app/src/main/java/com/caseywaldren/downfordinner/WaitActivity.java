package com.caseywaldren.downfordinner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WaitActivity extends AppCompatActivity {



    @Bind(R.id.tvResponses)
    TextView tvResponses;

    @Bind(R.id.btnCancelDinner)
    Button btnCancelDinner;

    private BroadcastReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        ButterKnife.bind(this);
        tvResponses.setText(String.format(getResources().getString(R.string.x_out_of_y_commited), 0, 2));

        getSupportActionBar().hide();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                fetchPeopleWhoAreReady();
            }
        };

        filter = new IntentFilter();
        filter.addAction(ParseUtils.INTENT_SOMEONE_DOWN_FOR_DINNER);
        filter.addAction(ParseUtils.INTENT_SOMEONE_DROPPED_OUT);

        fetchPeopleWhoAreReady();

    }

    @OnClick(R.id.btnCancelDinner)
    public void clickCancel() {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("downForDinner", false);
        user.saveInBackground();

        String alertText = ParseUser.getCurrentUser().getUsername() + " dropped out. ";
        try {
            ParseUtils.sendParsePush(ParseUtils.CHANNEL_DINNER_UPDATES, "Dinner Update", alertText, ParseUtils.INTENT_SOMEONE_DROPPED_OUT);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        finish();
    }

    private void fetchPeopleWhoAreReady() {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("downForDinner", true);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> peopleList, ParseException e) {
                if (e == null) {
                    Resources res = getResources();
                    if (peopleList.size() == 2) {
                        Intent suggestions = new Intent(WaitActivity.this, SuggestionActivity.class);
                        startActivity(suggestions);
                        finish();
                    }
                    String text = String.format(res.getString(R.string.x_out_of_y_commited), peopleList.size(), 2);
                    tvResponses.setText(text);
                    Log.i("WAIT_UPDATE", "There are " + peopleList.size());
                } else {
                    Log.e("PARSE_QUERY", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
        fetchPeopleWhoAreReady();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
