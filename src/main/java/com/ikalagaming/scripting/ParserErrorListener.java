package com.ikalagaming.scripting;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

/**
 * Handles proper logging of errors, tracks how many occurred over both lexing and parsing.
 *
 * <p>If parsing multiple scripts, you will want either a new error listener each time, or to reset
 * the error count before each script, lest failures falsely carry over to the next script.
 *
 * <p>On a similar note, this is also not thread safe, if parsing on multiple threads, you should
 * use multiple instances.
 *
 * @author Ches Burks
 */
@Slf4j
public class ParserErrorListener implements ANTLRErrorListener {

    /**
     * The number of errors that we have seen so far with this object.
     *
     * @see #resetErrorCount()
     */
    @Getter private int errorCount = 0;

    @Override
    public void reportAmbiguity(
            Parser recognizer,
            DFA dfa,
            int startIndex,
            int stopIndex,
            boolean exact,
            BitSet ambigAlts,
            ATNConfigSet configs) {
        // ignored
    }

    @Override
    public void reportAttemptingFullContext(
            Parser recognizer,
            DFA dfa,
            int startIndex,
            int stopIndex,
            BitSet conflictingAlts,
            ATNConfigSet configs) {
        // ignored
    }

    @Override
    public void reportContextSensitivity(
            Parser recognizer,
            DFA dfa,
            int startIndex,
            int stopIndex,
            int prediction,
            ATNConfigSet configs) {
        // ignored
    }

    /** How many expected tokens to list in an error before leaving out the rest. */
    private static final int MAX_EXPECTED_TOKENS = 6;

    /**
     * ANTLR lists every token it could have accepted, which can be dozens, so cut those lists
     * short.
     *
     * @param message The error message from ANTLR.
     * @return The message, with long lists of expected tokens shortened.
     */
    static String shortenMessage(String message) {
        if (message == null) {
            return "";
        }
        final int start = message.indexOf(" expecting {");
        final int end = message.lastIndexOf('}');
        if (start < 0 || end < start) {
            return message;
        }
        final String[] tokens = message.substring(start + " expecting {".length(), end).split(", ");
        if (tokens.length <= MAX_EXPECTED_TOKENS) {
            return message;
        }
        final String shown =
                String.join(", ", java.util.Arrays.copyOf(tokens, MAX_EXPECTED_TOKENS));
        return message.substring(0, start)
                + " expecting one of "
                + shown
                + ", or "
                + (tokens.length - MAX_EXPECTED_TOKENS)
                + " others"
                + message.substring(end + 1);
    }

    /** Reset the error count so that we can reuse this for multiple parse attempts. */
    public void resetErrorCount() {
        errorCount = 0;
    }

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {
        ScriptDiagnostics.warnAt(
                log,
                line,
                charPositionInLine + 1,
                SafeResourceLoader.getString("SYNTAX_ERROR", ScriptManager.getResourceBundle()),
                ParserErrorListener.shortenMessage(msg));
        ++errorCount;
    }
}
