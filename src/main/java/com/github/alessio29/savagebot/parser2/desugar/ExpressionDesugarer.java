package com.github.alessio29.savagebot.parser2.desugar;

import com.github.alessio29.savagebot.parser2.tree.expressions.CallExpression;
import com.github.alessio29.savagebot.parser2.tree.expressions.Expression;
import com.github.alessio29.savagebot.parser2.tree.expressions.IntExpression;
import com.github.alessio29.savagebot.parser2.tree.expressions.RollExpression;
import com.github.alession29.savagebot.grammar.RollBaseVisitor;
import com.github.alession29.savagebot.grammar.RollParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class ExpressionDesugarer extends RollBaseVisitor<Expression> {

    @Override
    public Expression visitBasicRollExpr(RollParser.BasicRollExprContext ctx) {
        return new RollExpression(
                ctx.getText(),
                ctx.br.op.getText(),
                desugar(ctx.br.t1),
                desugar(ctx.br.t2),
                ctx.br.basicRollSuffix().stream()
                        .map(m -> new RollModifierDesugarer().visit(m))
                        .collect(Collectors.toList())
        );
    }

    class RollModifierDesugarer extends RollBaseVisitor<RollExpression.Modifier> {
        @Override
        public RollExpression.Modifier visitOpenEndedRollSuffix(RollParser.OpenEndedRollSuffixContext ctx) {
            RollParser.ConditionSpecifierContext c = ctx.c;
            if (c == null) {
                return new RollExpression.Modifier("!", null);
            }

            RollExpression.Modifier m = visit(c);
            return new RollExpression.Modifier("!" + m.getModifier(), m.getArgument());
        }

        @Override
        public RollExpression.Modifier visitRerollSuffix(RollParser.RerollSuffixContext ctx) {
            RollParser.ConditionSpecifierContext c = ctx.c;
            if (c == null) {
                return new RollExpression.Modifier("r", null);
            }

            RollExpression.Modifier m = visit(c);
            return new RollExpression.Modifier("r" + m.getModifier(), m.getArgument());
        }

        @Override
        public RollExpression.Modifier visitRollAndKeepSuffix(RollParser.RollAndKeepSuffixContext ctx) {
            return new RollExpression.Modifier(ctx.op.getText().toLowerCase(), desugar(ctx.t));
        }

        @Override
        public RollExpression.Modifier visitAdvantageSuffix(RollParser.AdvantageSuffixContext ctx) {
            return new RollExpression.Modifier(ctx.op.getText().toLowerCase(), desugar(ctx.t));
        }

        @Override
        public RollExpression.Modifier visitTermCondition(RollParser.TermConditionContext ctx) {
            return new RollExpression.Modifier(":", desugar(ctx.t));
        }

        @Override
        public RollExpression.Modifier visitOperatorCondition(RollParser.OperatorConditionContext ctx) {
            return new RollExpression.Modifier(ctx.op.getText().toLowerCase(), desugar(ctx.t));
        }
    }

    @Override
    public Expression visitTermExpr(RollParser.TermExprContext ctx) {
        return desugar(ctx.term());
    }

    @Override
    public Expression visitMulExpr(RollParser.MulExprContext ctx) {
        return new CallExpression(
                ctx.getText(),
                Names.desugarInfixOperatorName(ctx.op.getText()),
                Arrays.asList(
                        desugar(ctx.e0),
                        desugar(ctx.e0)
                )
        );
    }

    @Override
    public Expression visitAddExpr(RollParser.AddExprContext ctx) {
        return new CallExpression(
                ctx.getText(),
                Names.desugarInfixOperatorName(ctx.op.getText()),
                Arrays.asList(
                        desugar(ctx.e0),
                        desugar(ctx.e0)
                )
        );
    }

    @Override
    public Expression visitPrefixExpr(RollParser.PrefixExprContext ctx) {
        return new CallExpression(
                ctx.getText(),
                Names.desugarPrefixOperatorName(ctx.op.getText()),
                Collections.singletonList(
                        desugar(ctx.e)
                )
        );
    }

    @Override
    public Expression visitIntTerm(RollParser.IntTermContext ctx) {
        try {
            return new IntExpression(
                    ctx.getText(),
                    Integer.parseInt(ctx.getText())
            );
        } catch (NumberFormatException e) {
            throw new DesugarErrorException("Bad integer literal: '" + ctx.getText() + "'");
        }
    }


    private Expression desugar(ParseTree parseTree) {
        return parseTree != null ? parseTree.accept(this) : null;
    }
}
