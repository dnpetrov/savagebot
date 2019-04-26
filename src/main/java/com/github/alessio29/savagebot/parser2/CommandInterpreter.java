package com.github.alessio29.savagebot.parser2;

import com.github.alessio29.savagebot.internal.MessageTextBuilder;
import com.github.alessio29.savagebot.internal.MessageTextElement;
import com.github.alessio29.savagebot.parser2.desugar.Parser;
import com.github.alessio29.savagebot.parser2.tree.expressions.*;
import com.github.alessio29.savagebot.parser2.tree.statements.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandInterpreter {
    private final CommandInterpreterContext context;

    private final MessageTextBuilder responseBuilder = new MessageTextBuilder();

    public CommandInterpreter(CommandInterpreterContext context) {
        this.context = context;
    }

    public CommandInterpreterContext getContext() {
        return context;
    }

    public String run(String[] inputArgs) {
        responseBuilder.clear();
        Interpreter interpreter = new Interpreter();
        List<Statement> statements = new Parser(responseBuilder).parseStatements(inputArgs);
        for (Statement statement : statements) {
            responseBuilder.append(statement.accept(interpreter));
        }
        return responseBuilder.toString();
    }

    private class Interpreter implements Statement.Visitor<MessageTextElement>, Expression.Visitor<Result> {
        @Override
        public Result visitIntExpression(IntExpression intExpression) {
            return new Result(
                    intExpression.getValue(),
                    b -> b.append(intExpression.getValue())
            );
        }

        @Override
        public Result visitCallExpression(CallExpression callExpression) {
            String functionName = callExpression.getFunctionName();
            Function function = context.getFunction(functionName);
            if (function == null) {
                throw new EvaluationErrorException("Undefined function: '" + functionName + "'");
            }

            return function.call(
                    context,
                    callExpression,
                    callExpression.getArguments().stream()
                            .map(CommandInterpreter.this::eval)
                            .collect(Collectors.toList())
            );
        }

        @Override
        public Result visitRollExpression(RollExpression rollExpression) {
            String rollMethodName = rollExpression.getMethodName();
            RollMethod rollMethod = context.getRollMethod(rollMethodName);
            if (rollMethod == null) {
                throw new EvaluationErrorException("Undefined roll method: '" + rollMethodName + "'");
            }

            return rollMethod.roll(
                    context,
                    rollExpression,
                    eval(rollExpression.getArgument1()),
                    eval(rollExpression.getArgument2()),
                    rollExpression.getModifiers().stream()
                            .map(m -> new RollMethod.ModifierArgument(m.getModifier(), eval(m.getArgument())))
                            .collect(Collectors.toList())
            );
        }

        @Override
        public Result visitVariableExpression(VariableExpression variableExpression) {
            String variableName = variableExpression.getVariableName();
            Expression variableValue = context.getVariable(variableName);
            if (variableValue == null) {
                throw new EvaluationErrorException("Undefined variable: ${" + variableName + "}");
            }

            Result variableResult = eval(variableValue);

            return new Result(
                    variableResult.getValue(),
                    b -> {
                        b.append("(${").append(variableName).append("}:");
                        if (variableValue instanceof IntExpression) {
                            b.append(variableResult.getValue());
                        } else {
                            b.append(variableResult);
                        }
                        b.append(")");
                    }
            );
        }

        @Override
        public MessageTextElement visitRollOnceStatement(RollOnceStatement rollOnceStatement) {
            Result result = eval(rollOnceStatement.getExpression());

            return b -> b.append(result).newLine();
        }

        @Override
        public MessageTextElement visitRollRepeatedStatement(RollRepeatedStatement rollRepeatedStatement) {
            Expression timesExpression = rollRepeatedStatement.getTimes();
            Result timesResult = eval(timesExpression);
            List<Result> rollResults = IntStream
                    .range(1, timesResult.getValue() + 1)
                    .mapToObj(i -> eval(rollRepeatedStatement.getExpression()))
                    .collect(Collectors.toList());

            return b -> {
                if (!(timesExpression instanceof IntExpression)) {
                    b.append(timesResult).newLine();
                }
                int i = 1;
                for (Result rollResult : rollResults) {
                    b.append(i++).append(": ").append(rollResult).newLine();
                }
            };
        }

        @Override
        public MessageTextElement visitStringStatement(CommentStatement commentStatement) {
            return b -> b.append(commentStatement.getString()).append(' ');
        }

        @Override
        public MessageTextElement visitAssignmentStatement(AssignmentStatement assignmentStatement) {
            // TODO
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public Result eval(Expression expression) {
        return expression != null ? expression.accept(new Interpreter()) : null;
    }

}
