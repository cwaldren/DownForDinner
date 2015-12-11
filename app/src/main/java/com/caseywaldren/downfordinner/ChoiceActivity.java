package com.caseywaldren.downfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.caseywaldren.downfordinner.adapter.ChoiceRecyclerAdapter;

public class ChoiceActivity extends AppCompatActivity {

    final ChoiceRecyclerAdapter adapter = new ChoiceRecyclerAdapter(this, true, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        setTitle("Vote for one or more restaurant:");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
