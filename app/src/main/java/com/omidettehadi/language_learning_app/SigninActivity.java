package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.Manifest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignup, btnLogin, btnReset;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private String [] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // If user is logged in go to Main Activity and skip this page
        if (auth.getCurrentUser() != null) {
            Intent go_to_MainActivity = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(go_to_MainActivity);
            finish();
        }

        // Set the view now
        setContentView(R.layout.activity_signin);

        // Definition
        int permissionCheck;
        int MY_PERMISSIONS_INTERNET = 0;
        int MY_PERMISSIONS_RECORD__AUDIO = 0;
        int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmail = (EditText) findViewById(R.id.etEmail);
        inputPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnReset = (Button) findViewById(R.id.btnResetPassword);
        btnSignup = (Button) findViewById(R.id.btnSignup);


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

        // Action when Login button is pressed.
        // Check login info with database.
        // Go to Navigation Activity if successful
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
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

                progressBar.setVisibility(View.VISIBLE);

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
                                progressBar.setVisibility(View.GONE);
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
                                    Intent go_to_MainActivity = new Intent(SigninActivity.this,
                                            MainActivity.class);
                                    startActivity(go_to_MainActivity);
                                    finish();
                                }
                            }
                        });
            }
        });

        // Action when Password Reset button is pressed.
        // Go to Reset Password Activity.
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_ResetPasswordActivity = new Intent(SigninActivity.this,
                        ResetPasswordActivity.class);
                startActivity(go_to_ResetPasswordActivity);
            }
        });

        // Action when Sign up button is pressed.
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
}
