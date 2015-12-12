package com.caseywaldren.downfordinner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.caseywaldren.downfordinner.adapter.ChoiceRecyclerAdapter;

public class ChoiceActivity extends RecyclerActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        setTitle("Choices: ");


        // First we have a filter which we use to update every object's vote count
        filter.addAction(ParseUtils.INTENT_UPDATE_VOTE_COUNT);

        // Then we have a filter which tells us to move on to the next activity
        filter.addAction(ParseUtils.INTENT_BEGIN_CHOOSE_TIME);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ParseUtils.INTENT_BEGIN_CHOOSE_TIME:
                        Intent launchTimeChoiceActivity = new Intent(context, TimeActivity.class);
                        context.startActivity(launchTimeChoiceActivity);
                        ((Activity) context).finish();
                        break;
                    case ParseUtils.INTENT_UPDATE_VOTE_COUNT:
                        updateChoices();
                        break;
                }
            }
        };

        adapter = new ChoiceRecyclerAdapter(ChoiceActivity.this, true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(ChoiceActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        addInitialChoices();
    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
