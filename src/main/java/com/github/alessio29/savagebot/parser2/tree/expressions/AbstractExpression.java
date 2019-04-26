package com.github.alessio29.savagebot.parser2.tree.expressions;

public abstract class AbstractExpression implements Expression {
    private final String text;

    public AbstractExpression(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
}
