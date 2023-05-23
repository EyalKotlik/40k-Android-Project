package com.example.wh40kapp.data;

public class AttackResultsData {
    private String attacker, defender;
    private int dead, wounds;

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

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public int getwounds() {
        return wounds;
    }

    public void setwounds(int wounds) {
        this.wounds = wounds;
    }

    public AttackResultsData(String attacker, String defender, int dead, int wounds) {
        this.attacker = attacker;
        this.defender = defender;
        this.dead = dead;
        this.wounds = wounds;
    }

    @Override
    public String toString() {
        return "AttackResultsData{" +
                "attacker='" + attacker + '\'' +
                ", defender='" + defender + '\'' +
                ", dead=" + dead +
                ", wounds=" + wounds +
                '}';
    }
}
