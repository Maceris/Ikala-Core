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

    /** Reset the error count so that we can reuse this for multiple parse attempts. */
    public void resetErrorCount() {
        this.errorCount = 0;
    }

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {
        ParserErrorListener.log.warn(
                SafeResourceLoader.getString("SYNTAX_ERROR", ScriptManager.getResourceBundle()),
                line,
                charPositionInLine,
                msg);
        ++this.errorCount;
    }
}
