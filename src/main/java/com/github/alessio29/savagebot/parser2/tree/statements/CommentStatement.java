package com.github.alessio29.savagebot.parser2.tree.statements;

public class CommentStatement extends AbstractStatement {
    private final String string;

    public CommentStatement(String string) {
        this(string, string);
    }

    public CommentStatement(String text, String string) {
        super(text);
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitStringStatement(this);
    }
}
