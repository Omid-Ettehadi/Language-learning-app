package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import static com.omidettehadi.language_learning_app.MainActivity.word;
import static com.omidettehadi.language_learning_app.MainActivity.wordoftheday;
import static com.omidettehadi.language_learning_app.MainActivity.WordHistory;
import static com.omidettehadi.language_learning_app.MainActivity.historystatus;
import static com.omidettehadi.language_learning_app.MainActivity.email;

public class SigninActivity extends AppCompatActivity {

    // ----------------------------------------------------------------------------------Declaration
    // Items
    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword;
    private Button btnSignup, btnLogin, btnReset;

    // --------------------------------------------------------------------------------Global Values
    // Permission
    private int permissionCheck;
    private int MY_PERMISSIONS_INTERNET = 0;
    private int MY_PERMISSIONS_RECORD__AUDIO = 0;
    private int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0;

    // Random Word Generator
    private Random RandomGen;
    private int wordofthedayscore;
    private String wordbeg, wordend;
    private String[] catalogue = {"a","b","c","d","e","f","g",
            "h","i","j","k","l","m","n","o","p",
            "q","r","s","t","u","v","w","x","y","z"};


    // ------------------------------------------------------------------------------------On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // If user is logged in go to Navigation activity and skip this page
        if (auth.getCurrentUser() != null) {
            Intent go_to_NavigationActivity = new
                    Intent(SigninActivity.this, NavigationActivity.class);
            startActivity(go_to_NavigationActivity);
            finish();
        }

        // Set the view now
        setContentView(R.layout.activity_signin);

        // Definitions
        inputEmail = findViewById(R.id.etEmail);
        inputPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnReset = findViewById(R.id.btnResetPassword);
        btnSignup = findViewById(R.id.btnSignup);

        // Check for all permissions
        permissions();

        // Random Word Generator for Word of the day
        RandomGen = new Random();
        int index = RandomGen.nextInt(catalogue.length);
        wordbeg = catalogue[index];
        index = RandomGen.nextInt(catalogue.length);
        wordend = catalogue[index];
        new SigninActivity.CallbackTask().execute(randomwordgeneratorEntries());

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = this.getSharedPreferences ("PREFS",0);
        int lastDay = settings.getInt("day",0);

        if(lastDay != currentDay){
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day",currentDay);
            editor.commit();

            RandomGen = new Random();
            index = RandomGen.nextInt(catalogue.length);
            wordbeg = catalogue[index];
            index = RandomGen.nextInt(catalogue.length);
            wordend = catalogue[index];
            new SigninActivity.CallbackTask().execute(randomwordgeneratorEntries());
        }

        // ----------------------------------------------------------------------------------Buttons
        // See if Login Button is pressed
        // Check login info with database.
        // Go to Navigation Activity if successful
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                // Check if email address is entered.
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if password is entered.
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authenticate user's information
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SigninActivity.this,
                                new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user.
                                // If sign in succeeds, the auth state listener will be notified
                                // and logic to handle the signed in user can be handled in the
                                // listener.
                                if (!task.isSuccessful()) {
                                    // Password is short
                                    if (password.length() < 6) {
                                        inputPassword.setError(
                                                getString(R.string.minimum_password));
                                    }
                                    else {
                                        Toast.makeText(SigninActivity.this,
                                                getString(R.string.auth_failed),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {

                                    Intent go_to_NavigationActivity = new
                                            Intent(SigninActivity.this,
                                            NavigationActivity.class);
                                    startActivity(go_to_NavigationActivity);
                                    finish();
                                }
                            }
                        });
            }
        });


        // See if Password Reset Button is pressed
        // Go to Reset Password Activity.
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_ResetPasswordActivity = new Intent(SigninActivity.this,
                        ResetPasswordActivity.class);
                startActivity(go_to_ResetPasswordActivity);
            }
        });

        // See if Signed up Button is pressed
        // Go to Sign up Activity.
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_SignupActivity = new Intent(SigninActivity.this,
                        SignupActivity.class);
                startActivity(go_to_SignupActivity);
            }
        });
    }

    // ----------------------------------------------------------------------------------Permissions
    private void permissions (){
        // See if all permissions have been granted.
        // else request for the permissions.
        permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_INTERNET);
        }
        permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_RECORD__AUDIO);
        }
        permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }
    }

    // -------------------------------------------------------------Random Word of the Day Generator
    private String randomwordgeneratorEntries() {
        return "http://api.datamuse.com/words?sp=" + wordbeg + "*" + wordend;
    }

    class CallbackTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String line = null;
            StringBuilder stringBuilder = null;
            URL url = null;
            URLConnection urlConnection;

            try {
                url = new URL(strings[0]);
                urlConnection = url.openConnection();

                // read the output from the server
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                stringBuilder = new StringBuilder();

                while ((line = reader.readLine()) != null)
                    stringBuilder.append(line);

                reader.close();
            } catch (MalformedURLException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder != null ? stringBuilder.toString() : null;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONArray resultsarray = new JSONArray(result);
                Random r = new Random();
                wordofthedayscore = 1;
                int i = 0;
                while (i < 50) {
                    int randomnumber = r.nextInt(resultsarray.length() + 1);
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject e = resultsarray.getJSONObject(randomnumber);
                    wordoftheday = e.getString("word");
                    String score = e.getString("score");
                    wordofthedayscore = Integer.parseInt(score);
                    if (wordofthedayscore > 1500) {
                        break;
                    }
                }
                // Word of teh Day Hard Coded
                wordoftheday = "Opportunity";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
