package com.ikalagaming.scripting;

import com.ikalagaming.scripting.interpreter.ScriptRuntime;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tests the functionality of the scripting engine class.
 *
 * @author Ches Burks
 */
class TestBehavior {

    /** Tries to execute the fizzbuzz program. */
    @Test
    void testFizzBuzz() {
        List<String> expected = new ArrayList<>();

        for (int i = 1; i <= 100; ++i) {
            String result;
            if (i % 3 == 0 && i % 5 == 0) {
                result = "FizzBuzz";
            } else if (i % 3 == 0) {
                result = "Fizz";
            } else if (i % 5 == 0) {
                result = "Buzz";
            } else {
                result = "" + i;
            }
            expected.add(result);
        }

        final String program =
                """
			for (int i = 1; i <= 100; ++i) {
				string result;
				if (i % 3 == 0 && i % 5 == 0) {
					result = "FizzBuzz";
				}
				else if (i % 3 == 0) {
					result = "Fizz";
				}
				else if (i % 5 == 0) {
					result = "Buzz";
				}
				else {
					result = "" + i;
				}
				TEST_printString(result);
			}
			""";

        CharStream stream = CharStreams.fromString(program);
        Optional<ScriptRuntime> maybeRuntime = IkalaScriptCompiler.parse(stream);

        ScriptManager.registerClass(DebugMethods.class);

        Assertions.assertTrue(maybeRuntime.isPresent());
        ScriptRuntime runtime = maybeRuntime.get();

        // Sanity check so builds finish
        final int MAX_INSTRUCTIONS = 10_000;
        int instructions = 0;
        while (!runtime.hasTerminated() && instructions < MAX_INSTRUCTIONS) {
            runtime.step();
            ++instructions;
        }

        List<String> actual = DebugMethods.getOutput();
        Assertions.assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); ++i) {
            Assertions.assertEquals(
                    expected.get(i), actual.get(i), String.format("Value %d should match", i));
        }

        DebugMethods.reset();
    }

    /** Tests some method calls. */
    @Test
    void testMethodCalls() {
        final String program =
                """
			double d;
			String s;
			int i = TEST_getInt();
			s = TEST_getString();
			d = TEST_getDouble();
			TestObject o = TEST_getObject();
			o.setInteger(i);
			o.setString(s);
			o.setDoub(d);
			TEST_checkValues(s, i, d, o);
			""";

        CharStream stream = CharStreams.fromString(program);
        Optional<ScriptRuntime> maybeRuntime = IkalaScriptCompiler.parse(stream);

        ScriptManager.registerClass(DebugMethods.class);

        Assertions.assertTrue(maybeRuntime.isPresent());
        ScriptRuntime runtime = maybeRuntime.get();

        // Sanity check so builds finish
        final int MAX_INSTRUCTIONS = 10_000;
        int instructions = 0;
        while (!runtime.hasTerminated() && instructions < MAX_INSTRUCTIONS) {
            runtime.step();
            ++instructions;
        }

        Assertions.assertTrue(DebugMethods.isCheckValuesMatched());

        DebugMethods.reset();
    }
}
