package com.github.alessio29.savagebot.parser2.tree.statements;

public interface Statement {
    String getText();

    <R> R accept(Visitor<R> visitor);

    interface Visitor<R> {
        R visitRollOnceStatement(RollOnceStatement rollOnceStatement);

        R visitRollRepeatedStatement(RollRepeatedStatement rollRepeatedStatement);

        R visitStringStatement(CommentStatement commentStatement);

        R visitAssignmentStatement(AssignmentStatement assignmentStatement);
    }
}
