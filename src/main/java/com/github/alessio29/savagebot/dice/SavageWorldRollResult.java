package com.github.alessio29.savagebot.dice;

import com.github.alessio29.savagebot.internal.Messages;

public class SavageWorldRollResult extends DiceRollResult {

    public SavageWorldRollResult(DiceRollResult trait, DiceRollResult wild) {

        long res;
        if (trait.getResult() > wild.getResult()) {
            trait.setDetails(Messages.bold(trait.getDetails()));
            res = trait.getResult();
        } else {
            wild.setDetails(Messages.bold(wild.getDetails()));
            res = wild.getResult();
        }
        this.setDieCode("max(" + trait.getDieCode() + ";" + wild.getDieCode() + ")");

        this.setDetails("(" + trait.getDetails() + ";" + wild.getDetails() + ")");
        this.setResult(res);
    }

}
