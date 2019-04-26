package com.github.alessio29.savagebot.parser2.desugar;

public final class Names {
    private Names() {}

    public static String desugarVariableName(String sourceVariableName) {
        if (sourceVariableName.startsWith("${") && sourceVariableName.endsWith("}")) {
            return sourceVariableName.substring(2, sourceVariableName.length() - 1);
        }
        else if (sourceVariableName.startsWith("$")) {
            return sourceVariableName.substring(1);
        } else {
            throw new DesugarErrorException("Unexpected variable name: '" + sourceVariableName + "'");
        }
    }

    public static String desugarInfixOperatorName(String operator) {
        return ":" + operator + ":";
    }

    public static String desugarPrefixOperatorName(String operator) {
        return operator + ":";
    }
}
