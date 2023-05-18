package com.example.wh40kapp;

import android.util.Log;

import java.util.Arrays;
import java.util.Objects;

public class AttackCalculations {
    /**
     * Calculates the result of a single attack
     *
     * @param attacker  the attacking model.
     * @param defender  the defending model.
     * @param hitMod    the modifiers to the hit roll, first one is the re-roll threshold, second one is the modifier, third one is remaining re-rolls, forth is remaining bonuses.
     * @param woundMod  the modifiers to the wound roll, first one is the re-roll threshold, second one is the modifier, third one is remaining re-rolls, forth is remaining bonuses.
     * @param saveMod   the modifiers to the save roll, first one is the re-roll threshold, second one is the modifier, third one is remaining re-rolls, forth is remaining bonuses.
     * @param damageMod the modifiers to the damage roll, first one is the re-roll threshold, second one is the modifier, third one is remaining re-rolls, forth is remaining bonuses.
     * @return an array of two integers, the first one is the number of dead models, the second one is the damage dealt to the last model.
     */
    public static void singleModelAttackResult(Model attacker, Model defender, int[] hitMod, int[] woundMod, int[] saveMod, int[] damageMod, boolean melee, int distance, int[] result) {
        //TODO: account for daemonic, invulnerable saves
        boolean attacked = false;
        for (int n = 0; n < attacker.getModel_num(); n++) {
            for (Wargear wargear : attacker.getWargear()) {
                boolean exclusivity = wargear.getProfileChoice().equals("exclusive");

                // attacks with the model's weapons
                for (WargearProfile profile : wargear.getProfiles()) {
                    if (profile.getAttacks_chosen() == 0) //if the profile is not chosen, skip it
                        continue;
                    if (melee && !attacked) { //if the model has melee weapons, hasn't attacked yet, and is set to fight in melee; attack with these weapons
                        if (!Objects.equals(profile.getType()[0], "melee"))
                            continue;
                        attacked = true;
                        for (int i = 0; i < attacker.getA(); i++) {
                            Log.d("TAG", "singleModelAttackResult [Dead, Wounded]: " + result[0] + " " + result[1]);
                            int hitRoll = DiceRoller.rollD6(hitMod[0], hitMod[1]);
                            if (hitRoll < attacker.getWs())
                                continue;
                            else { //this is the place to add abilities that trigger on a 6 to hit
                                int woundRoll = DiceRoller.rollD6(woundMod[0], woundMod[1]);
                                int rollNeededToWound = rollNeededToWound(modifiedStrength(attacker.getS(), profile.getS()), defender.getT());
                                if (woundRoll < rollNeededToWound)
                                    continue;
                                else { //this is the place to add abilities that trigger on a 6 to wound
                                    int saveRoll = DiceRoller.rollD6(saveMod[0], saveMod[1]);
                                    if (saveRoll + profile.getAp() >= defender.getSaves().get("armour")[0])
                                        continue;
                                    else { //this is the place to add abilities that trigger on a failed save
                                        int damageRoll = DiceRoller.diceNotationToRoll(profile.getD(), damageMod);
                                        if (damageRoll >= defender.getW() - result[1]) {
                                            result[0]++;
                                            result[1] = 0;
                                        } else {
                                            result[1] += damageRoll;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // attacks with the model's ranged weapons
                    if (!melee) {
                        if (Objects.equals(profile.getType()[0], "melee") || (Objects.equals(profile.getType()[0], "pistol") && attacked))
                            continue;
                        attacked = true;
                        int attacks = DiceRoller.diceNotationToRoll(profile.getType()[1], new int[]{0, 0, 0, 0});
                        attacks = Objects.equals(profile.getType()[0], "rapid fire") && distance > profile.getRange() / 2 ? attacks * 2 : attacks;
                        for (int i = 0; i < attacks; i++) {
                            Log.d("TAG", "singleModelAttackResult attack with: " + profile.getName() + "; [Dead, Wounded]: " + result[0] + " " + result[1]);
                            int hitRoll = DiceRoller.rollD6(hitMod[0], hitMod[1]);
                            if (hitRoll < attacker.getBs())
                                continue;
                            else { //this is the place to add abilities that trigger on a 6 to hit
                                int woundRoll = DiceRoller.rollD6(woundMod[0], woundMod[1]);
                                int rollNeededToWound = rollNeededToWound(profile.getS().matches("\\d+") ? Integer.parseInt(profile.getS()) : modifiedStrength(attacker.getS(), profile.getS()), defender.getT());
                                if (woundRoll < rollNeededToWound)
                                    continue;
                                else { //this is the place to add abilities that trigger on a 6 to wound
                                    int saveRoll = DiceRoller.rollD6(saveMod[0], saveMod[1]);
                                    if (saveRoll + profile.getAp() >= defender.getSaves().get("armour")[0])
                                        continue;
                                    else { //this is the place to add abilities that trigger on a failed save
                                        int damageRoll = DiceRoller.diceNotationToRoll(profile.getD(), damageMod);
                                        if (damageRoll >= defender.getW() - result[1]) {
                                            result[0]++;
                                            result[1] = 0;
                                        } else {
                                            result[1] += damageRoll;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // if the model fights in melee, and hasn't attacked with a melee weapon yet, attack with the model's unarmed profile
                if (melee && !attacked) {
                    for (int i = 0; i < attacker.getA(); i++) {
                        attacked = true;
                        int hitRoll = DiceRoller.rollD6(hitMod[0], hitMod[1]);
                        if (hitRoll < attacker.getWs())
                            continue;
                        else { //this is the place to add abilities that trigger on a 6 to hit
                            int woundRoll = DiceRoller.rollD6(woundMod[0], woundMod[1]);
                            int rollNeededToWound = rollNeededToWound(modifiedStrength(attacker.getS(), "User"), defender.getT());
                            if (woundRoll < rollNeededToWound)
                                continue;
                            else { //this is the place to add abilities that trigger on a 6 to wound
                                int saveRoll = DiceRoller.rollD6(saveMod[0], saveMod[1]);
                                if (saveRoll >= defender.getSaves().get("armour")[0])
                                    continue;
                                else { //this is the place to add abilities that trigger on a failed save
                                    int damageRoll = DiceRoller.diceNotationToRoll("1", damageMod);
                                    if (damageRoll >= defender.getW() - result[1]) {
                                        result[0]++;
                                        result[1] = 0;
                                    } else {
                                        result[1] += damageRoll;
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }
    }

    public static int rollNeededToWound(int attackerStrength, int defenderToughness) {
        if (attackerStrength >= defenderToughness * 2) {
            return 2;
        } else if (attackerStrength > defenderToughness) {
            return 3;
        } else if (attackerStrength == defenderToughness) {
            return 4;
        } else if (attackerStrength * 2 <= defenderToughness) {
            return 6;
        } else {
            return 5;
        }
    }

    public static int modifiedStrength(int attackerStrength, String strengthString) {
        int S = attackerStrength;
        if (strengthString.contains("+")) {
            S += Integer.parseInt(strengthString.substring(strengthString.indexOf("+") + 1));
        } else if (strengthString.contains("-")) {
            S -= Integer.parseInt(strengthString.substring(strengthString.indexOf("-") + 1));
        } else if (strengthString.contains("x")) {
            S *= Integer.parseInt(strengthString.substring(strengthString.indexOf("x") + 1));
        }

        return S;
    }
}
