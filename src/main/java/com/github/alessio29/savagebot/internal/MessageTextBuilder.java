package com.github.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.User;

import static com.github.alessio29.savagebot.internal.Messages.*;

@SuppressWarnings("UnusedReturnValue")
public class MessageTextBuilder implements MessageTextElement {
    private final StringBuilder builder = new StringBuilder();

    public String toString() {
        return builder.toString();
    }

    @Override
    public void appendTo(MessageTextBuilder builder) {
        builder.append(this.builder.toString());
    }

    public MessageTextBuilder newLine() {
        builder.append('\n');
        return this;
    }

    public MessageTextBuilder surrounded(String marker, Object body) {
        builder.append(marker).append(body).append(marker);
        return this;
    }

    public MessageTextBuilder surrounded(String marker, MessageTextElement body) {
        builder.append(marker);
        body.appendTo(this);
        builder.append(marker);
        return this;
    }

    public MessageTextBuilder appendMessage(String messageKind, String message) {
        return bold(b -> b.append(messageKind).append(": ")).append(message).append('\n');
    }

    public MessageTextBuilder appendMessage(String errorKind, MessageTextElement errorMessage) {
        return bold(b -> b.append(errorKind).append(": ")).append(errorMessage).append('\n');
    }

    public MessageTextBuilder mention(User user) {
        return append(user.getAsMention());
    }

    public MessageTextBuilder bold(MessageTextElement body) {
        return surrounded(BOLD_MARKER, body);
    }

    public MessageTextBuilder italic(MessageTextElement body) {
        return surrounded(ITALIC_MARKER, body);
    }

    public MessageTextBuilder underlined(MessageTextElement body) {
        return surrounded(UNDERLINED_MARKER, body);
    }

    public MessageTextBuilder strikeout(MessageTextElement body) {
        return surrounded(STRIKEOUT_MARKER, body);
    }

    public MessageTextBuilder bold(Object body) {
        return surrounded(BOLD_MARKER, body);
    }

    public MessageTextBuilder italic(Object body) {
        return surrounded(ITALIC_MARKER, body);
    }

    public MessageTextBuilder underlined(Object body) {
        return surrounded(UNDERLINED_MARKER, body);
    }

    public MessageTextBuilder strikeout(Object body) {
        return surrounded(STRIKEOUT_MARKER, body);
    }

    public MessageTextBuilder append(MessageTextElement nested) {
        nested.appendTo(this);
        return this;
    }

    public MessageTextBuilder append(Object obj) {
        if (obj instanceof MessageTextElement) {
            ((MessageTextElement) obj).appendTo(this);
        } else {
            builder.append(obj);
        }
        return this;
    }

    public interface Appender<T> {
        void append(MessageTextBuilder builder, T value);
    }

    public MessageTextBuilder append(String str) {
        builder.append(str);
        return this;
    }

    public MessageTextBuilder append(boolean b) {
        builder.append(b);
        return this;
    }

    public MessageTextBuilder append(char c) {
        builder.append(c);
        return this;
    }

    public MessageTextBuilder append(int i) {
        builder.append(i);
        return this;
    }

    public MessageTextBuilder append(long lng) {
        builder.append(lng);
        return this;
    }

    public MessageTextBuilder append(float f) {
        builder.append(f);
        return this;
    }

    public MessageTextBuilder append(double d) {
        builder.append(d);
        return this;
    }

    public MessageTextBuilder appendCodePoint(int codePoint) {
        builder.appendCodePoint(codePoint);
        return this;
    }

    public void clear() {
        builder.setLength(0);
    }
}
