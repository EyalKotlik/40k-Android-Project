package com.example.wh40kapp;

import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math4.legacy.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math4.legacy.core.Pair;
import org.apache.commons.math4.legacy.distribution.EnumeratedDistribution;
import org.apache.commons.math4.legacy.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.statistics.distribution.BinomialDistribution;
import org.apache.commons.statistics.distribution.GeometricDistribution;
import org.apache.commons.statistics.distribution.UniformDiscreteDistribution;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        int rerollUnder = Math.max(Math.min(modifiers[0], rollNeededToHit), 1);

        for (int i = 1; i <= 6; i++) {
            if (i < rerollUnder)
                d6.add(new Pair<Integer, Double>(i, 1.0 * (rerollUnder - 1) / 36.0));
            else
                d6.add(new Pair<Integer, Double>(i, 1.0 / 6.0 + 1.0 * (rerollUnder - 1) / 36.0));
        }

        double[] probabilities = new double[2];
        probabilities[1] = d6.get(5).getValue();
        for (int i = rollNeededToHit - 1; i < 5; i++) {
            probabilities[0] += d6.get(i).getValue();
        }

        return probabilities;
    }

    /**
     * @param toWound   roll needed to wound
     * @param modifiers [reroltlUnder, rollMod, canReroll, canHaveBonus]
     * @return [probability to wound (excluding unmodified 6s), probability to get unmodified 6]
     */
    public static double[] probabilityToWound(int toWound, int[] modifiers) {
        List<Pair<Integer, Double>> d6 = new ArrayList<Pair<Integer, Double>>();
        int rollMod = Math.max(Math.min(modifiers[1], 1), -1);
        int rollNeededToWound = Math.max(toWound - rollMod, 2);
        int rerollUnder = Math.max(Math.min(modifiers[0], rollNeededToWound), 1);

        for (int i = 1; i <= 6; i++) {
            if (i < rerollUnder)
                d6.add(new Pair<Integer, Double>(i, 1.0 * (rerollUnder - 1) / 36.0));
            else
                d6.add(new Pair<Integer, Double>(i, 1.0 / 6.0 + 1.0 * (rerollUnder - 1) / 36.0));
        }

        double[] probabilities = new double[2];
        probabilities[1] = d6.get(5).getValue();
        for (int i = rollNeededToWound - 1; i < 5; i++) {
            probabilities[0] += d6.get(i).getValue();
        }

        return probabilities;
    }

    /**
     * @param save      roll needed to save
     * @param modifiers [reroltlUnder, rollMod, canReroll, canHaveBonus] rollMod is negative for AP, and will be 0 for invulnerable/daemonic saves
     * @return probability for the save to be failed
     */
    public static double probabilityToNOTSave(int save, int[] modifiers) {
        List<Pair<Integer, Double>> d6 = new ArrayList<Pair<Integer, Double>>();
        int rollMod = modifiers[1];
        int rollNeededToSave = Math.max(save - rollMod, 2);
        int rerollUnder = Math.max(Math.min(modifiers[0], rollNeededToSave), 1);

        for (int i = 1; i <= 6; i++) {
            if (i < rerollUnder)
                d6.add(new Pair<Integer, Double>(i, 1.0 * (rerollUnder - 1) / 36.0));
            else
                d6.add(new Pair<Integer, Double>(i, 1.0 / 6.0 + 1.0 * (rerollUnder - 1) / 36.0));
        }

        double probability = 1.0;
        for (int i = rollNeededToSave - 1; i < 6; i++) {
            probability -= d6.get(i).getValue();
        }

        return probability;
    }

    /**
     * @param diceNotation the dice notation to be converted (e.g. 2d6+1, d6-1, 3, d6, 2d6, etc.)
     * @return [number of dice, number of sides, modifier]
     */
    public static String[] diceNotationToArray(String diceNotation) {
        String[] notation = new String[3];

        if (diceNotation.contains("d"))
            if (diceNotation.charAt(0) == 'd') {
                notation[0] = "1";
                notation[1] = String.valueOf(diceNotation.charAt(1));
            } else {
                notation[0] = String.valueOf(diceNotation.charAt(0));
                notation[1] = String.valueOf(diceNotation.charAt(2));
            }
        else {
            notation[0] = "0";
            notation[1] = "6";
            notation[2] = diceNotation;
            return notation;
        }

        if (diceNotation.contains("+")) {
            notation[2] = diceNotation.substring(diceNotation.indexOf('+'));
        } else if (diceNotation.contains("-")) {
            notation[2] = diceNotation.substring(diceNotation.indexOf('-'));
        } else
            notation[2] = "0";

        return notation;
    }

    /**
     * @param diceNotation [number of dice, number of sides, modifier]
     * @return the probability distribution of the dice roll (in the form of a polynomial function where the coefficients are the probabilities)
     */
    public static PolynomialFunction diceArrayProbabilities(String[] diceNotation) {
        int numberOfDice = Integer.parseInt(diceNotation[0]);
        int numberOfSides = Integer.parseInt(diceNotation[1]);
        int modifier = Integer.parseInt(diceNotation[2]);
        double probability = 1.0 / numberOfSides;
        double[] coefficients = new double[numberOfSides+modifier+1];
        if (numberOfDice == 0) {
            coefficients[modifier] = 1;
            return new PolynomialFunction(coefficients);
        }
        coefficients[0] = 0;
        for (int i = 1; i <= numberOfSides; i++) {
            coefficients[i] = probability;
        }
        PolynomialFunction polynomial1 = new PolynomialFunction(coefficients);
        PolynomialFunction polynomial2 = new PolynomialFunction(coefficients);
        for (int i = 1; i < numberOfDice; i++) {
            polynomial1 = polynomial1.multiply(polynomial2);
        }
        coefficients = polynomial1.getCoefficients();
        double[] newCoefficients = new double[coefficients.length + modifier];
        System.arraycopy(coefficients, 0, newCoefficients, modifier, coefficients.length); //TODO: crashes for negative modifiers

        return new PolynomialFunction(newCoefficients);
    }


    /**
     * @param polynomial polynomial function describing the probability of a dice roll
     *                  (in the form of a polynomial function where the coefficients are the probabilities and the index is the number of successes)
     * @param chanceToPass chance to pass the test
     * @return the probability distribution of the dice roll
     *         (in the form of a polynomial function where the coefficients are the probabilities and the index is the number of successes)
     */
    public static PolynomialFunction applyChanceToPass(PolynomialFunction polynomial, double chanceToPass) {
        //TODO: add support for things that happen on an unmodified 6
        double[] coefficients = polynomial.getCoefficients();
        double[] newCoefficients = new double[coefficients.length];
        BinomialDistribution binomialDistribution;
        for (int i = 0; i < coefficients.length; i++) {
            binomialDistribution = BinomialDistribution.of(i, chanceToPass);
            for (int j = 0; j <= i; j++) {
                newCoefficients[j] += coefficients[i] * binomialDistribution.probability(j);
            }
        }
        return new PolynomialFunction(newCoefficients);
    }

    /**
     *
     * @param hits     polynomial function describing the probability of each number of hits
     * @param damage   polynomial function describing the probability of each damage roll
     * @return         polynomial function describing the probability of each number of wounds
     */
    public static PolynomialFunction applyDamage(PolynomialFunction hits, PolynomialFunction damage){
        double[] hitsCoefficients = hits.getCoefficients();
        double[] damageCoefficients = damage.getCoefficients();
        double[] originalDamageCoefficients = damageCoefficients.clone();
        PolynomialFunction originalDamage = new PolynomialFunction(damageCoefficients);
        double[] newCoefficients = new double[hitsCoefficients.length*damageCoefficients.length];
        newCoefficients[0] = hitsCoefficients[0];
        for (int i = 1; i < hitsCoefficients.length; i++) {
            damageCoefficients = damage.getCoefficients();
            for(int j = 0; j < damageCoefficients.length; j++){
                newCoefficients[j] += hitsCoefficients[i]*damageCoefficients[j];
            }
            damage = damage.multiply(originalDamage);
        }
        //System.out.println(Arrays.stream(newCoefficients).sum());
        return new PolynomialFunction(newCoefficients);
    }

    public static EnumeratedDistribution polynomialToDistribution(PolynomialFunction polynomial){
        double[] coefficients = polynomial.getCoefficients();
        List<Pair<Integer,Double>> probabilities = new ArrayList<>();
        for (int i = 0; i < coefficients.length; i++) {
            probabilities.add(i, new Pair<Integer, Double>(i, coefficients[i]));
        }
        return new EnumeratedDistribution(probabilities);
    }

    public static PolynomialFunction combineResultPolynomials(PolynomialFunction result1, PolynomialFunction result2){
        return result1.multiply(result2);
    }
}
