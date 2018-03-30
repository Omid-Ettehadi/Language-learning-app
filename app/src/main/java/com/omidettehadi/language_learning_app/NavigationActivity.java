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

        // Go to Dictionary Fragment
        setTitle("Dictionary");
        DictionaryFragment fragment = new DictionaryFragment();
        FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
        fragmenttransaction.replace(R.id.main,fragment,"Dictionary");
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
