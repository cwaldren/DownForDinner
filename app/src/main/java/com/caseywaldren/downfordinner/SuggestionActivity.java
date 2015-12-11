package com.caseywaldren.downfordinner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuggestionActivity extends AppCompatActivity {

    @Bind(R.id.btnSubmit)
    Button btnSubmit;

    @Bind(R.id.etRestaurant)
    EditText etRestaurant;

    @Bind(R.id.etTime)
    EditText etTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnSubmit)
    public void submitSuggestions() {
        if("".equals(etRestaurant.getText().toString())) {
            etRestaurant.setError("Please enter a restaurant");
        } else if("".equals(etTime.getText().toString())) {
            etTime.setError("Please enter a time");
        } else {

            uploadSuggestion();
            notifyUsers();
            startChoiceActivity();
        }

    }

    public void notifyUsers() {
        //Send push to update data set
    }

    public void uploadSuggestion() {
        ParseObject suggestion = new ParseObject("Suggestions");
        suggestion.put("restaurant", etRestaurant.getText().toString());
        suggestion.put("restaurantVotes", 1);
        suggestion.put("time", etTime.getText().toString());
        suggestion.put("timeVotes", 1);
        suggestion.saveInBackground();
    }

    public void startChoiceActivity() {
        Intent intentStartChoiceActivity = new Intent(SuggestionActivity.this, ChoiceActivity.class);
        startActivity(intentStartChoiceActivity);
    }
}
