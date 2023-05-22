package com.example.wh40kapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class CreateActivity extends AppCompatActivity {

    private Spinner spinner_typeChoice,spinner_creativeActionChoice;
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
        spinner_creativeActionChoice = findViewById(R.id.spinner_creativeActionChoice);
        button_confirmCreate = findViewById(R.id.button_confirmCreate);
        button_confirmCreate.setOnClickListener(v -> {
            String typeChoice = spinner_typeChoice.getSelectedItem().toString();
            String creativeActionChoice = spinner_creativeActionChoice.getSelectedItem().toString();

            //TODO: start activity for result
            if( creativeActionChoice.equals("Add")){
                if (typeChoice.equals("Model")){
                    Intent intent = new Intent(this, CreateModel.class);
                    startActivity(intent);
                } else if (typeChoice.equals("Wargear")){
                    Intent intent = new Intent(this, CreateWargear.class);
                    startActivity(intent);
                } else if (typeChoice.equals("Profile")){
                    Intent intent = new Intent(this, CreateWargearProfile.class);
                    startActivity(intent);
                }

            }
        });
        button_returnToLandingPage = findViewById(R.id.button_returnToLandingPage);
        button_returnToLandingPage.setOnClickListener(v -> {
            Intent intent = new Intent(this, LandingPage.class);
            startActivity(intent);
        });
    }
}