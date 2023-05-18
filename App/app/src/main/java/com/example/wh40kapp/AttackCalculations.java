package com.example.wh40kapp;

import android.util.Log;

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
        //TODO: implement this function (single attack result)
        //TODO: account for unarmed (melee) attacks
        boolean attacked = false;
        for (int n = 0; n < attacker.getModel_num(); n++) {
            for (Wargear wargear : attacker.getWargear()) {
                boolean exclusivity = wargear.getProfileChoice().equals("exclusive");

                for (WargearProfile profile : wargear.getProfiles()) {
                    if ( profile.getAttacks_chosen() == 0)
                        continue;
                    if (melee) { //TODO: fix multiple melee weapons - currently all melee weapons are used instead of just one
                        if (!Objects.equals(profile.getType()[0], "melee"))
                            continue;
                        for (int i = 0; i < attacker.getA(); i++) {
                            Log.d("TAG", "singleModelAttackResult: " +result[0] + " " + result[1]);
                            int hitRoll = DiceRoller.rollD6(hitMod[0], hitMod[1]);
                            if (hitRoll < attacker.getWs())
                                continue;
                            else { //this is the place to add abilities that trigger on a 6 to hit
                                int woundRoll = DiceRoller.rollD6(woundMod[0], woundMod[1]);
                                int rollNeededToWound = rollNeededToWound(modifiedMeleeStrength(attacker.getS(), profile.getS()), defender.getT());
                                if (woundRoll < rollNeededToWound)
                                    continue;
                                else { //this is the place to add abilities that trigger on a 6 to wound
                                    int saveRoll = DiceRoller.rollD6(saveMod[0], saveMod[1]);
                                    if (saveRoll + profile.getAp() >= defender.getSaves().get("armour")[0])
                                        continue;
                                    else { //this is the place to add abilities that trigger on a failed save
                                        int damageRoll = damageDealt(profile.getD(), damageMod);
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
    public static int modifiedMeleeStrength(int attackerStrength, String strengthString){
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

    /**
     * Calculates the damage dealt by a single attack
     * @param damageString the damage string of the weapon (like D6, 2D3, 3, D3+6, etc.)
     * @param damageMod    the modifiers to the damage roll, first one is the re-roll threshold, second one is the modifier, third one is remaining re-rolls, forth is remaining bonuses.
     * @return the damage dealt by the attack
     */
    public static int damageDealt(String damageString, int[] damageMod) {
        // TODO: account for things like 2D3 damage
        int damage = 0;
        char[] damageArray = damageString.toCharArray();
        if(damageString.contains("D")){
            if (damageArray[0]=='D'){
                if(damageArray[1] == 6)
                    damage = DiceRoller.rollD6(damageMod[0], damageMod[1], damageMod[2] > 0, damageMod[3] > 0);
                else
                    damage = DiceRoller.rollD3(damageMod[0], damageMod[1], damageMod[2] > 0, damageMod[3] > 0);
                damageMod[2]--;
                damageMod[3]--;
            }
            else{
                if (damageArray[2] == 6)
                    for (int i = 0; i < Character.getNumericValue(damageArray[0]); i++)
                        damage += DiceRoller.rollD6(damageMod[0], damageMod[1], damageMod[2] > 0, damageMod[3] > 0);
                else
                    for (int i = 0; i < Character.getNumericValue(damageArray[0]); i++)
                        damage += DiceRoller.rollD3(damageMod[0], damageMod[1], damageMod[2] > 0, damageMod[3] > 0);
            }
        }
        if (damageString.contains("+")) {
            damage += Integer.parseInt(damageString.substring(damageString.indexOf("+") + 1));
        } else if (damageString.contains("-")) {
            damage -= Integer.parseInt(damageString.substring(damageString.indexOf("-") + 1));
        }
        return damage;
    }
}
