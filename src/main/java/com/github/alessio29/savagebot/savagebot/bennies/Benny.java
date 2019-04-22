package com.github.alessio29.savagebot.savagebot.bennies;

public class Benny {

    private BennyColor color;

    public Benny(BennyColor color) {
        this.color = color;
    }

    public BennyColor getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return color + " benny";
    }


}
