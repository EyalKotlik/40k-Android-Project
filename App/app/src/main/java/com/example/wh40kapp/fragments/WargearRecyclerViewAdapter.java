package com.example.wh40kapp.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.R;
import com.example.wh40kapp.Wargear;

import java.util.List;

public class WargearRecyclerViewAdapter extends RecyclerView.Adapter<WargearRecyclerViewAdapter.ViewHolder>{

    private final List<Wargear> mValues;

    public WargearRecyclerViewAdapter(List<Wargear> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public WargearRecyclerViewAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wargear_viewer, parent, false));
    }

    @Override
    public void onBindViewHolder(
            @NonNull WargearRecyclerViewAdapter.ViewHolder holder,
            int position
    ) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView_wargearName, textView_wargearPointsCost, textView_wargearDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView_wargearName = itemView.findViewById(R.id.textView_wargearName);
            this.textView_wargearPointsCost = itemView.findViewById(R.id.textView_wargearPointsCost);
            this.textView_wargearDesc = itemView.findViewById(R.id.textView_wargearDesc);
        }
    }
}