package com.ikalagaming.scripting;

import com.ikalagaming.scripting.interpreter.ScriptRuntime;

import lombok.NonNull;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tests starting scripts through the script manager, and resuming them with values, on the script
 * runner thread.
 */
class TestScriptLaunch {

    /** How long to wait for the runner to do something. */
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    /** How long to watch for something that should not happen. */
    private static final Duration QUIET_PERIOD = Duration.ofMillis(300);

    /** The owner of scripts started by these tests. */
    private static final String OWNER = "launch-test";

    /** Scripts started by a test, terminated after it. */
    private final List<ScriptRuntime> started = new ArrayList<>();

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

    /** Stop anything still running, and clear output. */
    @AfterEach
    void afterEach() {
        started.forEach(ScriptManager::terminate);
        ScriptManager.terminateAllOwnedBy(TestScriptLaunch.OWNER);
        ScriptManager.unregisterGlobal("who");
        ScriptManager.unregisterGlobal("probe");
        DebugMethods.reset();
    }

    /**
     * Start a script owned by the test owner.
     *
     * @param program The script.
     * @return The running script.
     */
    private ScriptRuntime start(@NonNull String program) {
        final Optional<ScriptRuntime> runtime =
                ScriptManager.start(ScriptLaunch.source(program).owner(TestScriptLaunch.OWNER));
        Assertions.assertTrue(runtime.isPresent(), () -> "Should start: " + program);
        started.add(runtime.get());
        return runtime.get();
    }

    /**
     * Wait until a script is parked, waiting on a tag.
     *
     * @param runtime The script.
     * @param tag The tag, or an empty string for yields without a tag.
     */
    private void awaitParked(@NonNull ScriptRuntime runtime, @NonNull String tag) {
        Awaitility.await()
                .atMost(TestScriptLaunch.TIMEOUT)
                .until(() -> tag.equals(ScriptManager.getYieldedScripts().get(runtime)));
    }

    /**
     * Wait until the output exactly matches the expected values.
     *
     * @param expected The expected output.
     */
    private void awaitOutput(String... expected) {
        Awaitility.await()
                .atMost(TestScriptLaunch.TIMEOUT)
                .until(() -> List.of(expected).equals(List.copyOf(DebugMethods.getOutput())));
    }

    /**
     * Check that the output stays exactly the expected values for a while.
     *
     * @param expected The expected output.
     */
    private void assertOutputStays(String... expected) {
        Awaitility.await()
                .during(TestScriptLaunch.QUIET_PERIOD)
                .atMost(TestScriptLaunch.QUIET_PERIOD.multipliedBy(4))
                .until(() -> List.of(expected).equals(List.copyOf(DebugMethods.getOutput())));
    }

    /** A targeted resume delivers its value to the script's await. */
    @Test
    void testResumeWithValue() {
        final ScriptRuntime runtime = start("int c = await(\"pick\"); TEST_printInt(c);");
        awaitParked(runtime, "pick");
        ScriptManager.resume(runtime, "pick", 9);
        awaitOutput("9");
    }

    /** A targeted resume that arrives before the await is kept, so it isn't missed. */
    @Test
    void testEarlyResumeIsKept() {
        final ScriptRuntime runtime = start("yield(); int c = await(\"pick\"); TEST_printInt(c);");
        awaitParked(runtime, "");
        ScriptManager.resume(runtime, "pick", 3);
        ScriptManager.resume();
        awaitOutput("3");
    }

    /** A targeted resume only resumes that script. */
    @Test
    void testTargetedResumeOnlyResumesOne() {
        final ScriptRuntime first = start("TEST_printInt(await(\"pick\"));");
        final ScriptRuntime second = start("TEST_printInt(await(\"pick\") + 100);");
        awaitParked(first, "pick");
        awaitParked(second, "pick");

        ScriptManager.resume(first, "pick", 1);
        awaitOutput("1");
        assertOutputStays("1");

        ScriptManager.resume(second, "pick", 2);
        awaitOutput("1", "102");
    }

    /** A broadcast resume gives its value to every script waiting on the tag. */
    @Test
    void testBroadcastResumeWithValue() {
        final ScriptRuntime first = start("TEST_printInt(await(\"all\"));");
        final ScriptRuntime second = start("TEST_printInt(await(\"all\"));");
        awaitParked(first, "all");
        awaitParked(second, "all");
        ScriptManager.resume("all", 4);
        awaitOutput("4", "4");
    }

    /** A broadcast with nobody waiting is dropped, as it always was. */
    @Test
    void testBroadcastWithNobodyWaitingIsDropped() {
        final ScriptRuntime runtime = start("yield(); TEST_printInt(await(\"pick\"));");
        awaitParked(runtime, "");
        ScriptManager.resume("pick", 1);
        ScriptManager.resume();
        awaitParked(runtime, "pick");
        assertOutputStays();

        ScriptManager.resume("pick", 2);
        awaitOutput("2");
    }

    /** Scripts can start at a label. */
    @Test
    void testStartAtLabel() {
        final Optional<ScriptRuntime> runtime =
                ScriptManager.start(
                        ScriptLaunch.source(
                                        "TEST_printString(\"a\"); next: TEST_printString(\"b\");")
                                .owner(TestScriptLaunch.OWNER)
                                .at("next"));
        Assertions.assertTrue(runtime.isPresent());
        awaitOutput("b");
    }

    /** An unknown entry label fails to start. */
    @Test
    void testUnknownLabelFailsToStart() {
        Assertions.assertTrue(
                ScriptManager.start(
                                ScriptLaunch.source("TEST_printString(\"a\");")
                                        .owner(TestScriptLaunch.OWNER)
                                        .at("missing"))
                        .isEmpty());
    }

    /** A launch can add its own globals. */
    @Test
    void testLaunchGlobal() {
        Assertions.assertTrue(
                ScriptManager.start(
                                ScriptLaunch.source("TEST_printString(event);")
                                        .owner(TestScriptLaunch.OWNER)
                                        .global("event", "clicked"))
                        .isPresent());
        awaitOutput("clicked");
    }

    /** Provided globals are made for the plugin that owns each script. */
    @Test
    void testProvidedGlobalPerOwner() {
        ScriptManager.registerGlobal("who", "provider", owner -> "made for " + owner);
        start("TEST_printString(who);");
        awaitOutput("made for " + TestScriptLaunch.OWNER);
    }

    /** A global can't be registered twice, or over a standard class. */
    @Test
    void testDuplicateGlobal() {
        ScriptManager.registerGlobal("who", "provider", owner -> owner);
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScriptManager.registerGlobal("who", "other", owner -> owner));
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScriptManager.registerGlobal("Math", "other", owner -> owner));
    }

    /** Unloading a plugin stops its scripts, drops what they hold, and removes its globals. */
    @Test
    void testTerminateAllOwnedBy() {
        ScriptManager.registerGlobal("probe", TestScriptLaunch.OWNER, owner -> new Object());
        final ScriptRuntime runtime = start("Object held = probe; int c = await(\"never\");");
        awaitParked(runtime, "never");

        ScriptManager.terminateAllOwnedBy(TestScriptLaunch.OWNER);

        Assertions.assertTrue(runtime.hasTerminated());
        Assertions.assertTrue(runtime.getSymbolTable().isEmpty(), "Its variables should be gone");
        Assertions.assertFalse(ScriptManager.getYieldedScripts().containsKey(runtime));
        Assertions.assertFalse(ScriptManager.globalsFor(null).containsKey("probe"));
    }

    /** Other plugins' scripts keep running when one plugin unloads. */
    @Test
    void testTerminateOnlyTheOwner() {
        final Optional<ScriptRuntime> other =
                ScriptManager.start(
                        ScriptLaunch.source("TEST_printInt(await(\"keep\"));").owner("other"));
        Assertions.assertTrue(other.isPresent());
        started.add(other.get());
        awaitParked(other.get(), "keep");

        ScriptManager.terminateAllOwnedBy(TestScriptLaunch.OWNER);
        ScriptManager.resume(other.get(), "keep", 5);
        awaitOutput("5");
    }

    /**
     * Script files are compiled once, and again when they change.
     *
     * @param folder A folder for the script.
     * @throws IOException If the file can't be written.
     */
    @Test
    void testChangedFileIsRecompiled(@TempDir Path folder) throws IOException {
        final Path file = folder.resolve("cached.iks");
        Files.writeString(file, "TEST_printString(\"one\");");
        Assertions.assertTrue(
                ScriptManager.start(ScriptLaunch.file(file).owner(TestScriptLaunch.OWNER))
                        .isPresent());
        awaitOutput("one");

        Files.writeString(file, "TEST_printString(\"two\");");
        // File times can be coarse, so make sure the change is visible
        Files.setLastModifiedTime(
                file, FileTime.fromMillis(Files.getLastModifiedTime(file).toMillis() + 2000));
        Assertions.assertTrue(
                ScriptManager.start(ScriptLaunch.file(file).owner(TestScriptLaunch.OWNER))
                        .isPresent());
        awaitOutput("one", "two");
    }
}
