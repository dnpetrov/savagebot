package com.github.alessio29.savagebot.parser2;

import java.util.Random;

public final class Rolls {
    private Rolls() {}

    public static int rollInt(Random random, int nFacets) {
        return random.nextInt(nFacets) + 1;
    }

    public static Result roll(Random random, int nFacets) {
        int i = rollInt(random, nFacets);
        return new Result(i, b -> b.append(i));
    }

    public static Result rollOpenEnded(Random random, int nFacets) {
        if (nFacets <= 1) {
            throw new EvaluationErrorException("Number of facets in an open-ended roll should be at least 2: " + nFacets);
        }

        StringBuilder sb = new StringBuilder();
        int sum = 0;
        for (;;) {
            int n = rollInt(random, nFacets);
            sum += n;
            sb.append(n);
            if (n == nFacets) {
                sb.append("+");
            } else {
                break;
            }
        }

        return new Result(sum, b -> b.append(sb.toString()));
    }
}
