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
    public static int sampleFreq = 44100;

    public static double i_1 = 280;
    public static double i_2 = 2620;
    public static double ɪ_1 = 360;
    public static double ɪ_2 = 2220;
    public static double e_1 = 600;
    public static double e_2 = 2060;
    public static double ɛ_1 = 500;
    public static double ɛ_2 = 2200;
    public static double æ_1 = 800;
    public static double æ_2 = 1760;
    public static double a_1 = 800;
    public static double a_2 = 1200;

    public static double ə_1 = 500;
    public static double ə_2 = 1500;
    public static double ɜ_1 = 560;
    public static double ɜ_2 = 1480;

    public static double u_1 = 320;
    public static double u_2 = 920;
    public static double ʊ_1 = 380;
    public static double ʊ_2 = 940;
    public static double o_1 = 36;
    public static double o_2 = 640;
    public static double ʌ_1 = 760;
    public static double ʌ_2 = 1320;
    public static double ɔ_1 = 480;
    public static double ɔ_2 = 760;
    public static double ɒ_1 = 560;
    public static double ɒ_2 = 920;
    public static double ɑ_1 = 740;
    public static double ɑ_2 = 1180;

    public static double User_i_1;
    public static double User_i_2;
    public static double User_ɪ_1;
    public static double User_ɪ_2;
    public static double User_e_1;
    public static double User_e_2;
    public static double User_ɛ_1;
    public static double User_ɛ_2;
    public static double User_æ_1;
    public static double User_æ_2;
    public static double User_a_1;
    public static double User_a_2;

    public static double User_ə_1;
    public static double User_ə_2;
    public static double User_ɜ_1;
    public static double User_ɜ_2;

    public static double User_u_1;
    public static double User_u_2;
    public static double User_ʊ_1;
    public static double User_ʊ_2;
    public static double User_o_1;
    public static double User_o_2;
    public static double User_ʌ_1;
    public static double User_ʌ_2;
    public static double User_ɔ_1;
    public static double User_ɔ_2;
    public static double User_ɒ_1;
    public static double User_ɒ_2;
    public static double User_ɑ_1;
    public static double User_ɑ_2;

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