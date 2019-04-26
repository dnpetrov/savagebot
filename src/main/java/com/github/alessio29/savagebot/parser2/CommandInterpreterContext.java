package com.github.alessio29.savagebot.parser2;

import com.github.alessio29.savagebot.parser2.tree.expressions.Expression;

import java.util.Map;
import java.util.Random;

public class CommandInterpreterContext {
    private final Random random;
    private final Map<String, Expression> userVariables;
    private final Map<String, Expression> globalVariables;
    private final Map<String, Function> functions;
    private final Map<String, RollMethod> rollMethods;

    public CommandInterpreterContext(
            Random random,
            Map<String, Expression> localVariables,
            Map<String, Expression> globalVariables,
            Map<String, Function> functions,
            Map<String, RollMethod> rollMethods
    ) {
        this.random = random;
        this.userVariables = localVariables;
        this.globalVariables = globalVariables;
        this.functions = functions;
        this.rollMethods = rollMethods;
    }

    public Random getRandom() {
        return random;
    }

    public Expression getVariable(String name) {
        Expression result = userVariables.get(name);
        if (result != null) return result;
        return globalVariables.get(name);
    }

    public void setUserVariable(String name, Expression value) {
        userVariables.put(name, value);
    }

    public void setGlobalVariable(String name, Expression value) {
        globalVariables.put(name, value);
    }

    public Function getFunction(String name) {
        return functions.get(name);
    }

    public RollMethod getRollMethod(String name) {
        return rollMethods.get(name);
    }
}
