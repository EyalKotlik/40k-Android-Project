package com.example.wh40kapp.fragments;

import android.util.Log;

import org.apache.commons.math4.legacy.core.Pair;
import org.apache.commons.math4.legacy.distribution.EnumeratedDistribution;

import java.util.ArrayList;
import java.util.List;

public class DiceProbabilities {

    /**
     * @param skill     the skill of the unit (weapon skill or ballistic skill)
     * @param modifiers [reroltlUnder, rollMod, canReroll, canHaveBonus]
     * @return [probability to hit (excluding unmodified 6s), probability to get unmodified 6]
     */
    public static double[] probabilityToHit(int skill, int[] modifiers) {
        List<Pair<Integer, Double>> d6 = new ArrayList<Pair<Integer, Double>>();
        int rollMod = Math.max(Math.min(modifiers[1], 1), -1);
        int rollNeededToHit = Math.max(skill - rollMod, 2);
        int rerollUnder = Math.max(Math.min(modifiers[0], rollNeededToHit),1);
        System.out.println("rerollUnder: " + rerollUnder+ " rollNeededToHit: " + rollNeededToHit+ " rollMod: " + rollMod);


        for (int i = 1; i <= 6; i++) {
            if (i < rerollUnder)
                d6.add(new Pair<Integer, Double>(i, 1.0 * (rerollUnder - 1) / 36.0));
            else
                d6.add(new Pair<Integer, Double>(i, 1.0 / 6.0 + 1.0 * (rerollUnder - 1) / 36.0));
        }

        EnumeratedDistribution<Integer> d6Distribution = new EnumeratedDistribution<Integer>(d6);
        double[] probabilities = new double[2];
        probabilities[1] = d6.get(5).getValue();
        for (int i = rollNeededToHit - 1; i < 5; i++) {
            probabilities[0] += d6.get(i).getValue();
        }

        return probabilities;
    }
}
