package com.caseywaldren.downfordinner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.caseywaldren.downfordinner.adapter.ChoiceRecyclerAdapter;

public class TimeActivity extends RecyclerActivity {


    public static final String UPDATE_TIME_COUNT = "com.caseywaldren.downfordinner.intent.UPDATE_TIME_COUNT";
    public static final String PLANS_CREATED = "com.caseywaldren.downfordinner.intent.PLANS_CREATED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        setTitle("Times:");

        // First we have a filter which tells us to update all the time votes
        filter.addAction(UPDATE_TIME_COUNT);

        // Then we have a filter which tells us to move on to the next activity
        filter.addAction(PLANS_CREATED);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case PLANS_CREATED:
                        Intent launchRestaurantActivity = new Intent(context, AcceptedActivity.class);
                        context.startActivity(launchRestaurantActivity);
                        ((Activity) context).finish();
                        break;
                    case UPDATE_TIME_COUNT:
                        updateChoices();
                        break;
                }
            }
        };


        adapter = new ChoiceRecyclerAdapter(TimeActivity.this, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(TimeActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        addInitialChoices();


    }



}
