package com.caseywaldren.downfordinner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.caseywaldren.downfordinner.adapter.ChoiceRecyclerAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Casey on 12/11/2015.
 */
public class RecyclerActivity extends AppCompatActivity {

    protected ChoiceRecyclerAdapter adapter;
    protected BroadcastReceiver receiver;
    protected BroadcastReceiver updateReceiver;

    protected IntentFilter filter;
    protected IntentFilter updateFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter = new IntentFilter();
        updateFilter = new IntentFilter(ParseUtils.INTENT_SUGGESTION_ADDED);
        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ParseUtils.INTENT_SUGGESTION_ADDED:
                        updateChoices();
                        break;
                }
            }
        };
    }

    protected void addInitialChoices() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseUtils.CLASS_APP_STATUS);
        query.getInBackground(ParseUtils.ID_APP_STATUS, new GetCallback<ParseObject>() {
            public void done(final ParseObject status, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery(ParseUtils.CLASS_SUGGESTIONS);
                    query1.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                adapter.addInitialChoices(status, objects);
                            } else {
                                Log.e(ParseUtils.TAG_PARSE_QUERY, "Failed to fetch suggestions");
                            }
                        }
                    });
                } else {
                    Log.e(ParseUtils.TAG_PARSE_QUERY, "Failed to fetch status");
                }
            }
        });
    }

    protected void updateChoices() {
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(ParseUtils.CLASS_SUGGESTIONS);
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    adapter.updateChoices(objects);
                } else {
                    Log.e(ParseUtils.TAG_PARSE_QUERY, "Failed to download suggestions");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
        registerReceiver(updateReceiver, updateFilter);
        updateChoices();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(updateReceiver);
        unregisterReceiver(receiver);
    }
}
