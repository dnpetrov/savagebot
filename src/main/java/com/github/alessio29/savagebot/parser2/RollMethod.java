package com.github.alessio29.savagebot.parser2;

import com.github.alessio29.savagebot.parser2.tree.expressions.RollExpression;

import java.util.List;

public interface RollMethod {
    class ModifierArgument {
        private final String modifier;
        private final Result argument;

        public ModifierArgument(String modifier, Result argument) {
            this.modifier = modifier;
            this.argument = argument;
        }

        public String getModifier() {
            return modifier;
        }

        public Result getArgument() {
            return argument;
        }
    }

    Result roll(
            CommandInterpreterContext context,
            RollExpression expression,
            Result argument1,
            Result argument2,
            List<ModifierArgument> modifiers
    );
}
