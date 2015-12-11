package com.caseywaldren.downfordinner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

public class InitialResponseActivity extends AppCompatActivity {

    @Bind(R.id.btnYes)
    Button btnYes;

    @Bind(R.id.btnNo)
    Button btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_response);
    }

    @OnClick(R.id.btnYes)
    public void respondYes() {
        ParseUser.getCurrentUser().put("downForDinner", true);
        ParsePush.subscribeInBackground("DinnerUpdates");
        ParsePush push = new ParsePush();
        String alertText = ParseUser.getCurrentUser().getUsername() + " is down. ";
        try {
            JSONObject data = new JSONObject("{\"title\": \"Dinner Update\", \"alert\":\"" + alertText + "\",  \"action\":\"com.caseywaldren.downfordinner.intent.SOMEONE_IS_DOWN_FOR_DINNER\" }");
            push.setChannel("DinnerUpdates");
            push.setData(data);
            push.sendInBackground();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent wait = new Intent(this, WaitActivity.class);
        startActivity(wait);
        finish();
    }

    @OnClick(R.id.btnNo)
    public void respondNo() {
        ParseUser.getCurrentUser().put("downForDinner", false);
        finish();
    }

    public void notifyGroup(){

    }
}
