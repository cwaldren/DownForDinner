package com.caseywaldren.downfordinner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.caseywaldren.downfordinner.adapter.ChoiceRecyclerAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ChoiceActivity extends AppCompatActivity {

    public static final String UPDATE_VOTE_COUNT = "com.caseywaldren.downfordinner.intent.UPDATE_VOTE_COUNT";
    public static final String BEGIN_CHOOSE_TIME = "com.caseywaldren.downfordinner.intent.BEGIN_CHOOSE_TIME";

    ChoiceRecyclerAdapter adapter;

    private BroadcastReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        filter = new IntentFilter();
        filter.addAction(UPDATE_VOTE_COUNT);
        filter.addAction(BEGIN_CHOOSE_TIME);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BEGIN_CHOOSE_TIME)) {
                    Intent launchTimeChoiceActivity = new Intent(context, TimeActivity.class);
                    context.startActivity(launchTimeChoiceActivity);
                    ((Activity) context).finish();
                } else if (intent.getAction().equals(UPDATE_VOTE_COUNT)) {


                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Suggestions");
                    query1.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                adapter.updateChoices(objects);
                            } else {
                                Log.i("yolo", "failed");
                            }
                        }
                    });
                }
            }
        };
        setTitle("Choices: ");

        adapter = new ChoiceRecyclerAdapter(ChoiceActivity.this, true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(ChoiceActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("AppStatus");
        query.getInBackground("tuD10rMajj", new GetCallback<ParseObject>() {
            public void done(final ParseObject status, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Suggestions");
                    query1.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                adapter.addInitialChoices(status, objects);
                            } else {
                                Log.i("yolo", "failed");
                            }
                        }
                    });
                } else {
                    Log.i("yolo", "failed");
                }
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
        //implement checking server to see if should move on
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


}
