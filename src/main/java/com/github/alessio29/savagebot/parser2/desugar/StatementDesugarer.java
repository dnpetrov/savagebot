package com.github.alessio29.savagebot.parser2.desugar;

import com.github.alessio29.savagebot.parser2.tree.expressions.Expression;
import com.github.alessio29.savagebot.parser2.tree.statements.AssignmentStatement;
import com.github.alessio29.savagebot.parser2.tree.statements.RollOnceStatement;
import com.github.alessio29.savagebot.parser2.tree.statements.RollRepeatedStatement;
import com.github.alessio29.savagebot.parser2.tree.statements.Statement;
import com.github.alession29.savagebot.grammar.RollBaseVisitor;
import com.github.alession29.savagebot.grammar.RollParser;
import org.antlr.v4.runtime.tree.ParseTree;

public class StatementDesugarer extends RollBaseVisitor<Statement> {
    @Override
    public Statement visitRollOnceStmt(RollParser.RollOnceStmtContext ctx) {
        return new RollOnceStatement(
                ctx.getText(),
                desugarExpression(ctx.e)
        );
    }

    @Override
    public Statement visitRollRepeatedStmt(RollParser.RollRepeatedStmtContext ctx) {
        return new RollRepeatedStatement(
                ctx.getText(),
                desugarExpression(ctx.t),
                desugarExpression(ctx.e)
        );
    }

    private Expression desugarExpression(ParseTree parseTree) {
        return new ExpressionDesugarer().visit(parseTree);
    }
}
