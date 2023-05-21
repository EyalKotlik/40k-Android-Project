package com.example.wh40kapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class CreateActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Button button_returnToLandingPage;

=======

public class CreateActivity extends AppCompatActivity {

>>>>>>> parent of f2dad8e (1 idea for createActivity)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
<<<<<<< HEAD
        tabLayout = findViewById(R.id.tab_layout_create);
        button_returnToLandingPage = findViewById(R.id.button_returnToLandingPage2);
        button_returnToLandingPage.setOnClickListener(v -> {
            Intent intent = new Intent(this, LandingPage.class);
            startActivity(intent);
        });
=======
>>>>>>> parent of f2dad8e (1 idea for createActivity)
    }
}