package com.example.wh40kapp.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.R;
import com.example.wh40kapp.Wargear;

import java.util.List;

public class WargearRecyclerViewAdapter extends
        RecyclerView.Adapter<WargearRecyclerViewAdapter.ViewHolder> {

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
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wargear_viewer, parent, false));
    }

    @Override
    public void onBindViewHolder(
            final ViewHolder holder,
            int position
    ) {
        holder.textView_wargearName.setText(mValues.get(position).getName());
        holder.textView_wargearPointsCost.setText(mValues.get(position).getCost() + "");
        //holder.textView_wargearPointsCost.setText("-1");
        switch (mValues.get(position).getProfileChoice()) {
            case "exclusive":
                holder.textView_wargearDesc.setText(
                        "Before selecting targets, select one of the profiles below to make attacks with.");
                break;
            case "inclusive":
                holder.textView_wargearDesc.setText(
                        "Before selecting targets, select one of the profiles below to make attacks with. If you do not select a profile, you can make attacks with the profile that is not selected.");
                break;
            case "none":
                holder.textView_wargearDesc.setText("");
                break;
        }
        holder.recyclerView_wargearProfiles.setAdapter(
                new WargearProfileRecyclerViewAdapter(mValues.get(position).getProfiles()));
        holder.recyclerView_wargearProfiles.setLayoutManager(
                new LinearLayoutManager(holder.recyclerView_wargearProfiles.getContext()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView_wargearName, textView_wargearPointsCost,
                textView_wargearDesc;
        public final RecyclerView recyclerView_wargearProfiles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView_wargearName = itemView.findViewById(R.id.textView_wargearName);
            this.textView_wargearPointsCost =
                    itemView.findViewById(R.id.textView_wargearPointsCost);
            this.textView_wargearDesc = itemView.findViewById(R.id.textView_wargearDesc);
            this.recyclerView_wargearProfiles =
                    itemView.findViewById(R.id.recyclerView_wargearProfileList);
        }
    }
}