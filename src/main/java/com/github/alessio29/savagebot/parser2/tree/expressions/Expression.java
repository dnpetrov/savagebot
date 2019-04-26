package com.github.alessio29.savagebot.parser2.tree.expressions;

public interface Expression {
    String getText();

    <R> R accept(Visitor<R> visitor);

    interface Visitor<R> {
        R visitIntExpression(IntExpression intExpression);

        R visitVariableExpression(VariableExpression variableExpression);

        R visitRollExpression(RollExpression rollExpression);

        R visitCallExpression(CallExpression callExpression);
    }
}
