package com.example.wh40kapp.test;

import com.example.wh40kapp.fragments.DiceProbabilities;

import org.apache.commons.math4.legacy.analysis.polynomials.PolynomialFunction;
import org.testng.annotations.Test;

import java.util.Arrays;

public class DiceProbabilitiesTest {

    @Test
    public void test1() {
        PolynomialFunction polynomial = DiceProbabilities.diceArrayProbabilities(new String[]{"1","6","0"});
        System.out.println(polynomial);
    }
}
