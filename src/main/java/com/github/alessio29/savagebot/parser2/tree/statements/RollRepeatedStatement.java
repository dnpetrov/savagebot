package com.github.alessio29.savagebot.parser2.tree.statements;

import com.github.alessio29.savagebot.parser2.tree.expressions.Expression;

public class RollRepeatedStatement extends AbstractStatement {
    private final Expression times;
    private final Expression expression;

    public RollRepeatedStatement(String text, Expression times, Expression expression) {
        super(text);
        this.times = times;
        this.expression = expression;
    }

    public Expression getTimes() {
        return times;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitRollRepeatedStatement(this);
    }
}
