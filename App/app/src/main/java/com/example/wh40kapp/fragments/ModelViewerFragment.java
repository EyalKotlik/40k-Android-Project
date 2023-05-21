package com.example.wh40kapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.CompressedModel;
import com.example.wh40kapp.Model;
import com.example.wh40kapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class ModelViewerFragment extends Fragment {


    private int mColumnCount = 1;
    private Button button_confirm;
    private EditText editText_addedModelName;
    private static final String ARG_COLUMN_COUNT = "column-count";

    public ArrayList<Model> getItems() {
        return items;
    }

    private ArrayList<Model> items;
    private Spinner spinner_actionChoice;
    private String actionChoice;

    @SuppressWarnings("unused")
    public static ModelViewerFragment newInstance(int columnCount) {
        ModelViewerFragment fragment = new ModelViewerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ModelViewerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_model_viewer_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_modelList);
        items = new ArrayList<Model>();
        // Set the adapter
        Log.d("TAG", "onCreateView: 1");
        Context context = recyclerView.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        try {
            items.add(new Model(context, Objects.requireNonNull(Model.canCreateModel(context, "necron warrior"))));
            items.add(new Model(context, Objects.requireNonNull(Model.canCreateModel(context, "triarch stalker"))));
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        ModelRecyclerViewAdapter adapter = new ModelRecyclerViewAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        editText_addedModelName = (EditText) view.findViewById(R.id.editText_modelName);
        button_confirm = (Button) view.findViewById(R.id.button_confirm);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ( spinner_actionChoice.getSelectedItem().toString().equals("Remove") ) {
                    for (int i=0; i<items.size();i++){
                        if (items.get(i).getName().equals(editText_addedModelName.getText().toString().toLowerCase().trim())){
                            items.remove(i);
                            adapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }
                else if (spinner_actionChoice.getSelectedItem().toString().equals("Add")) {
                    try {
                        String[] model = Model.canCreateModel(requireContext(), editText_addedModelName.getText().toString().toLowerCase().trim());
                        if (model == null) {
                            Toast.makeText(requireContext(), "Model not found", Toast.LENGTH_SHORT).show();
                            Log.d("ModelViewerFragment", "onClick: model not found");
                            return;
                        }
                        items.add(new Model(requireContext(), model));
                        adapter.notifyItemInserted(items.size() - 1);
                    } catch (IOException | CsvValidationException e) {
                        Log.d("ModelViewerFragment", "onClick: failed to add model");
                    }
                }
                else if (spinner_actionChoice.getSelectedItem().toString().equals("Add custom")) {
                    Log.d("Adding Custom Model", "onClick: "+editText_addedModelName.getText().toString().toLowerCase().trim());
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("models");
                    mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            Log.d("Adding Custom Model", "onComplete: ");
                            if (task.isSuccessful()) {
                                DataSnapshot dataSnapshot = task.getResult();
                                if (dataSnapshot.exists()) {
                                    HashMap values = (HashMap) ((HashMap) dataSnapshot.getValue()).get(editText_addedModelName.getText().toString().toLowerCase().trim());
                                    items.add(CompressedModel.compressedModelFromHash(values).uncompressModel());
                                    adapter.notifyItemInserted(items.size() - 1);
                                    Log.d("TAG", "onComplete: "+values.toString());

                                } else {
                                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle the error
                                Exception exception = task.getException();
                                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        spinner_actionChoice = (Spinner) view.findViewById(R.id.spinner_actionChoice);
        /*/spinner_actionChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actionChoice = parent.getItemAtPosition(position).toString();
                Log.d("ModelViewerFragment", "onItemSelected: " + actionChoice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected
            }
        });*/
        return view;
    }
}