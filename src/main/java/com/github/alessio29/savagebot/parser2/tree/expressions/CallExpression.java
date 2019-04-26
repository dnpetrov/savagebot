package com.github.alessio29.savagebot.parser2.tree.expressions;

import java.util.List;

public class CallExpression extends AbstractExpression {
    private final String functionName;
    private final List<Expression> arguments;

    public CallExpression(String text, String functionName, List<Expression> arguments) {
        super(text);
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitCallExpression(this);
    }
}
