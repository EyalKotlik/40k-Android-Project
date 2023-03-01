package com.example.wh40kapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.Model;
import com.example.wh40kapp.R;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class ModelViewerFragment extends Fragment {


    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Button button_AddModel;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameter initialization
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
        ArrayList<Model> items = new ArrayList<Model>();
        // Set the adapter
        Log.d("TAG", "onCreateView: 1");
        Context context = recyclerView.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        try {
            items.add(new Model(context, "necron warrior"));
            items.add(new Model(context, "immortal"));
            items.add(new Model(context, "overlord"));
            items.add(new Model(context, "triarch stalker"));
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        ModelRecyclerViewAdapter adapter = new ModelRecyclerViewAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        button_AddModel = (Button) view.findViewById(R.id.button_AddModel);
        button_AddModel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    items.add(new Model(requireContext(), "lord"));
                    adapter.notifyItemInserted(items.size() - 1);

                } catch (IOException | CsvValidationException e) {
                    Log.d("ModelViewerFragment", "onClick: failed to add model");
                }
            }
        });

        return view;
    }
}