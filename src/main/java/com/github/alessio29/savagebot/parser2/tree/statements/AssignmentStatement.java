package com.github.alessio29.savagebot.parser2.tree.statements;

import com.github.alessio29.savagebot.parser2.tree.expressions.Expression;

public class AssignmentStatement extends AbstractStatement {
    private final String variableName;
    private final Expression expression;
    private final boolean deferred;

    public AssignmentStatement(String text, String variableName, Expression expression, boolean deferred) {
        super(text);
        this.variableName = variableName;
        this.expression = expression;
        this.deferred = deferred;
    }

    public String getVariableName() {
        return variableName;
    }

    public Expression getExpression() {
        return expression;
    }

    public boolean isDeferred() {
        return deferred;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitAssignmentStatement(this);
    }
}
