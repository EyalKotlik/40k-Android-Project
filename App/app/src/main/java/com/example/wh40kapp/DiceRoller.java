package com.example.wh40kapp;

import android.util.Log;

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

    /**
     *
     * @param notation dice notation like 2D6+1, 3D3+2, 1D6-1, 2D3-2, 3, 4, etc.
     * @param modifiers [rerollUnder, rollMod, canReroll, canHaveBonus]
     * @return the result of the roll
     */
    public static int diceNotationToRoll(String notation, int[] modifiers){
        int result = 0;
        char[] modArray = notation.toCharArray();
        if (notation.contains("d")) {
            if (modArray[0] == 'd') {
                if (modArray[1] == '6')
                    result = DiceRoller.rollD6(modifiers[0], modifiers[1], modifiers[2] > 0, modifiers[3] > 0);
                else
                    result = DiceRoller.rollD3(modifiers[0], modifiers[1], modifiers[2] > 0, modifiers[3] > 0);
                modifiers[2]--;
                modifiers[3]--;
            } else {
                if (modArray[2] == '6')
                    for (int i = 0; i < Character.getNumericValue(modArray[0]); i++)
                        result += DiceRoller.rollD6(modifiers[0], modifiers[1], modifiers[2] > 0, modifiers[3] > 0);
                else
                    for (int i = 0; i < Character.getNumericValue(modArray[0]); i++)
                        result += DiceRoller.rollD3(modifiers[0], modifiers[1], modifiers[2] > 0, modifiers[3] > 0);
            }
        }
        else {
            result = Integer.parseInt(notation.substring(0, Math.max(notation.indexOf("+"), 1))) + (modifiers[3] > 0 ? modifiers[1] : 0);
        }

        if (notation.contains("+")) {
            result += Integer.parseInt(notation.substring(notation.indexOf("+") + 1));
        } else if (notation.contains("-")) {
            result -= Integer.parseInt(notation.substring(notation.indexOf("-") + 1));
        }
        return result;
    }
}
