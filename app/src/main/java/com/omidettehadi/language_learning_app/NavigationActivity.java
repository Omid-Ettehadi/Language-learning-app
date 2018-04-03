package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.omidettehadi.language_learning_app.MainActivity.User_a_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_a_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_e_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_e_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_i_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_i_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_o_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_o_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_u_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_u_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_æ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_æ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɑ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɑ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɒ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɒ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɔ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɔ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ə_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ə_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɛ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɛ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɜ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɜ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɪ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ɪ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ʊ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ʊ_2;
import static com.omidettehadi.language_learning_app.MainActivity.User_ʌ_1;
import static com.omidettehadi.language_learning_app.MainActivity.User_ʌ_2;
import static com.omidettehadi.language_learning_app.MainActivity.a_1;
import static com.omidettehadi.language_learning_app.MainActivity.a_2;
import static com.omidettehadi.language_learning_app.MainActivity.e_1;
import static com.omidettehadi.language_learning_app.MainActivity.e_2;
import static com.omidettehadi.language_learning_app.MainActivity.i_1;
import static com.omidettehadi.language_learning_app.MainActivity.i_2;
import static com.omidettehadi.language_learning_app.MainActivity.o_1;
import static com.omidettehadi.language_learning_app.MainActivity.o_2;
import static com.omidettehadi.language_learning_app.MainActivity.sampleFreq;
import static com.omidettehadi.language_learning_app.MainActivity.u_1;
import static com.omidettehadi.language_learning_app.MainActivity.u_2;
import static com.omidettehadi.language_learning_app.MainActivity.æ_1;
import static com.omidettehadi.language_learning_app.MainActivity.æ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɑ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɑ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɒ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɒ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɔ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɔ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ə_1;
import static com.omidettehadi.language_learning_app.MainActivity.ə_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɛ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɛ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɜ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɜ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ɪ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ɪ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ʊ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ʊ_2;
import static com.omidettehadi.language_learning_app.MainActivity.ʌ_1;
import static com.omidettehadi.language_learning_app.MainActivity.ʌ_2;
import static com.omidettehadi.language_learning_app.MainActivity.i_1;
import static com.omidettehadi.language_learning_app.MainActivity.word;
import static com.omidettehadi.language_learning_app.MainActivity.wordoftheday;
import static com.omidettehadi.language_learning_app.MainActivity.WordHistory;
import static com.omidettehadi.language_learning_app.MainActivity.historystatus;
import static com.omidettehadi.language_learning_app.MainActivity.email;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // ----------------------------------------------------------------------------------Declaration
    // Items
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView tvEmail;


    // ------------------------------------------------------------------------------------On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // Get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Check if a change in User Authentication occurs
        // if Yes, fgo to Signin Activity
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // user auth state is changed - user is null
                if (user == null) {
                    startActivity(
                            new Intent(NavigationActivity.this, SigninActivity.class));
                    finish();
                }
            }
        };

        // Set the view now
        setContentView(R.layout.activity_navigation);

        // Set default font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"tradegothicltstdlight.otf",true);

        // Definitions
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationview = findViewById(R.id.nav_view);
        View headerview = navigationview.getHeaderView(0);
        tvEmail = headerview.findViewById(R.id.tvEmail);

        // Set data for the Drawer
        email = user.getEmail();
        tvEmail.setText(email);


        // Set up the Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Go to Set Up Fragment
        setTitle("Set up");
        SetUpFreqFragment fragment = new SetUpFreqFragment();
        FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
        fragmenttransaction.replace(R.id.main,fragment,"Set Up");
        fragmenttransaction.commit();
    }

    // ---------------------------------------------------------------------------------Back Pressed
    // If Drawer is opened, close it
    // else go to Dictionary Fragment
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            setTitle("Dictionary");
            DictionaryFragment fragment = new DictionaryFragment();
            FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main,fragment,"Dictionary");
            fragmenttransaction.commit();
        }
    }

    // ---------------------------------------------------------------------------------Options Menu
    // Generate the option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    // If Sign Out Button is pressed, sign out and close the application.
    // If Clear History Button is pressed, set historystatus to true and go to Dictionary Fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            auth.signOut();
            finish();
            Intent go_to_SigninActivity = new
                    Intent(NavigationActivity.this,
                    SigninActivity.class);
            startActivity(go_to_SigninActivity);
            return true;
        }
        if (id == R.id.action_clearhistory) {
            historystatus = true;
            setTitle("Dictionary");
            DictionaryFragment fragment = new DictionaryFragment();
            FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main,fragment,"Dictionary");
            fragmenttransaction.commit();
            return true;
        }
        if (id == R.id.action_setup) {
            setTitle("Set up");
            SetUpFreqFragment fragment = new SetUpFreqFragment();
            FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main,fragment,"Set Up");
            fragmenttransaction.commit();
            return true;
        }
        if (id == R.id.action_default) {
            User_i_1 = i_1;
            User_i_2 = i_2;
            User_ɪ_1 = ɪ_1;
            User_ɪ_2 = ɪ_2;
            User_e_1 = e_1;
            User_e_2 = e_2;
            User_ɛ_1 = ɛ_1;
            User_ɛ_2 = ɛ_2;
            User_æ_1 = æ_1;
            User_æ_2 = æ_2;
            User_a_1 = a_1;
            User_a_2 = a_2;

            User_ə_1 = ə_1;
            User_ə_2 = ə_2;
            User_ɜ_1 = ɜ_1;
            User_ɜ_2 = ɜ_2;

            User_u_1 = u_1;
            User_u_2 = u_2;
            User_ʊ_1 = ʊ_1;
            User_ʊ_2 = ʊ_2;
            User_o_1 = o_1;
            User_o_2 = o_1;
            User_ʌ_1 = ʌ_1;
            User_ʌ_2 = ʌ_2;
            User_ɔ_1 = ɔ_1;
            User_ɔ_2 = ɔ_2;
            User_ɒ_1 = ɒ_1;
            User_ɒ_2 = ɒ_2;
            User_ɑ_1 = ɑ_1;
            User_ɑ_2 = ɑ_2;

            setTitle("Dictionary");
            DictionaryFragment fragment = new DictionaryFragment();
            FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main,fragment,"Dictionary");
            fragmenttransaction.commit();
            
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ---------------------------------------------------------------------------------------Drawer
    // Create drawer and if any is pressed, go to their specified Fragment
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dictionary) {
            // the dictionary action
            setTitle("Dictionary");
            DictionaryFragment fragment = new DictionaryFragment();
            FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main,fragment,"Dictionary");
            fragmenttransaction.commit();
        } else if (id == R.id.learn) {
            // the learn action
            setTitle("Learn");
            LearnFragment fragment = new LearnFragment();
            FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main,fragment,"Learn");
            fragmenttransaction.commit();
        } else if (id == R.id.ipavowels){
            // the IPAVowels action
            setTitle("IPA Vowels");
            IPAVowelsFragment fragment = new IPAVowelsFragment();
            FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main,fragment,"IPA Vowels");
            fragmenttransaction.commit();
        } else if (id == R.id.wordoftheday) {
            // the wordoftheday action
            setTitle("Word of The Day");
            WordoftheDayFragment fragment = new WordoftheDayFragment();
            FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
            fragmenttransaction.replace(R.id.main, fragment, "Word of The Day");
            fragmenttransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
