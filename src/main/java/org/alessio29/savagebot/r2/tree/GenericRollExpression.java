package org.alessio29.savagebot.r2.tree;

import java.util.HashMap;
import java.util.Map;

public class GenericRollExpression extends Expression {
    public enum SuffixOperator {
        KEEP("k", "K"),
        KEEP_LEAST("kl", "KL"),
        ADVANTAGE("a", "adv"),
        DISADVANTAGE("d", "dis");

        private final String image;
        private final String[] aliases;

        SuffixOperator(String image, String... aliases) {
            this.image = image;
            this.aliases = aliases;
        }

        public String getImage() {
            return image;
        }

        public String[] getAliases() {
            return aliases;
        }
    }

    private static final Map<String, SuffixOperator> SUFFIX_OPERATORS = new HashMap<>();
    static {
        for (SuffixOperator operator : SuffixOperator.values()) {
            SUFFIX_OPERATORS.put(operator.getImage(), operator);
            for (String alias : operator.aliases) {
                SUFFIX_OPERATORS.put(alias, operator);
            }
        }
    }

    public static SuffixOperator getSuffixOperator(String image) {
        return SUFFIX_OPERATORS.get(image);
    }

    private final Expression diceCountArg;
    private final Expression facetsCountArg;
    private final boolean isOpenEnded;
    private final SuffixOperator suffixOperator;
    private final Expression suffixArg;

    public GenericRollExpression(
            String text,
            Expression diceCountArg,
            Expression facetsCountArg,
            boolean isOpenEnded,
            SuffixOperator suffixOperator,
            Expression suffixArg
    ) {
        super(text);
        this.diceCountArg = diceCountArg;
        this.facetsCountArg = facetsCountArg;
        this.isOpenEnded = isOpenEnded;
        this.suffixOperator = suffixOperator;
        this.suffixArg = suffixArg;
    }

    public GenericRollExpression(
            String text,
            Expression diceCountArg,
            Expression facetsCountArg,
            boolean isOpenEnded
    ) {
        this(text, diceCountArg, facetsCountArg, isOpenEnded, null, null);
    }

    public Expression getDiceCountArg() {
        return diceCountArg;
    }

    public Expression getFacetsCountArg() {
        return facetsCountArg;
    }

    public boolean isOpenEnded() {
        return isOpenEnded;
    }

    public SuffixOperator getSuffixOperator() {
        return suffixOperator;
    }

    public Expression getSuffixArg() {
        return suffixArg;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitGenericRollExpression(this);
    }
}
