package com.example.wh40kapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wh40kapp.R;
import com.example.wh40kapp.data.CompressedProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateWargearProfile extends AppCompatActivity {
    private EditText editText_wargearProfileName, editText_range, editText_type,
            editText_S, editText_AP, editText_D, editText_attacks;
    private Button button_discard, button_confirmCreateWargearProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wargear_profile);
        editText_wargearProfileName = findViewById(R.id.editText_wargearProfileName2);
        editText_range = findViewById(R.id.editTextNumber_range);
        editText_type = findViewById(R.id.editText_wargearProfileType);
        editText_S = findViewById(R.id.editTextText_profileS);
        editText_AP = findViewById(R.id.editTextNumberSigned_AP);
        editText_D = findViewById(R.id.editTextText_profileD);
        editText_attacks = findViewById(R.id.editText_wargearProfileAttacks);

        button_discard = findViewById(R.id.button_discard2);
        button_confirmCreateWargearProfile = findViewById(R.id.button_saveWargearProfile);

        button_discard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        button_confirmCreateWargearProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText_wargearProfileName.getText().toString().trim()) ||
                        TextUtils.isEmpty(editText_range.getText().toString().trim()) ||
                        TextUtils.isEmpty(editText_type.getText().toString().trim()) ||
                        TextUtils.isEmpty(editText_S.getText().toString().trim()) ||
                        TextUtils.isEmpty(editText_AP.getText().toString().trim()) ||
                        TextUtils.isEmpty(editText_D.getText().toString().trim()) ||
                        TextUtils.isEmpty(editText_attacks.getText().toString().trim())
                ) {
                    Toast.makeText(CreateWargearProfile.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = editText_wargearProfileName.getText().toString().toLowerCase().trim();
                int range = Integer.parseInt(editText_range.getText().toString().toLowerCase().trim());
                String type = editText_type.getText().toString().toLowerCase().trim();
                if (type.equals("melee"))
                    range = -1;
                String S = editText_S.getText().toString().toLowerCase().trim();
                if (!S.matches("^user$|^[+-]?\\d+$")) {
                    Toast.makeText(CreateWargearProfile.this, "Invalid S, it should be either 'user' or a signed integer", Toast.LENGTH_SHORT).show();
                    return;
                }
                int AP = Integer.parseInt(editText_AP.getText().toString().toLowerCase().trim());
                AP = Math.min(AP, 0);
                String D = editText_D.getText().toString().toLowerCase().trim();
                if (!D.matches("^\\d+[d](3|6)[+-]\\d+$")) {
                    Toast.makeText(CreateWargearProfile.this, "Invalid D, it should be XdX+-X", Toast.LENGTH_SHORT).show();
                    return;
                }
                String attacks = editText_attacks.getText().toString().toLowerCase().trim();
                if (!attacks.matches("^\\d+[d](3|6)[+-]\\d+$")) {
                    Toast.makeText(CreateWargearProfile.this, "Invalid attacks, it should be XdX+-X", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] completeType = new String[]{type, attacks};


                CompressedProfile newProfile = new CompressedProfile(
                        0,
                        0,
                        range, AP, 0,
                        name, S, D, completeType
                );
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String userID = mAuth.getCurrentUser().getUid();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Log.d("TAG", "CHECK: Add model to database");
                mDatabase.child(userID).child("profiles").child(newProfile.getName()).setValue(newProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateWargearProfile.this, "Profile created", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "CHECK: wargear created");
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateWargearProfile.this, "Failed to create profile", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "CHECK: failed to create wargear");
                    }
                });
                finish();
            }
        });
    }
}