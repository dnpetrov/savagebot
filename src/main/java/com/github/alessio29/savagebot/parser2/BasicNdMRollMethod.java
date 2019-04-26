package com.github.alessio29.savagebot.parser2;

import com.github.alessio29.savagebot.internal.MessageTextBuilder;
import com.github.alessio29.savagebot.parser2.tree.expressions.RollExpression;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BasicNdMRollMethod implements RollMethod {
    private enum Operator {
        EQUAL, GREATER, GREATER_OR_EQUAL, LESS, LESS_OR_EQUAL, HIGHEST, LOWEST
    }

    private static class Operation {
        private final Operator operator;
        private final Result argument;
        private final int value;

        public Operation(Operator operator, int value) {
            this.operator = operator;
            this.argument = null;
            this.value = value;
        }

        public Operation(Operator operator, Result argument) {
            this.operator = operator;
            this.argument = argument;
            this.value = argument.getValue();
        }

        public Operator getOperator() {
            return operator;
        }

        public Result getArgument() {
            return argument;
        }

        public int getValue() {
            return value;
        }

        public boolean is(int value) {
            switch (operator) {
                case EQUAL:
                    return value == this.value;
                case GREATER:
                    return value > this.value;
                case GREATER_OR_EQUAL:
                    return value >= this.value;
                case LESS:
                    return value < this.value;
                case LESS_OR_EQUAL:
                    return value <= this.value;
                default:
                    throw new EvaluationErrorException("Operator should not be used as a predicate: " + operator);
            }
        }
    }

    private static final class RollData {
        final RollExpression rollExpression;

        public RollData(RollExpression rollExpression) {
            this.rollExpression = rollExpression;
        }

        int nDice = 0;
        int nFacets = 0;

        Operation openEndedOperation = null;
        Operation rerollOperation = null;

        Operation rollAndKeep = null;

        Operation success = null;
        Operation failure = null;
    }

    private interface ModifierHandler {
        void handle(RollData rollData, Result argument);
    }

    private static class AdvantageDisadvantageModifierHandler implements ModifierHandler {
        private final Operator operator;

        public AdvantageDisadvantageModifierHandler(Operator operator) {
            this.operator = operator;
        }

        @Override
        public void handle(RollData rollData, Result argument) {
            if (argument != null) {
                rollData.rollAndKeep = new Operation(operator, argument);
                rollData.nDice += argument.getValue();
            } else {
                rollData.rollAndKeep = new Operation(operator, 1);
                rollData.nDice += 1;
            }
        }
    }

    private static final Map<String, ModifierHandler> MODIFIERS = new HashMap<>();
    static {
        MODIFIERS.put("!", (rd, a) -> rd.openEndedOperation = new Operation(Operator.EQUAL, rd.nFacets));
        MODIFIERS.put("!:", (rd, a) -> rd.openEndedOperation = new Operation(Operator.EQUAL, a));
        MODIFIERS.put("!>", (rd, a) -> rd.openEndedOperation = new Operation(Operator.GREATER, a));
        MODIFIERS.put("!>=", (rd, a) -> rd.openEndedOperation = new Operation(Operator.GREATER_OR_EQUAL, a));
        MODIFIERS.put("!<", (rd, a) -> rd.openEndedOperation = new Operation(Operator.LESS, a));
        MODIFIERS.put("!<=", (rd, a) -> rd.openEndedOperation = new Operation(Operator.LESS_OR_EQUAL, a));

        MODIFIERS.put("r", (rd, a) -> rd.rerollOperation = new Operation(Operator.EQUAL, 1));
        MODIFIERS.put("r:", (rd, a) -> rd.rerollOperation = new Operation(Operator.EQUAL, a));
        MODIFIERS.put("r>", (rd, a) -> rd.rerollOperation = new Operation(Operator.GREATER, a));
        MODIFIERS.put("r>=", (rd, a) -> rd.rerollOperation = new Operation(Operator.GREATER_OR_EQUAL, a));
        MODIFIERS.put("r<", (rd, a) -> rd.rerollOperation = new Operation(Operator.LESS, a));
        MODIFIERS.put("r<=", (rd, a) -> rd.rerollOperation = new Operation(Operator.LESS_OR_EQUAL, a));

        MODIFIERS.put("k", (rd, a) -> rd.rollAndKeep = new Operation(Operator.HIGHEST, a));
        MODIFIERS.put("kl", (rd, a) -> rd.rollAndKeep = new Operation(Operator.LOWEST, a));

        AdvantageDisadvantageModifierHandler withAdvantage = new AdvantageDisadvantageModifierHandler(Operator.HIGHEST);
        MODIFIERS.put("a", withAdvantage);
        MODIFIERS.put("adv", withAdvantage);

        AdvantageDisadvantageModifierHandler withDisadvantage = new AdvantageDisadvantageModifierHandler(Operator.LOWEST);
        MODIFIERS.put("d", withDisadvantage);
        MODIFIERS.put("dis", withDisadvantage);
    }

    @Override
    public Result roll(
            CommandInterpreterContext context,
            RollExpression expression,
            Result argument1,
            Result argument2,
            List<ModifierArgument> modifiers
    ) {
        RollData rollData = new RollData(expression);

        rollData.nDice = argument1 == null ? 1 : argument1.getValue();
        rollData.nFacets = argument2.getValue();

        for (ModifierArgument modifier : modifiers) {
            String modifierName = modifier.getModifier();
            ModifierHandler handler = MODIFIERS.get(modifierName);
            if (handler == null) {
                throw new EvaluationErrorException("Unknown modifier: '" + modifierName + "'");
            }

            handler.handle(rollData, modifier.getArgument());
        }

        checkRoll(rollData);

        Result[] dice = rollDice(context.getRandom(), rollData);

//        if (rollData.rollAndKeep != null) {
//            return rollAndKeep(dice);
//        } else if (rollData.success != null || rollData.failure != null) {
//            return rollSuccessesAndFailures(dice);
//        } else {
//            return rollSum(dice, rollData);
//        }
        return rollSum(dice, rollData);
    }

    private void checkRoll(RollData rollData) {
        if (rollData.openEndedOperation != null) {
            if (rollData.openEndedOperation.is(1) && rollData.openEndedOperation.is(rollData.nFacets)) {
                throw new EvaluationErrorException(
                        "Infinite open-ended roll: '" + rollData.rollExpression.getText() + "'"
                );
            }
        }
    }

    private Result rollSum(Result[] dice, RollData rollData) {
        int sum = Arrays.stream(dice).mapToInt(Result::getValue).sum();

        return new Result(
                sum,
                b -> {
                    b.append("(").append(rollData.rollExpression.getText()).append(":");
                    if (rollData.nDice == 1) {
                        b.append(sum);
                    } else {
                        boolean isFirst = true;
                        for (Result d : dice) {
                            if (isFirst) {
                                isFirst = false;
                            } else {
                                b.append("+");
                            }
                            b.append(d.getExplanation());
                        }
                        b.append(" = ").append(sum);
                    }
                    b.append(")");
                }
        );
    }

    @NotNull
    private Result[] rollDice(Random random, RollData rollData) {
        Result[] dice = new Result[rollData.nDice];
        for (int i = 0; i < rollData.nDice; ++i) {
            dice[i] = roll1(random, rollData);
        }
        return dice;
    }

    private Result roll1(Random random, RollData rollData) {
        int value = 0;
        boolean canReroll = true;
        boolean hadRerolls = false;
        boolean hadAdditives = false;
        MessageTextBuilder explanation = new MessageTextBuilder();

        for (;;) {
            int die = Rolls.rollInt(random, rollData.nFacets);

            if (hadAdditives) {
                explanation.append("+");
            } else if (hadRerolls) {
                explanation.append(",");
            }

            if (canReroll && rollData.rerollOperation != null && rollData.rerollOperation.is(die)) {
                hadRerolls = true;
                explanation.strikeout(die);
                continue;
            }

            canReroll = false;
            hadAdditives = true;

            explanation.append(die);
            value += die;

            if (rollData.openEndedOperation == null || !rollData.openEndedOperation.is(die)) {
                break;
            }
        }

        return new Result(value, explanation);
    }
}
