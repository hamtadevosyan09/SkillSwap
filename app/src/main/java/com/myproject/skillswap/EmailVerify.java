package com.myproject.skillswap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerify extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            toastEmailOpen();
            sendVerificationEmailIfNeeded();
            checkEmailVerification();
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void toastEmailOpen() {
        Toast.makeText(EmailVerify.this, R.string.if_open_email, Toast.LENGTH_SHORT).show();
    }

    private void sendVerificationEmailIfNeeded() {
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailVerify.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EmailVerify.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void checkEmailVerification() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    if (currentUser.isEmailVerified()) {
                        Toast.makeText(EmailVerify.this, "Email is verified", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EmailVerify.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(EmailVerify.this, "Email is not verified yet. Please check your inbox.", Toast.LENGTH_SHORT).show();
                        checkEmailVerification();
                    }
                } else {
                    Toast.makeText(EmailVerify.this, "User is not logged in", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}

