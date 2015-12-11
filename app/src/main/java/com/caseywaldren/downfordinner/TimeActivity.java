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

public class TimeActivity extends AppCompatActivity {

    ChoiceRecyclerAdapter adapter;
    public static final String UPDATE_TIME_COUNT = "com.caseywaldren.downfordinner.intent.UPDATE_TIME_COUNT";
    public static final String PLANS_CREATED = "com.caseywaldren.downfordinner.intent.PLANS_CREATED";

    private BroadcastReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        filter = new IntentFilter();
        filter.addAction(UPDATE_TIME_COUNT);
        filter.addAction(PLANS_CREATED);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(PLANS_CREATED)) {
                    Intent launchRestaurantActivity = new Intent(context, AcceptedActivity.class);
                    context.startActivity(launchRestaurantActivity);
                    ((Activity) context).finish();
                } else if (intent.getAction().equals(UPDATE_TIME_COUNT)) {
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

        setTitle("Times:");

        adapter = new ChoiceRecyclerAdapter(TimeActivity.this, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(TimeActivity.this);
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
                                Log.i("yolo", "Could not get any parseobjects from suggestions");
                            }
                        }
                    });

                } else {
                    Log.i("yolo", "could not fetch the status");
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
