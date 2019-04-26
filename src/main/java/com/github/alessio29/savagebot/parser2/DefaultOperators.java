package com.github.alessio29.savagebot.parser2;

import com.github.alessio29.savagebot.parser2.desugar.Names;
import com.github.alessio29.savagebot.parser2.tree.expressions.CallExpression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DefaultOperators {
    private DefaultOperators() {
    }

    public abstract static class Operator implements Function {
        private final String image;

        public Operator(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public abstract String getName();
    }

    public static class InfixOperator extends Operator {
        public interface Fn {
            int eval(int x1, int x2);
        }

        private final Fn fn;

        public InfixOperator(String image, Fn fn) {
            super(image);
            this.fn = fn;
        }

        @Override
        public String getName() {
            return Names.desugarInfixOperatorName(getImage());
        }

        @Override
        public Result call(CommandInterpreterContext context, CallExpression expression, List<Result> arguments) {
            if (arguments.size() != 2) {
                throw new EvaluationErrorException("2 arguments expected for '" + getImage() + "', got " + arguments.size());
            }
            Result arg1 = arguments.get(0);
            Result arg2 = arguments.get(1);

            return new Result(
                    fn.eval(arg1.getValue(), arg2.getValue()),
                    b -> b.append(arg1.getExplanation()).append(getImage()).append(arg2.getExplanation())
            );
        }
    }

    public static final InfixOperator PLUS = new InfixOperator("+", Integer::sum);

    public static final InfixOperator MINUS = new InfixOperator("-", (x1, x2) -> x1 - x2);

    public static final InfixOperator MUL = new InfixOperator("*", (x1, x2) -> x1 * x2);

    public static final InfixOperator DIV = new InfixOperator(
            "/",
            (x1, x2) -> {
                if (x2 == 0) {
                    throw new EvaluationErrorException("Division by 0");
                }
                return x1 / x2;
            }
    );

    public static final InfixOperator MOD = new InfixOperator(
            "%",
            (x1, x2) -> {
                if (x2 == 0) {
                    throw new EvaluationErrorException("Division by 0");
                }
                return x1 % x2;
            }
    );

    public static class PrefixOperator extends Operator {
        public interface Fn {
            int eval(int x);
        }

        private final Fn fn;

        public PrefixOperator(String image, Fn fn) {
            super(image);
            this.fn = fn;
        }

        @Override
        public String getName() {
            return Names.desugarPrefixOperatorName(getImage());
        }

        @Override
        public Result call(CommandInterpreterContext context, CallExpression expression, List<Result> arguments) {
            if (arguments.size() != 1) {
                throw new EvaluationErrorException("1 argument expected for '" + getImage() + "', got " + arguments.size());
            }
            Result arg = arguments.get(0);

            return new Result(
                    fn.eval(arg.getValue()),
                    b -> b.append(getImage()).append(arg.getExplanation())
            );
        }
    }

    public static final PrefixOperator UNARY_PLUS = new PrefixOperator("+", x -> x);

    public static final PrefixOperator UNARY_MINUS = new PrefixOperator("-", x -> -x);

    public static final Map<String, Function> OPERATORS = new HashMap<>();

    private static void register(Operator operator) {
        OPERATORS.put(operator.getName(), operator);
    }

    static {
        register(PLUS);
        register(MINUS);
        register(MUL);
        register(DIV);
        register(MOD);
        register(UNARY_PLUS);
        register(UNARY_MINUS);
    }
}
