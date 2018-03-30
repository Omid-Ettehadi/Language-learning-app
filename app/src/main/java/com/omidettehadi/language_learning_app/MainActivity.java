package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity {

    // ----------------------------------------------------------------------------World Wide Values
    public static String word, wordoftheday;
    public static String[] WordHistory = new String[]{};
    public static boolean historystatus = true;
    public static String email;

    // ------------------------------------------------------------------------------------On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set default font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"tradegothicltstdlight.otf",true);

        // Go to Signin Activity
        Intent go_to_SigninActivity = new
                Intent(MainActivity.this,
                SigninActivity.class);
        startActivity(go_to_SigninActivity);
    }
}