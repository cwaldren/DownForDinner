package com.caseywaldren.downfordinner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdleActivity extends AppCompatActivity {

    @Bind(R.id.btnDownForDinner)
    Button btnDownForDinner;

    @Bind(R.id.etMinPeople)
    EditText etMinPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle);
        ButterKnife.bind(this);

        if (ParseUser.getCurrentUser().getBoolean("downForDinner")) {
            Intent wait = new Intent(this, WaitActivity.class);
            startActivity(wait);
            finish();
        }

    }


    @OnClick(R.id.btnDownForDinner)
    public void downForDinner() {
        int minPeople = 2;
        try {
            minPeople = Integer.parseInt(etMinPeople.getText().toString());
        } catch (NumberFormatException e) {
            Log.i("IDLE_ACTIVITY", "Couldn't parse min people number, therefore using default");
        }
        ParseUser.getCurrentUser().put("downForDinner", true);
        ParsePush.subscribeInBackground("DinnerUpdates");
        ParseUser.getCurrentUser().saveInBackground();
        ParsePush push = new ParsePush();
        String alertText = ParseUser.getCurrentUser().getUsername() + " wants dinner. Do you?";
        try {
            JSONObject data = new JSONObject("{\"title\": \"Dinner Request!\", \"alert\":\"" + alertText + "\" }");
            push.setChannel("DinnerRequests");
            push.setData(data);
            push.sendInBackground();

            Intent wait = new Intent(this, WaitActivity.class);
            startActivity(wait);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
