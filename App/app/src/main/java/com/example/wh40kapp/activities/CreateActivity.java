package com.example.wh40kapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import com.example.wh40kapp.R;
import com.example.wh40kapp.data.Model;
import com.example.wh40kapp.data.Wargear;
import com.example.wh40kapp.data.WargearProfile;
import com.google.firebase.auth.FirebaseAuth;

public class CreateActivity extends AppCompatActivity {

    private Spinner spinner_typeChoice;
    private Button button_confirmCreate, button_returnToLandingPage;
    private FirebaseAuth mAuth;
    private Model mResult;
    private Wargear wResult;
    private WargearProfile pResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        spinner_typeChoice = findViewById(R.id.spinner_typeChoice);
        button_confirmCreate = findViewById(R.id.button_confirmCreate);
        button_confirmCreate.setOnClickListener(v -> {
            String typeChoice = spinner_typeChoice.getSelectedItem().toString();


            if (typeChoice.equals("Model")) {
                Intent intent = new Intent(this, CreateModel.class);
                startActivity(intent);
            } else if (typeChoice.equals("Wargear")) {
                Intent intent = new Intent(this, CreateWargear.class);
                startActivity(intent);
            } else if (typeChoice.equals("Profile")) {
                Intent intent = new Intent(this, CreateWargearProfile.class);
                startActivity(intent);

            }
        });
        button_returnToLandingPage = findViewById(R.id.button_returnToLandingPage);
        button_returnToLandingPage.setOnClickListener(v -> {
            Intent intent = new Intent(this, LandingPage.class);
            startActivity(intent);
        });
    }
}