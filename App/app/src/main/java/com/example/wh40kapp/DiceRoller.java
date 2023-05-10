package com.example.wh40kapp;

public class DiceRoller {
    public static int rollD6() {
        return (int) (Math.random() * 6) + 1;
    }

    public static int rollD6(int rerollUnder, int rollMod) {
        int roll = rollD6() + rollMod;
        if (roll < rerollUnder) {
            roll = Math.max(rollD6() + rollMod, roll);
        }
        return Math.max(Math.min(roll, 6), 1);
    }

    public static int rollD6(int rerollUnder, int rollMod, boolean canReroll, boolean canHaveBonus) {
        int roll = rollD6() + (canHaveBonus ? rollMod : 0);
        if (roll < rerollUnder && canReroll) {
            roll = Math.max(rollD6() + (canHaveBonus ? rollMod : 0), roll);
        }
        return Math.max(Math.min(roll, 6), 1);
    }

    public static int rollD3() {
        return (int) (Math.random() * 3) + 1;
    }

    public static int rollD3(int rerollUnder, int rollMod) {
        int roll = rollD3() + rollMod;
        if (roll < rerollUnder) {
            roll = Math.max(rollD3() + rollMod, roll);
        }
        return Math.max(Math.min(roll, 3), 1);
    }
}
