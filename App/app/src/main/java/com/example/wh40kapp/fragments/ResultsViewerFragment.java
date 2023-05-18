package com.example.wh40kapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.wh40kapp.AttackCalculations;
import com.example.wh40kapp.Model;
import com.example.wh40kapp.R;

public class ResultsViewerFragment extends Fragment {
    private Button button_oneAttack, button_averageResult;
    private Fragment[] fragments;


    public ResultsViewerFragment(Fragment[] fragments) {
        this.fragments = fragments;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results_viewer, container, false);
        button_averageResult = (Button) view.findViewById(R.id.button_averageResult);
        button_averageResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: make a function to calculate the average result of an attack
            }
        });
        button_oneAttack = (Button) view.findViewById(R.id.button_oneAttack);
        button_oneAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: make a function to calculate the result of a single attack
                int[] result = new int[2];
                int[] modifiers = new int[4];
                Model attacker = ((ModelViewerFragment) fragments[0]).getItems().get(0);
                Model defender = ((ModelViewerFragment) fragments[1]).getItems().get(0);
                AttackCalculations.singleModelAttackResult(attacker, defender, modifiers, modifiers, modifiers, modifiers, false, 10, result);
                Log.d("TAG", "ATTACK RESULT: " + result[0] + " " + result[1]);
            }
        });
        return view;
    }
}
