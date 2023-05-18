package com.example.wh40kapp;

public class DiceRoller {
    /**
     * @return a random number between 1 and 6 (inclusive)
     */
    public static int rollD6() {
        return (int) (Math.random() * 6) + 1;
    }

    /**
     * @param rerollUnder the number that must be rolled under to reroll (once)
     * @param rollMod     the modifier to the roll
     * @return the roll (or reroll if applicable), modified by the rollMod.
     */
    public static int rollD6(int rerollUnder, int rollMod) {
        int roll = rollD6() + rollMod;
        if (roll < rerollUnder) {
            roll = Math.max(rollD6() + rollMod, roll);
        }
        return Math.max(Math.min(roll, 6), 1);
    }

    /**
     * @param rerollUnder  the number that must be rolled under to reroll (once)
     * @param rollMod      the modifier to the roll
     * @param canReroll    whether or not the roll can be rerolled
     * @param canHaveBonus whether or not the roll can have a bonus
     * @return the roll (or reroll if applicable), modified by the rollMod.
     */
    public static int rollD6(int rerollUnder, int rollMod, boolean canReroll, boolean canHaveBonus) {
        int roll = rollD6() + (canHaveBonus ? rollMod : 0);
        if (roll < rerollUnder && canReroll) {
            roll = Math.max(rollD6() + (canHaveBonus ? rollMod : 0), roll);
        }
        return Math.max(roll, 1);
    }

    /**
     * @return a random number between 1 and 3 (inclusive)
     */
    public static int rollD3() {
        return (int) (Math.random() * 3) + 1;
    }

    /**
     * @param rerollUnder the number that must be rolled under to reroll (once)
     * @param rollMod     the modifier to the roll
     * @return the roll (or reroll if applicable), modified by the rollMod.
     * @post the roll will be at least 1.
     */
    public static int rollD3(int rerollUnder, int rollMod) {
        int roll = rollD3() + rollMod;
        if (roll < rerollUnder) {
            roll = Math.max(rollD3() + rollMod, roll);
        }
        return Math.max(roll, 1);
    }

    /**
     * @param rerollUnder  the number that must be rolled under to reroll (once)
     * @param rollMod      the modifier to the roll
     * @param canReroll    whether or not the roll can be rerolled
     * @param canHaveBonus whether or not the roll can have a bonus
     * @return the roll (or reroll if applicable), modified by the rollMod.
     */
    public static int rollD3(int rerollUnder, int rollMod, boolean canReroll, boolean canHaveBonus) {
        int roll = rollD3() + (canHaveBonus ? rollMod : 0);
        if (roll < rerollUnder && canReroll) {
            roll = Math.max(rollD3() + (canHaveBonus ? rollMod : 0), roll);
        }
        return Math.max(roll, 1);
    }
}
