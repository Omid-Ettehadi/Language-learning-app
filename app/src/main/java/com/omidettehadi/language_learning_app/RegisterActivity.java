package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Definition of Items
        final EditText etFName = (EditText) findViewById(R.id.etFName);
        final EditText etLName = (EditText) findViewById(R.id.etLName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        // Performs when someone presses Register Button
        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Creates an intent to go to Main Activity
                Intent Welcome = new Intent(RegisterActivity.this,MainActivity.class);
                RegisterActivity.this.startActivity(Welcome);
            }
        });
    }
}