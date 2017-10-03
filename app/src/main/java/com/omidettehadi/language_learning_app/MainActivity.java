package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an intent to go to Login Activity
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(login);

    }
}