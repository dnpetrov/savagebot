package com.github.alessio29.savagebot.parser2.tree.statements;

public abstract class AbstractStatement implements Statement {
    private final String text;

    public AbstractStatement(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
}
