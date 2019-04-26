package com.github.alessio29.savagebot.parser2.tree.statements;

import com.github.alessio29.savagebot.parser2.tree.expressions.Expression;

public class RollOnceStatement extends AbstractStatement {
    private final Expression expression;

    public RollOnceStatement(String text, Expression expression) {
        super(text);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitRollOnceStatement(this);
    }
}
