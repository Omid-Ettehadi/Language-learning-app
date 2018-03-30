package com.omidettehadi.language_learning_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.omidettehadi.language_learning_app.MainActivity.word;
import static com.omidettehadi.language_learning_app.MainActivity.wordoftheday;
import static com.omidettehadi.language_learning_app.MainActivity.WordHistory;
import static com.omidettehadi.language_learning_app.MainActivity.historystatus;
import static com.omidettehadi.language_learning_app.MainActivity.email;

public class ResetPasswordActivity extends AppCompatActivity {

    // ----------------------------------------------------------------------------------Declaration
    // Items
    private FirebaseAuth auth;
    private EditText inputEmail;
    private Button btnResetPassword;


    // ------------------------------------------------------------------------------------On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // Set the view now
        setContentView(R.layout.activity_reset_password);

        // Definitions
        inputEmail = findViewById(R.id.etEmail);

        btnResetPassword = findViewById(R.id.btnResetPassword);

        // ----------------------------------------------------------------------------------Buttons
        // See if Reset Password button is pressed.
        // Send an email with details on how to reset their password.
        // Go to Login Activity if successful
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString().trim();

                // Check if email address is entered.
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this,
                                            "We have sent you instructions to reset your password!",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this,
                                            "Failed to send reset email!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}