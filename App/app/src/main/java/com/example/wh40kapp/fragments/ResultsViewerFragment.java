package com.example.wh40kapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wh40kapp.AttackCalculations;
import com.example.wh40kapp.Model;
import com.example.wh40kapp.R;

public class ResultsViewerFragment extends Fragment {
    private Button button_oneAttack, button_averageResult;
    private Switch switch_melee;
    private EditText editText_distanceToTarget;
    private TextView textView_singleAttackResult, textView_averageAttackResult;
    private Fragment[] fragments;
    private boolean isMelee;
    private int distanceToTarget;


    public ResultsViewerFragment(Fragment[] fragments) {
        this.fragments = fragments;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results_viewer, container, false);
        isMelee = false;
        distanceToTarget = 2;
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
                //TODO: refine the function to include the correct modifiers
                int[] result = new int[2];
                int[] modifiers = new int[4];
                Model attacker = ((ModelViewerFragment) fragments[0]).getItems().get(0);
                Model defender = ((ModelViewerFragment) fragments[1]).getItems().get(0);
                AttackCalculations.singleModelAttackResult(attacker, defender, modifiers, modifiers, modifiers, modifiers, isMelee, distanceToTarget, result);
                textView_singleAttackResult.setText("Sample attack result: "+result[0] + " Dead; " + result[1]+" Wounded");
                Log.d("TAG", "ATTACK RESULT: " + result[0] + " " + result[1]);
            }
        });
        switch_melee = (Switch) view.findViewById(R.id.switch_melee);
        switch_melee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMelee) {
                    isMelee = false;
                } else {
                    isMelee = true;
                }
            }
        });
        editText_distanceToTarget = (EditText) view.findViewById(R.id.editText_distanceToTarget);
        editText_distanceToTarget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().matches("\\d+")) {
                    distanceToTarget = Integer.parseInt(editable.toString());
                    return;
                }
                Toast.makeText(editText_distanceToTarget.getContext(), "Distance can only be a positive integer", Toast.LENGTH_SHORT).show();
            }
        });
        textView_singleAttackResult = (TextView) view.findViewById(R.id.textView_singleAttackResult);
        textView_averageAttackResult = (TextView) view.findViewById(R.id.textView_averageAttackResult);
        return view;
    }
}
