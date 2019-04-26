package com.github.alessio29.savagebot.parser2.tree.expressions;

public class VariableExpression extends AbstractExpression {
    private final String variableName;

    public VariableExpression(String text, String variableName) {
        super(text);
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitVariableExpression(this);
    }
}
