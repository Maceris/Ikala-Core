package com.ikalagaming.scripting;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

/**
 * Tests running scripts on the script runner thread, including yielding and resuming.
 *
 * @author Ches Burks
 */
class TestScriptRunner {

    /** How long to wait for the runner to do something. */
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    /** How long to watch for something that should not happen. */
    private static final Duration QUIET_PERIOD = Duration.ofMillis(300);

    /** Shut down the runner thread. */
    @AfterAll
    static void afterAll() {
        ScriptManager.shutdown();
    }

    /** Register the debug methods. */
    @BeforeAll
    static void beforeAll() {
        ScriptManager.registerClass(DebugMethods.class);
    }

    /** Clear out recorded output. */
    @AfterEach
    void afterEach() {
        // Release anything still waiting, so it doesn't leak into other tests
        ScriptManager.resume();
        ScriptManager.resume("tag");
        DebugMethods.reset();
    }

    /**
     * Wait until the output exactly matches the expected values.
     *
     * @param expected The expected output.
     */
    private void awaitOutput(String... expected) {
        Awaitility.await()
                .atMost(TestScriptRunner.TIMEOUT)
                .until(() -> List.of(expected).equals(List.copyOf(DebugMethods.getOutput())));
    }

    /**
     * Check that the output stays exactly the expected values for a while.
     *
     * @param expected The expected output.
     */
    private void assertOutputStays(String... expected) {
        Awaitility.await()
                .during(TestScriptRunner.QUIET_PERIOD)
                .atMost(TestScriptRunner.QUIET_PERIOD.multipliedBy(4))
                .until(() -> List.of(expected).equals(List.copyOf(DebugMethods.getOutput())));
    }

    /** Scripts that fail to compile are reported, and not run. */
    @Test
    void testInvalidScript() {
        Assertions.assertFalse(ScriptManager.runScript("int x = \"not an int\";"));
    }

    /** A script that errors should not stop other scripts from running. */
    @Test
    void testErrorDoesNotBlockOthers() {
        Assertions.assertTrue(ScriptManager.runScript("int z = 0; int x = 1 / z;"));
        Assertions.assertTrue(ScriptManager.runScript("TEST_printString(\"still running\");"));
        awaitOutput("still running");
    }

    /** Scripts run on the runner thread without needing to be stepped manually. */
    @Test
    void testRunScript() {
        Assertions.assertTrue(
                ScriptManager.runScript("for (int i = 0; i < 3; i++) { TEST_printInt(i); }"));
        awaitOutput("0", "1", "2");
    }

    /** Scripts can be restarted after the runner has been shut down. */
    @Test
    void testRunAfterShutdown() {
        ScriptManager.shutdown();
        Assertions.assertTrue(ScriptManager.runScript("TEST_printString(\"restarted\");"));
        awaitOutput("restarted");
    }

    /** Yielding without a tag waits until resumed without a tag. */
    @Test
    void testYield() {
        Assertions.assertTrue(
                ScriptManager.runScript(
                        "TEST_printString(\"before\"); yield(); TEST_printString(\"after\");"));
        awaitOutput("before");
        assertOutputStays("before");

        // Resuming a tag shouldn't resume untagged scripts
        ScriptManager.resume("tag");
        assertOutputStays("before");

        ScriptManager.resume();
        awaitOutput("before", "after");
    }

    /** Yielding inside a loop, repeatedly. */
    @Test
    void testYieldInLoop() {
        Assertions.assertTrue(
                ScriptManager.runScript(
                        "for (int i = 0; i < 3; i++) { TEST_printInt(i); yield(\"tag\"); }"
                                + " TEST_printString(\"done\");"));
        awaitOutput("0");
        ScriptManager.resume("tag");
        awaitOutput("0", "1");
        ScriptManager.resume("tag");
        awaitOutput("0", "1", "2");
        ScriptManager.resume("tag");
        awaitOutput("0", "1", "2", "done");
    }

    /** Yielding with a tag waits until resumed with that specific tag. */
    @Test
    void testYieldWithTag() {
        Assertions.assertTrue(
                ScriptManager.runScript(
                        "TEST_printString(\"before\"); yield(\"tag\"); TEST_printString(\"after\");"));
        awaitOutput("before");
        assertOutputStays("before");

        ScriptManager.resume();
        ScriptManager.resume("other tag");
        assertOutputStays("before");

        ScriptManager.resume("tag");
        awaitOutput("before", "after");
    }
}
