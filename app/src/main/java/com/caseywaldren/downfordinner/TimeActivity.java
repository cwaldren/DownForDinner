package com.caseywaldren.downfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        setTitle("Choose times that work for you:");
    }
}
