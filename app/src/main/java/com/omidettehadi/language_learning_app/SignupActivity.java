package com.omidettehadi.language_learning_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignin, btnSignup, btnResetPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // Set the view now
        setContentView(R.layout.activity_signup);

        // Definition
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmail = (EditText) findViewById(R.id.etEmail);
        inputPassword = (EditText) findViewById(R.id.etPassword);
        btnSignup = (Button) findViewById(R.id.btnRegister);
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);
        btnSignin = (Button) findViewById(R.id.btnSignin);

        // Action when Register button is pressed.
        // Create an account in the database.
        // Go to Main Activity if successful
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

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
                // Check if password length is acceptable.
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Create a new user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this,
                                        "createUserWithEmail:onComplete:" + task.isSuccessful(),
                                        Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);

                                // If sign in succeeds the auth state listener will be notified and
                                // logic to handle the user can be handled in the listener.
                                // If sign in fails, display a message to the user.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this,
                                            "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Intent go_to_MainActivity = new Intent(SignupActivity.this,
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
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_ResetPasswordActivity = new Intent(SignupActivity.this,
                        ResetPasswordActivity.class);
                startActivity(go_to_ResetPasswordActivity);
            }
        });

        // Action when Sign in button is pressed.
        // Finish activity to go back to Signin Activity.
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}