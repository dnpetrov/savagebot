package com.github.alessio29.savagebot.parser2.tree.expressions;

import java.util.List;

public class RollExpression extends AbstractExpression {
    public static class Modifier {
        private final String modifier;
        private final Expression argument;

        public Modifier(String modifier, Expression argument) {
            this.modifier = modifier;
            this.argument = argument;
        }

        public String getModifier() {
            return modifier;
        }

        public Expression getArgument() {
            return argument;
        }
    }

    private final String methodName;
    private final Expression argument1;
    private final Expression argument2;
    private final List<Modifier> modifiers;

    public RollExpression(String text, String methodName, Expression argument1, Expression argument2, List<Modifier> modifiers) {
        super(text);
        this.methodName = methodName;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.modifiers = modifiers;
    }

    public String getMethodName() {
        return methodName;
    }

    public Expression getArgument1() {
        return argument1;
    }

    public Expression getArgument2() {
        return argument2;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitRollExpression(this);
    }
}
