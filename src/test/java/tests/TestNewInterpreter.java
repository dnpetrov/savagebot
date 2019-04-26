package tests;

import com.github.alessio29.savagebot.parser2.CommandInterpreter;
import com.github.alessio29.savagebot.parser2.CommandInterpreterContext;
import com.github.alessio29.savagebot.parser2.DefaultOperators;
import com.github.alessio29.savagebot.parser2.DefaultRollMethods;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

public class TestNewInterpreter {
    @Test
    public void testSimpleArithmetic() {
        shouldEvaluateTo("2 => **2**", "2");
        shouldEvaluateTo("-2 => **-2**", "-2");
        shouldEvaluateTo("+2 => **2**", "+2");
        shouldEvaluateTo("1+2 => **3**", "1+2");
        shouldEvaluateTo("2*2 => **4**", "2*2");
        shouldEvaluateTo("6/2 => **3**", "6/2");
        shouldEvaluateTo("11%2 => **1**", "11%2");
        shouldEvaluateTo(
                "1 => **1**\n" +
                        "2 => **2**\n" +
                        "3 => **3**\n" +
                        "4 => **4**\n" +
                        "5 => **5**",
                "1 2 3 4 5"
        );
    }

    @Test
    public void testStatementSeparation() {
        shouldEvaluateTo("2-2 => **0**", "2-2");
        shouldEvaluateTo("2 => **2**\n-2 => **-2**", "2;-2");
        shouldEvaluateTo("2 => **2**\n-2 => **-2**", "2 -2");
        shouldEvaluateTo("2 => **2**\n-2 => **-2**", "2; -2");
    }

    @Test
    public void testRollAndKeepSum() {
        shouldEvaluateTo("(d6:1) => **1**", "d6");
        shouldEvaluateTo("(2d6:1+5=6) => **6**", "2d6");
        shouldEvaluateTo("(2d6:1+5=6)+3 => **9**", "2d6+3");
        shouldEvaluateTo("(2d6:1+5=6)+3+1 => **10**", "2d6+3+1");
        shouldEvaluateTo("(4d6k3:~~1~~+2+5+6=13) => **13**", "4d6k3");
        shouldEvaluateTo("(4d6kl3:1+2+5+~~6~~=8) => **8**", "4d6kl3");
        shouldEvaluateTo("(d20a:~~1~~+9=9) => **9**", "d20a");
        shouldEvaluateTo("(d20a:~~1~~+9=9)+4 => **13**", "d20a+4");
        shouldEvaluateTo("(d20a1:~~1~~+9=9) => **9**", "d20a1");
        shouldEvaluateTo("(d20adv1:~~1~~+9=9) => **9**", "d20adv1");
        shouldEvaluateTo("(d20d:1+~~9~~=1) => **1**", "d20d");
        shouldEvaluateTo("(d20d1:1+~~9~~=1) => **1**", "d20d1");
        shouldEvaluateTo("(d20dis1:1+~~9~~=1) => **1**", "d20dis1");
        shouldEvaluateTo("(5d20max:~~1~~,~~8~~,~~9~~,~~10~~,16=16) => **16**", "5d20max");
        shouldEvaluateTo("(5d20min:1,~~8~~,~~9~~,~~10~~,~~16~~=1) => **1**", "5d20min");
    }

    @Test
    public void testSuccessesAndFailures() {
        shouldEvaluateTo("(5d10>8:1,6,8,9,10 => successes: 2) => **2**", "5d10>8");
        shouldEvaluateTo("(5d10>=8:1,6,8,9,10 => successes: 3) => **3**", "5d10>=8");
        shouldEvaluateTo("(5d10<8:1,6,8,9,10 => successes: 2) => **2**", "5d10<8");
        shouldEvaluateTo("(5d10<=8:1,6,8,9,10 => successes: 3) => **3**", "5d10<=8");
        shouldEvaluateTo("(5d10f1>8:1,6,8,9,10 => successes: 2; failures: 1) => **1**", "5d10f1>8");
        shouldEvaluateTo("(5d10f1>=8:1,6,8,9,10 => successes: 3; failures: 1) => **2**", "5d10f1>=8");
        shouldEvaluateTo("(5d10>8f1:1,6,8,9,10 => successes: 2; failures: 1) => **1**", "5d10>8f1");
        shouldEvaluateTo("(5d10>=8f1:1,6,8,9,10 => successes: 3; failures: 1) => **2**", "5d10>=8f1");
    }

    @Test
    public void testMixedInput() {
        shouldEvaluateTo(
                "shooting (d20:1)+3 => **4**\n" +
                        "damage (5d10:4+6+8+9+10=37) => **37**",
                "shooting", "d20+3", "damage", "5d10"
        );
    }

    private void shouldEvaluateTo(String expected, String... args) {
        CommandInterpreterContext context = new CommandInterpreterContext(
                new Random(0),
                new HashMap<>(),
                new HashMap<>(),
                DefaultOperators.OPERATORS,
                DefaultRollMethods.ROLL_METHODS
        );
        CommandInterpreter interpreter = new CommandInterpreter(context);
        Assert.assertEquals(expected, interpreter.run(args).trim());
    }
}
