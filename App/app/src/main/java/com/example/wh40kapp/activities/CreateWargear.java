package com.example.wh40kapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wh40kapp.R;
import com.example.wh40kapp.adapters.WargearProfileRecyclerViewAdapter;
import com.example.wh40kapp.data.CompressedProfile;
import com.example.wh40kapp.data.CompressedWargear;
import com.example.wh40kapp.data.WargearProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateWargear extends AppCompatActivity {
    private EditText editText_wargearName, editText_wargearProfileName;
    private Button button_discard, button_confirmCreateWargear, button_addWargearProfile;
    private Spinner spinner_wargearProfileActionChoice;
    private RecyclerView recyclerView_wargearProfileList;
    private ArrayList<WargearProfile> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wargear);
        editText_wargearName = findViewById(R.id.editText_wargearName2);
        editText_wargearProfileName = findViewById(R.id.editText_wargearProfileName);
        button_addWargearProfile = findViewById(R.id.button_confirm4);
        button_discard = findViewById(R.id.button_discard3);
        button_confirmCreateWargear = findViewById(R.id.button_saveWargear);
        recyclerView_wargearProfileList = findViewById(R.id.recyclerView_newWargearProfiles);
        spinner_wargearProfileActionChoice = findViewById(R.id.spinner_actionChoice4);
        items = new ArrayList<WargearProfile>();
        WargearProfileRecyclerViewAdapter adapter = new WargearProfileRecyclerViewAdapter(items);
        recyclerView_wargearProfileList.setAdapter(adapter);
        recyclerView_wargearProfileList.setLayoutManager(new LinearLayoutManager(this));

        button_addWargearProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (spinner_wargearProfileActionChoice.getSelectedItem().toString().equals("Remove")) {
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getName().equals(editText_wargearProfileName.getText().toString().toLowerCase().trim())) {
                            items.remove(i);
                            adapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                } else if (spinner_wargearProfileActionChoice.getSelectedItem().toString().equals("Add custom")) {
                    Log.d("Adding Custom Wargear Profile", "onClick: " + editText_wargearProfileName.getText().toString().toLowerCase().trim());
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("profiles");
                    mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            Log.d("Adding Custom Profile", "onComplete: ");
                            if (task.isSuccessful()) {
                                DataSnapshot dataSnapshot = task.getResult();
                                if (dataSnapshot.exists()) {
                                    HashMap values = (HashMap) ((HashMap) dataSnapshot.getValue()).get(editText_wargearProfileName.getText().toString().toLowerCase().trim());
                                    if (values == null) {
                                        Toast.makeText(editText_wargearName.getContext(), "Profile not found", Toast.LENGTH_SHORT).show();
                                        Log.d("ModelViewerFragment", "onClick: profile not found");
                                        return;
                                    }
                                    items.add(CompressedProfile.compressedProfileFromHash(values).uncompressProfile());
                                    adapter.notifyItemInserted(items.size() - 1);
                                    Log.d("TAG", "onComplete: "+values.toString());

                                } else {
                                    Toast.makeText(editText_wargearProfileName.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle the error
                                Exception exception = task.getException();
                                Toast.makeText(editText_wargearProfileName.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        button_discard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        button_confirmCreateWargear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ArrayList<CompressedProfile> profiles = new ArrayList<CompressedProfile>();
                for (WargearProfile profile : items) {
                    profiles.add(new CompressedProfile(profile.getId(), profile.getLine(), profile.getRange(),
                            profile.getAp(), profile.getAttacks_chosen(), profile.getName(), profile.getS(),
                            profile.getD(), profile.getType()));
                }

                CompressedWargear newWargear = new CompressedWargear(
                        0,
                        0,
                        editText_wargearName.getText().toString().toLowerCase().trim(),
                        "inclusive",
                        profiles);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String userID = mAuth.getCurrentUser().getUid();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Log.d("TAG", "CHECK: Add wargear to database");
                mDatabase.child(userID).child("wargear").child(newWargear.getName()).setValue(newWargear).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateWargear.this, "Wargear created", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "CHECK: wargear created");
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateWargear.this, "Failed to create wargear", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "CHECK: failed to create wargear");
                    }
                });
                finish();
            }
        });
    }
}