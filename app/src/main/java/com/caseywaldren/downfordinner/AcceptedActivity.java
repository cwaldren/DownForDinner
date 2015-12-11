package com.caseywaldren.downfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AcceptedActivity extends AppCompatActivity {

    @Bind(R.id.tvRestaurantName)
    TextView tvRestaurantName;

    @Bind(R.id.tvTime)
    TextView tvTime;

    @Bind(R.id.btnReset)
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted);

        setTitle("Accepted");

        ButterKnife.bind(this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("AppStatus");
        query.getInBackground("tuD10rMajj", new GetCallback<ParseObject>() {
            public void done(final ParseObject status, ParseException e) {
                if (e == null) {

                    tvRestaurantName.setText(status.getString("restaurant"));
                    tvTime.setText(status.getString("time"));

                } else {
                    Log.i("yolo", "failed");
                }
            }
        });
    }


    @OnClick(R.id.btnReset)
    public void resetApp() {

        //delete all suggestions

        //restore app to original state

    }
}
