package com.example.wh40kapp.data;

import org.apache.commons.math4.legacy.core.Pair;

import java.util.List;

public class AverageAttackResultsData {
    private String attacker, defender;
    private double wounds, SD, pointEfficiency;
    private List<Pair<Integer,Double>> pmf;

    public AverageAttackResultsData(String attacker, String defender, double wounds, double pointEfficiency, double SD, List<Pair<Integer,Double>> pmf) {
        this.attacker = attacker;
        this.defender = defender;
        this.wounds = wounds;
        this.pointEfficiency = pointEfficiency;
        this.SD = SD;
        this.pmf = pmf;
    }

    public double getSD() {
        return SD;
    }

    public void setSD(double SD) {
        this.SD = SD;
    }

    public String getAttacker() {
        return attacker;
    }

    public void setAttacker(String attacker) {
        this.attacker = attacker;
    }

    public String getDefender() {
        return defender;
    }

    public void setDefender(String defender) {
        this.defender = defender;
    }

    public double getWounds() {
        return wounds;
    }

    public void setWounds(double wounds) {
        this.wounds = wounds;
    }

    public double getPointEfficiency() {
        return pointEfficiency;
    }

    public void setPointEfficiency(double pointEfficiency) {
        this.pointEfficiency = pointEfficiency;
    }

    public List<Pair<Integer,Double>> getPmf() {
        return pmf;
    }

    public void setPmf(List<Pair<Integer,Double>> pmf) {
        this.pmf = pmf;
    }
}
