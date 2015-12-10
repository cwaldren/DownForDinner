package com.caseywaldren.downfordinner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.login_progress)
    ProgressBar login_progress;

    @Bind(R.id.etUserName)
    EditText etUserName;

    @Bind(R.id.etPassword)
    EditText etPassword;

    @Bind(R.id.btnRegister)
    Button btnRegister;

    @Bind(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }
    }


    @OnClick(R.id.btnRegister)
    public void registerUser() {
        ParseUser user = new ParseUser();
        user.setUsername(etUserName.getText().toString());
        user.setPassword(etPassword.getText().toString());
        try {
            user.setPassword(calcHash(etPassword.getText().toString()));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        login_progress.setVisibility(View.VISIBLE);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                login_progress.setVisibility(View.GONE);

                if (e == null) {
                    Toast.makeText(LoginActivity.this, "Registration OK", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Registration failed:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @OnClick(R.id.btnLogin)
    public void loginUser() {

        login_progress.setVisibility(View.VISIBLE);
        String password = "";
        try {
            password = calcHash(etPassword.getText().toString());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ParseUser.logInInBackground(etUserName.getText().toString(), password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                login_progress.setVisibility(View.GONE);
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                    Intent intentStartMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intentStartMainActivity);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private String calcHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(password.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return Arrays.toString(digest);
    }


}
