package com.example.wh40kapp.fragments;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wh40kapp.adapters.AttackResultsRecyclerViewAdapter;
import com.example.wh40kapp.adapters.AverageAttackResultsRecyclerViewerAdapter;
import com.example.wh40kapp.logic.AttackCalculations;
import com.example.wh40kapp.data.AttackResultsData;
import com.example.wh40kapp.data.AverageAttackResultsData;
import com.example.wh40kapp.data.Model;
import com.example.wh40kapp.R;

import org.apache.commons.math4.legacy.core.Pair;

import java.util.ArrayList;
import java.util.List;

public class ResultsViewerFragment extends Fragment {
    private Button button_oneAttack, button_averageResult;
    private Switch switch_melee;
    private EditText editText_distanceToTarget;
    private TextView textView_singleAttackResult, textView_averageAttackResult;
    private Fragment[] fragments;
    private boolean isMelee;
    private int distanceToTarget;
    private RecyclerView recyclerView_attackResults;
    private ArrayList<AttackResultsData> sampleAttackResults;
    private ArrayList<AverageAttackResultsData> averageAttackResults;
    private AttackResultsRecyclerViewAdapter sampleAttackResultsAdapter;
    private AverageAttackResultsRecyclerViewerAdapter averageAttackResultsAdapter;

    public ResultsViewerFragment(Fragment[] fragments) {
        this.fragments = fragments;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results_viewer, container, false);
        isMelee = false;
        distanceToTarget = 2;
        button_averageResult = (Button) view.findViewById(R.id.button_averageResult);
        button_averageResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_attackResults.setAdapter(averageAttackResultsAdapter);
                recyclerView_attackResults.setLayoutManager(new LinearLayoutManager(recyclerView_attackResults.getContext()));
                averageAttackResults.clear();
                averageAttackResultsAdapter.notifyDataSetChanged();



                int attackerSize = ((ModelViewerFragment) fragments[0]).getItems().size();
                int defenderSize = ((ModelViewerFragment) fragments[1]).getItems().size();

                int totalIterations = attackerSize * defenderSize;
                int numThreads = Runtime.getRuntime().availableProcessors();
                int iterationsPerThread = totalIterations / numThreads;

                for (int t = 0; t < numThreads; t++) {
                    final int threadIndex = t;
                    Thread calculationThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int startIndex = threadIndex * iterationsPerThread;
                            int endIndex = (threadIndex == numThreads - 1) ? totalIterations : startIndex + iterationsPerThread;

                            for (int i = startIndex; i < endIndex; i++) {
                                int attackerIndex = i / defenderSize;
                                int defenderIndex = i % defenderSize;
                                Model attacker = ((ModelViewerFragment) fragments[0]).getItems().get(attackerIndex);
                                Model defender = ((ModelViewerFragment) fragments[1]).getItems().get(defenderIndex);
                                int[] modifiers = new int[4];
                                List<Pair<Integer, Double>> pmf = AttackCalculations.probabilityDistributionOfAttack(attacker, defender, modifiers, modifiers, modifiers, modifiers, isMelee, distanceToTarget);
                                AverageAttackResultsData resultData = new AverageAttackResultsData(attacker.getName(), defender.getName(), AttackCalculations.averageWounds(pmf), AttackCalculations.averagePointEfficiency(attacker, defender, pmf), AttackCalculations.SD(pmf), pmf);
                                synchronized (averageAttackResults) {
                                    averageAttackResults.add(resultData);
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        averageAttackResultsAdapter.notifyItemInserted(averageAttackResults.size() - 1);
                                    }
                                });
                            }
                        }
                    });

                    calculationThread.start();
                }
            }
        });
        button_oneAttack = (Button) view.findViewById(R.id.button_oneAttack);
        button_oneAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: refine the function to include the correct modifiers
                recyclerView_attackResults.setAdapter(sampleAttackResultsAdapter);
                recyclerView_attackResults.setLayoutManager(new LinearLayoutManager(recyclerView_attackResults.getContext()));
                int[] result;
                int[] modifiers;
                sampleAttackResults.clear();
                sampleAttackResultsAdapter.notifyDataSetChanged();
                for (int attackerIndex = 0; attackerIndex < ((ModelViewerFragment) fragments[0]).getItems().size(); attackerIndex++) {
                    for (int defenderIndex = 0; defenderIndex < ((ModelViewerFragment) fragments[1]).getItems().size(); defenderIndex++) {
                        result = new int[2];
                        modifiers = new int[4];
                        Model attacker = ((ModelViewerFragment) fragments[0]).getItems().get(attackerIndex);
                        Model defender = ((ModelViewerFragment) fragments[1]).getItems().get(defenderIndex);
                        AttackCalculations.singleModelAttackResult(attacker, defender, modifiers, modifiers, modifiers, modifiers, isMelee, distanceToTarget, result);
                        sampleAttackResults.add(new AttackResultsData(attacker.getName(), defender.getName(), result[0], result[1]));
                        sampleAttackResultsAdapter.notifyItemInserted(sampleAttackResults.size() - 1);
                        Log.d("TAG", "ATTACK RESULT: " +new AttackResultsData(attacker.getName(), defender.getName(), result[0], result[1]));
                    }
                }
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
        recyclerView_attackResults = (RecyclerView) view.findViewById(R.id.recyclerView_attackResults);

        sampleAttackResults = new ArrayList<AttackResultsData>();
        sampleAttackResultsAdapter = new AttackResultsRecyclerViewAdapter(sampleAttackResults);


        averageAttackResults = new ArrayList<AverageAttackResultsData>();
        averageAttackResultsAdapter = new AverageAttackResultsRecyclerViewerAdapter(averageAttackResults);

        return view;
    }
}
