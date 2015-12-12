package com.caseywaldren.downfordinner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdleActivity extends AppCompatActivity {

    @Bind(R.id.btnDownForDinner)
    Button btnDownForDinner;

    @Bind(R.id.etMinPeople)
    EditText etMinPeople;

    BroadcastReceiver receiver;
    IntentFilter filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle);
        ButterKnife.bind(this);

        filter = new IntentFilter(DinnerPushBroadcastReceiver.ACTION_YES_DINNER);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DinnerPushBroadcastReceiver.ACTION_YES_DINNER)) {
                    Intent wait = new Intent(IdleActivity.this, WaitActivity.class);
                    startActivity(wait);
                    finish();
                }
            }
        };
        if (ParseUser.getCurrentUser().getBoolean("downForDinner")) {
            Intent wait = new Intent(this, WaitActivity.class);
            startActivity(wait);
            finish();
        }

    }


    @OnClick(R.id.btnDownForDinner)
    public void downForDinner() {
        ParseUser.getCurrentUser().put("downForDinner", true);
        ParsePush.subscribeInBackground(ParseUtils.CHANNEL_DINNER_UPDATES);
        ParseUser.getCurrentUser().saveInBackground();
        String alertText = ParseUser.getCurrentUser().getUsername() + " wants dinner. Do you?";
        try {
            ParseUtils.sendParsePush(ParseUtils.CHANNEL_DINNER_REQUESTS, "Dinner Request!", alertText);
            Intent wait = new Intent(this, WaitActivity.class);
            startActivity(wait);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

}
