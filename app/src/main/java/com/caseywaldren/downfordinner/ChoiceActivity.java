package com.caseywaldren.downfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.caseywaldren.downfordinner.adapter.ChoiceRecyclerAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ChoiceActivity extends AppCompatActivity {

    ChoiceRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        setTitle("Vote for one or more restaurant:");

        adapter = new ChoiceRecyclerAdapter(ChoiceActivity.this, true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(ChoiceActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Suggestions");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    adapter.addInitialChoices(objects);
                } else {
                    Log.i("yolo", "failed");
                }
            }
        });

    }


}
