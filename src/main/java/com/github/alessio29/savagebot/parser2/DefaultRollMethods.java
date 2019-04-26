package com.github.alessio29.savagebot.parser2;

import java.util.HashMap;
import java.util.Map;

public class DefaultRollMethods {
    public static final Map<String, RollMethod> ROLL_METHODS = new HashMap<>();

    static {
        ROLL_METHODS.put("d", new BasicNdMRollMethod());
    }
}
