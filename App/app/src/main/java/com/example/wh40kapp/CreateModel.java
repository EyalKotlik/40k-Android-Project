package com.example.wh40kapp;

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

import com.example.wh40kapp.fragments.WargearRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateModel extends AppCompatActivity {
    private EditText editText_modelName, editText_cost, editText_weaponSkill,
            editText_ballisticSkill, editText_strength, editText_toughness,
            editText_wounds, editText_attacks, editText_save, editText_wargearName;
    private Button button_discard, button_confirmCreateModel, button_addWargear;
    private Spinner spinner_wargearActionChoice;
    private RecyclerView recyclerView_wargearList;
    private ArrayList<Wargear> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_model);
        editText_modelName = findViewById(R.id.editText_modelName2);
        editText_cost = findViewById(R.id.editTextNumber_cost);
        editText_weaponSkill = findViewById(R.id.editTextNumber_WS);
        editText_ballisticSkill = findViewById(R.id.editTextNumber_BS);
        editText_strength = findViewById(R.id.editTextNumber_S);
        editText_toughness = findViewById(R.id.editTextNumber_T);
        editText_wounds = findViewById(R.id.editTextNumber_W);
        editText_attacks = findViewById(R.id.editTextNumber_A);
        editText_save = findViewById(R.id.editTextNumber_save);
        editText_wargearName = findViewById(R.id.editText_wargearName);
        button_addWargear = findViewById(R.id.button_confirm2);
        button_discard = findViewById(R.id.button_discard);
        button_confirmCreateModel = findViewById(R.id.button_saveModel);
        recyclerView_wargearList = findViewById(R.id.recyclerView_newWargearProfiles);
        spinner_wargearActionChoice = findViewById(R.id.spinner_actionChoice2);
        items = new ArrayList<Wargear>();
        WargearRecyclerViewAdapter adapter = new WargearRecyclerViewAdapter(items);
        recyclerView_wargearList.setAdapter(adapter);
        recyclerView_wargearList.setLayoutManager(new LinearLayoutManager(this));

        button_addWargear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (spinner_wargearActionChoice.getSelectedItem().toString().equals("Remove")) {
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getName().equals(editText_wargearName.getText().toString().toLowerCase().trim())) {
                            items.remove(i);
                            adapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                } else if (spinner_wargearActionChoice.getSelectedItem().toString().equals("Add")) {
                    try {
                        String[] wargear = Wargear.canCreateWargear(editText_wargearName.getContext(), editText_wargearName.getText().toString().toLowerCase().trim());
                        if (wargear == null) {
                            Toast.makeText(editText_wargearName.getContext(), "Wargear not found", Toast.LENGTH_SHORT).show();
                            Log.d("CreateModel", "onClick: wargear not found");
                            return;
                        }
                        items.add(new Wargear(editText_wargearName.getContext(), Integer.parseInt(wargear[0]), 0));
                        adapter.notifyItemInserted(items.size() - 1);
                    } catch (IOException | CsvValidationException e) {
                        Log.d("CreateModel", "onClick: failed to add Wargear");
                    }
                } else if (spinner_wargearActionChoice.getSelectedItem().toString().equals("Add custom")) {
                    Log.d("Adding Custom Wargear", "onClick: " + editText_wargearName.getText().toString().toLowerCase().trim());
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("wargear");
                    mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            Log.d("Adding Custom Wargear", "onComplete: ");
                            if (task.isSuccessful()) {
                                DataSnapshot dataSnapshot = task.getResult();
                                if (dataSnapshot.exists()) {
                                    HashMap values = (HashMap) ((HashMap) dataSnapshot.getValue()).get(editText_wargearName.getText().toString().toLowerCase().trim());
                                    if (values == null) {
                                        Toast.makeText(editText_wargearName.getContext(), "Wargear not found", Toast.LENGTH_SHORT).show();
                                        Log.d("ModelViewerFragment", "onClick: wargear not found");
                                        return;
                                    }
                                    items.add(CompressedWargear.compressedWargearFromHash(values).uncompressWargear());
                                    adapter.notifyItemInserted(items.size() - 1);
                                    Log.d("TAG", "onComplete: "+values.toString());

                                } else {
                                    Toast.makeText(editText_wargearName.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle the error
                                Exception exception = task.getException();
                                Toast.makeText(editText_wargearName.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
        button_confirmCreateModel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<CompressedWargear> wargear = new ArrayList<CompressedWargear>();
                for (int i = 0; i < items.size(); i++) {
                    ArrayList<CompressedProfile> profiles = new ArrayList<CompressedProfile>();
                    for (WargearProfile profile : items.get(i).getProfiles()) {
                        profiles.add(new CompressedProfile(profile.getId(),profile.getLine(),profile.getRange(),
                                profile.getAp(),profile.getAttacks_chosen(),profile.getName(),profile.getS(),
                                profile.getD(), profile.getType()));
                    }
                    wargear.add(new CompressedWargear(items.get(i).getId(), items.get(i).getCost(), items.get(i).getName(),
                            items.get(i).getProfileChoice(), profiles));
                }
                CompressedModel newModel = new CompressedModel(editText_modelName.getText().toString().toLowerCase().trim(),
                        Integer.parseInt(editText_cost.getText().toString().trim()),
                        Integer.parseInt(editText_weaponSkill.getText().toString().trim()),
                        Integer.parseInt(editText_ballisticSkill.getText().toString().trim()),
                        Integer.parseInt(editText_strength.getText().toString().trim()),
                        Integer.parseInt(editText_toughness.getText().toString().trim()),
                        Integer.parseInt(editText_wounds.getText().toString().trim()),
                        Integer.parseInt(editText_attacks.getText().toString().trim()),
                        Integer.parseInt(editText_save.getText().toString().trim()),
                        wargear);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String userID = mAuth.getCurrentUser().getUid();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Log.d("TAG", "CHECK: Add model to database");
                mDatabase.child(userID).child("models").child(newModel.getName()).setValue(newModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateModel.this, "Model created", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "CHECK: model created");
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateModel.this, "Failed to create model", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "CHECK: failed to create model");
                    }
                });
                finish();
            }
        });
    }
}