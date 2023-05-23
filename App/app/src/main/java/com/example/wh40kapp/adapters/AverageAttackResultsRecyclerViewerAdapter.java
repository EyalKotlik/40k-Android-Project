package com.example.wh40kapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.data.AverageAttackResultsData;
import com.example.wh40kapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.commons.math4.legacy.core.Pair;

import java.util.ArrayList;
import java.util.List;

public class AverageAttackResultsRecyclerViewerAdapter extends RecyclerView.Adapter<AverageAttackResultsRecyclerViewerAdapter.ViewHolder> {
    private final List<AverageAttackResultsData> attackResultsList;

    public AverageAttackResultsRecyclerViewerAdapter(List<AverageAttackResultsData> AverageAttackResultsData) {
        this.attackResultsList = AverageAttackResultsData;
    }

    @NonNull
    @Override
    public AverageAttackResultsRecyclerViewerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.average_attack_results_viewer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AverageAttackResultsData averageAttackResultsData = attackResultsList.get(position);
        holder.textView_title.setText(averageAttackResultsData.getAttacker() + " vs " + averageAttackResultsData.getDefender());
        holder.textView_meanWounds.setText("Mean Wounds: " + String.format("%.3f", averageAttackResultsData.getWounds()));
        holder.textView_SD.setText("Standard Deviation: " + String.format("%.3f", averageAttackResultsData.getSD()));
        holder.textView_textView_pointEfficiancy.setText("Damage in pts. / cost in pts.: " + String.format("%.3f",averageAttackResultsData.getPointEfficiency()));
        List<BarEntry> entries = new ArrayList<>();
        List<Pair<Integer, Double>> pmf = averageAttackResultsData.getPmf();
        for (int i = 0; i < pmf.size(); i++) {
            int value = pmf.get(i).getKey();
            double probability = pmf.get(i).getValue();
            entries.add(new BarEntry(value, (float) probability));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Distribution");
        BarData barData = new BarData(dataSet);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);
        holder.barChart_results.setData(barData);
        holder.barChart_results.invalidate();
    }

    @Override
    public int getItemCount() {
        return attackResultsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView_meanWounds, textView_SD, textView_textView_pointEfficiancy, textView_title;
        private final BarChart barChart_results;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_meanWounds = itemView.findViewById(R.id.textView_meanWounds);
            textView_SD =  itemView.findViewById(R.id.textView_SD);
            textView_textView_pointEfficiancy = itemView.findViewById(R.id.textView_pointEfficiancy);
            barChart_results = (BarChart) itemView.findViewById(R.id.barChart_results);
        }
    }
}
