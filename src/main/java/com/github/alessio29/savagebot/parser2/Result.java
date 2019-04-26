package com.github.alessio29.savagebot.parser2;

import com.github.alessio29.savagebot.internal.MessageTextBuilder;
import com.github.alessio29.savagebot.internal.MessageTextElement;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class Result implements MessageTextElement {
    private final int value;
    private final boolean withValue;
    private final MessageTextElement explanation;

    public Result(int value, @NotNull MessageTextElement explanation) {
        this.value = value;
        this.withValue = true;
        this.explanation = explanation;
    }

    public Result(@NotNull MessageTextElement explanation) {
        this.value = 0;
        this.withValue = false;
        this.explanation = explanation;
    }

    public int getValue() {
        return value;
    }

    public boolean isWithValue() {
        return withValue;
    }

    @NotNull
    public MessageTextElement getExplanation() {
        return explanation;
    }

    @Override
    public void appendTo(MessageTextBuilder builder) {
        explanation.appendTo(builder);
        if (withValue) {
            builder.append(" => ").bold(value);
        }
    }

    public static final Comparator<Result> BY_VALUE = Comparator.comparingInt(Result::getValue);
}
