package com.caseywaldren.downfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.OnClick;

public class InitialResponseActivity extends AppCompatActivity {

    @Bind(R.id.btnYes)
    Button btnYes;

    @Bind(R.id.btnNo)
    Button btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_response);
    }

    @OnClick(R.id.btnYes)
    public void respondYes() {
        ParseUser.getCurrentUser().put("downForDinner", true);
        notifyGroup();
    }

    @OnClick(R.id.btnNo)
    public void respondNo() {
        ParseUser.getCurrentUser().put("downForDinner", false);
        notifyGroup();
    }

    public void notifyGroup(){

    }
}
