package com.caseywaldren.downfordinner;

import android.content.BroadcastReceiver;
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
    protected static final String STATUS_OBJECT_ID = "tuD10rMajj";
    private static final String APP_STATUS = "AppStatus";
    private static final String SUGGESTIONS = "Suggestions";
    private static final String PARSE_QUERY_TAG = "PARSE_QUERY";

    protected ChoiceRecyclerAdapter adapter;
    protected BroadcastReceiver receiver;
    protected IntentFilter filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter = new IntentFilter();
    }

    protected void addInitialChoices() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(APP_STATUS);
        query.getInBackground(STATUS_OBJECT_ID, new GetCallback<ParseObject>() {
            public void done(final ParseObject status, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery(SUGGESTIONS);
                    query1.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                adapter.addInitialChoices(status, objects);
                            } else {
                                Log.e(PARSE_QUERY_TAG, "Failed to fetch suggestions");
                            }
                        }
                    });
                } else {
                    Log.e(PARSE_QUERY_TAG, "Failed to fetch status");
                }
            }
        });
    }

    protected void updateChoices() {
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(SUGGESTIONS);
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    adapter.updateChoices(objects);
                } else {
                    Log.e(PARSE_QUERY_TAG, "Failed to download suggestions");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
        updateChoices();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
