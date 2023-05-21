package com.example.wh40kapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class CreateActivity extends AppCompatActivity {

    private Spinner spinner_typeChoice,spinner_creativeActionChoice;
    private Button button_confirmCreate;
    private FirebaseAuth mAuth;
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
        });
    }
}