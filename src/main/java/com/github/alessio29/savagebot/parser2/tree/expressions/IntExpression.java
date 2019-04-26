package com.github.alessio29.savagebot.parser2.tree.expressions;

public class IntExpression extends AbstractExpression {
    private final int value;

    public IntExpression(String text, int value) {
        super(text);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitIntExpression(this);
    }
}
