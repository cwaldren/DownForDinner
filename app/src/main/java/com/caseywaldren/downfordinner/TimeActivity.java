package com.caseywaldren.downfordinner;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

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
}
