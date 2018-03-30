package com.omidettehadi.language_learning_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    }
}