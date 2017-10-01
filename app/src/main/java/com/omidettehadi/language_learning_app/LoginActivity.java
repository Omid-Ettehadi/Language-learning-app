package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Definition of Items
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView tvRegisterNow = (TextView) findViewById(R.id.tvRegisterHere);

        // Performs when someone presses Register Now Button
        tvRegisterNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Creates an intent to ogo to Register Activity
                Intent tvRegisterNowIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(tvRegisterNowIntent);
            }
        });

        // Performs when someone presses Login Button
        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
    }
}