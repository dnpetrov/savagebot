package com.github.alessio29.savagebot.parser2;

import com.github.alessio29.savagebot.parser2.tree.expressions.CallExpression;

import java.util.List;

public interface Function {
    Result call(
            CommandInterpreterContext context,
            CallExpression expression,
            List<Result> arguments
    );
}
