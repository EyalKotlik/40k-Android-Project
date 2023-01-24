package com.example.wh40kapp.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.R;
import com.example.wh40kapp.WargearProfile;

import java.util.List;

public class WargearProfileRecyclerViewAdapter extends
        RecyclerView.Adapter<WargearProfileRecyclerViewAdapter.ViewHolder> {
    private final List<WargearProfile> mValues;

    public WargearProfileRecyclerViewAdapter(List<WargearProfile> mValues) {
        this.mValues = mValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WargearProfileRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wargear_profile_viewer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TO DO: holder.recyclerView_wargearProfileAbilities.setAdapter(new WargearProfileAbilityRecyclerViewAdapter(mValues.get(position).getAbilities()));
        holder.textView_wargearProfileName.setText(mValues.get(position).getName());
        holder.textView_wargearProfileRange.setText(mValues.get(position).getRange()+"\"");
        holder.textView_wargearProfileType.setText(mValues.get(position).getType()[0].toString()+" "+mValues.get(position).getType()[1].toString());
        holder.textView_wargearProfileS.setText(mValues.get(position).getS());
        holder.textView_wargearProfileAP.setText("-"+mValues.get(position).getAp());
        holder.textView_wargearProfileD.setText(mValues.get(position).getD());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView_wargearProfileName, textView_wargearProfileRange,
                textView_wargearProfileType, textView_wargearProfileS, textView_wargearProfileAP,
                textView_wargearProfileD;
        public final RecyclerView recyclerView_wargearProfileAbilities;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.recyclerView_wargearProfileAbilities = itemView.findViewById(R.id.recyclerView_wargearProfileAbilities);
            this.textView_wargearProfileName = itemView.findViewById(R.id.textView_wargearProfileName);
            this.textView_wargearProfileRange = itemView.findViewById(R.id.textView_wargearProfileRange);
            this.textView_wargearProfileType = itemView.findViewById(R.id.textView_wargearProfileType);
            this.textView_wargearProfileS = itemView.findViewById(R.id.textView_wargearProfileS);
            this.textView_wargearProfileAP = itemView.findViewById(R.id.textView_wargearProfileAP);
            this.textView_wargearProfileD = itemView.findViewById(R.id.textView_wargearProfileD);
        }
    }
}
