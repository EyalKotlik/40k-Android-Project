package com.example.wh40kapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.Set;


public class LandingPage extends AppCompatActivity {

    private EditText editText_email,editText_password;
    private TextView textView_account;
    private Button button_signIn,button_signUp, button_toBattle, button_toCreate;
    private String email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        textView_account = findViewById(R.id.textView_account);
        if (mAuth.getCurrentUser() != null) {
            textView_account.setText("Welcome, " + mAuth.getCurrentUser().getEmail());
        }
        editText_email = findViewById(R.id.editTextTextEmailAddress);
        editText_password = findViewById(R.id.editTextTextPassword);

        button_signIn = findViewById(R.id.button_signIn);
        button_signIn.setOnClickListener(v -> {
                    email = editText_email.getText().toString();
                    password = editText_password.getText().toString();
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(LandingPage.this, "Please enter an email and password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LandingPage.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                            sharedPreferences.edit().putString("email",email).apply(); //TODO: this might not be the best way to store user data, but it works for now (probably)
                            sharedPreferences.edit().putString("password",password).apply();
                            textView_account.setText("Welcome, " + email);
                        } else {
                            Toast.makeText(LandingPage.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

        button_signUp = findViewById(R.id.button_signUp);
        button_signUp.setOnClickListener(v -> {
            email = editText_email.getText().toString();
            password = editText_password.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LandingPage.this, "Please enter an email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LandingPage.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().putString("email",email).apply(); //TODO: this might not be the best way to store user data, but it works for now (probably)
                    sharedPreferences.edit().putString("password",password).apply();
                    textView_account.setText("Welcome, " + email);
                } else {
                    Toast.makeText(LandingPage.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        button_toBattle = findViewById(R.id.button_battleMode);
        button_toBattle.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(LandingPage.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(LandingPage.this, "You must be signed in to access this feature", Toast.LENGTH_SHORT).show();
            }
        });
        button_toCreate = findViewById(R.id.button_createMode);
        button_toCreate.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(LandingPage.this, CreateActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(LandingPage.this, "You must be signed in to access this feature", Toast.LENGTH_SHORT).show();
            }
        });
    }
}