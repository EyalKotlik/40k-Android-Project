package com.example.wh40kapp.test;

import com.example.wh40kapp.DiceProbabilities;

import org.apache.commons.math4.legacy.analysis.polynomials.PolynomialFunction;
import org.testng.annotations.Test;

public class DiceProbabilitiesTest {

    @Test
    public void test1() {
        PolynomialFunction damage = DiceProbabilities.diceArrayProbabilities(new String[]{"2","3","+2"});
        PolynomialFunction hits = DiceProbabilities.diceArrayProbabilities(new String[]{"2","3","+1"});
        hits = DiceProbabilities.applyChanceToPass(hits,  0.5);
        hits = DiceProbabilities.applyChanceToPass(hits,  1.0/3);
        hits = DiceProbabilities.applyChanceToPass(hits,  2.0/3);
        damage = DiceProbabilities.applyDamage(hits, damage);
        System.out.println(damage);
        System.out.println(DiceProbabilities.polynomialToDistribution(damage).getPmf());
    }
}
