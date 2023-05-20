package com.example.wh40kapp.test;
import com.example.wh40kapp.fragments.DiceProbabilities;

import org.testng.annotations.Test;

import java.util.Arrays;

public class DiceProbabilitiesTest {

    @Test
    public void test1() {
        System.out.println(Arrays.toString(DiceProbabilities.probabilityToHit(3, new int[]{2, 0, 0, 0})));
    }
}
