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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        setTitle("Times:");

        // First we have a filter which tells us to update all the time votes
        filter.addAction(ParseUtils.INTENT_UPDATE_TIME_COUNT);

        // Then we have a filter which tells us to move on to the next activity
        filter.addAction(ParseUtils.INTENT_PLANS_CREATED);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ParseUtils.INTENT_PLANS_CREATED:
                        Intent launchRestaurantActivity = new Intent(context, AcceptedActivity.class);
                        context.startActivity(launchRestaurantActivity);
                        ((Activity) context).finish();
                        break;
                    case ParseUtils.INTENT_UPDATE_TIME_COUNT:
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
