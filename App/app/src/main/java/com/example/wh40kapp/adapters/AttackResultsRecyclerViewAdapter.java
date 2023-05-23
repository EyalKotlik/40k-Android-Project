package com.example.wh40kapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.data.AttackResultsData;
import com.example.wh40kapp.R;

import java.util.List;

public class AttackResultsRecyclerViewAdapter extends
        RecyclerView.Adapter<AttackResultsRecyclerViewAdapter.ViewHolder> {

    private final List<AttackResultsData> attackResultsList;

    public AttackResultsRecyclerViewAdapter(List<AttackResultsData> attackResultsList) {
        this.attackResultsList = attackResultsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attack_results_viewer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttackResultsData attackResultsData = attackResultsList.get(position);
        holder.textView_combatants.setText(attackResultsData.getAttacker()+" vs "+attackResultsData.getDefender());
        holder.textView_deadNum.setText(String.valueOf(attackResultsData.getDead()));
        holder.textView_woundsNum.setText(String.valueOf(attackResultsData.getwounds()));
    }

    @Override
    public int getItemCount() {
        return attackResultsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView_combatants;
        private final TextView textView_deadNum;
        private final TextView textView_woundsNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_combatants = itemView.findViewById(R.id.textView_combatants);
            textView_deadNum = itemView.findViewById(R.id.textView_deadNum);
            textView_woundsNum = itemView.findViewById(R.id.textView_woundsNum);
        }
    }
}