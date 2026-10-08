package com.ikalagaming.scripting;

import com.ikalagaming.scripting.ast.Node;

import lombok.NonNull;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import java.util.List;
import java.util.function.Supplier;

/**
 * Reports problems with scripts, like syntax errors or runtime errors. Problems are always logged,
 * and can also be collected on the current thread so that tools like a script debugger can show
 * them.
 *
 * @author Ches Burks
 */
public final class ScriptDiagnostics {

    /**
     * A problem with a script.
     *
     * @param line The line in the source the problem is on, starting at 1, or -1 if unknown.
     * @param column The column in the line the problem is at, starting at 1, or -1 if unknown.
     * @param message A description of the problem, without the location.
     */
    public record Diagnostic(int line, int column, @NonNull String message) {

        /**
         * Describe where the problem is.
         *
         * @return Something like "Line 2, column 5", or an empty string if the line is unknown.
         */
        public String location() {
            if (line <= 0) {
                return "";
            }
            return column > 0
                    ? String.format("Line %d, column %d", line, column)
                    : String.format("Line %d", line);
        }

        @Override
        public String toString() {
            final String where = location();
            return where.isEmpty() ? message : where + ": " + message;
        }
    }

    /** The problems being collected on each thread, null if not collecting. */
    private static final ThreadLocal<List<Diagnostic>> COLLECTOR = new ThreadLocal<>();

    /**
     * Run something, collecting any problems that are reported on this thread while it runs.
     * Collection can be nested, the innermost collector gets the problems.
     *
     * @param problems The list to add problems to.
     * @param action The action to run.
     * @param <T> The return type of the action.
     * @return The result of the action.
     */
    public static <T> T collect(@NonNull List<Diagnostic> problems, @NonNull Supplier<T> action) {
        final List<Diagnostic> previous = COLLECTOR.get();
        COLLECTOR.set(problems);
        try {
            return action.get();
        } finally {
            if (previous == null) {
                COLLECTOR.remove();
            } else {
                COLLECTOR.set(previous);
            }
        }
    }

    /**
     * Run something, collecting any problems that are reported on this thread while it runs.
     *
     * @param problems The list to add problems to.
     * @param action The action to run.
     * @see #collect(List, Supplier)
     */
    public static void collect(@NonNull List<Diagnostic> problems, @NonNull Runnable action) {
        collect(
                problems,
                () -> {
                    action.run();
                    return null;
                });
    }

    /**
     * Report a problem with a script, when we don't know where in the script it is. This is logged
     * as a warning, and collected if something is collecting problems on this thread.
     *
     * @param log The logger to log to.
     * @param format The message format, using slf4j style {} placeholders.
     * @param args The arguments for the message.
     */
    public static void warn(@NonNull Logger log, String format, Object... args) {
        warnAt(log, -1, -1, format, args);
    }

    /**
     * Report a problem with part of a script. This is logged as a warning, and collected if
     * something is collecting problems on this thread.
     *
     * @param log The logger to log to.
     * @param node The part of the syntax tree with the problem, which provides the line.
     * @param format The message format, using slf4j style {} placeholders.
     * @param args The arguments for the message.
     */
    public static void warnAt(
            @NonNull Logger log, @NonNull Node node, String format, Object... args) {
        warnAt(log, node.getLine(), -1, format, args);
    }

    /**
     * Report a problem with part of the parsed script, before there is a syntax tree. This is
     * logged as a warning, and collected if something is collecting problems on this thread.
     *
     * @param log The logger to log to.
     * @param context The part of the parse tree with the problem, which provides the position.
     * @param format The message format, using slf4j style {} placeholders.
     * @param args The arguments for the message.
     */
    public static void warnAt(
            @NonNull Logger log,
            @NonNull ParserRuleContext context,
            String format,
            Object... args) {
        final Token start = context.getStart();
        if (start == null) {
            warnAt(log, -1, -1, format, args);
            return;
        }
        warnAt(log, start.getLine(), start.getCharPositionInLine() + 1, format, args);
    }

    /**
     * Report a problem at a specific place in a script. This is logged as a warning, and collected
     * if something is collecting problems on this thread.
     *
     * @param log The logger to log to.
     * @param line The line in the source, starting at 1, or -1 if unknown.
     * @param column The column in the line, starting at 1, or -1 if unknown.
     * @param format The message format, using slf4j style {} placeholders.
     * @param args The arguments for the message.
     */
    public static void warnAt(
            @NonNull Logger log, int line, int column, String format, Object... args) {
        final Diagnostic problem =
                new Diagnostic(
                        line, column, MessageFormatter.arrayFormat(format, args).getMessage());
        // Keep the exception, if there is one
        final Throwable throwable = MessageFormatter.getThrowableCandidate(args);
        if (throwable != null) {
            log.warn(problem.toString(), throwable);
        } else {
            log.warn(problem.toString());
        }
        final List<Diagnostic> problems = COLLECTOR.get();
        if (problems != null) {
            problems.add(problem);
        }
    }

    /** Private constructor so that this class is not instantiated. */
    private ScriptDiagnostics() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
