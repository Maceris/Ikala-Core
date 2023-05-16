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
 * Handles proper logging of errors, tracks how many occurred over both lexing
 * and parsing. We want to use a new one of these each time if tracking error
 * counts is important.
 * 
 * @author Ches Burks
 *
 */
@Slf4j
public class ParserErrorListener implements ANTLRErrorListener {

	/**
	 * The number of errors that we have seen so far with this object.
	 */
	@Getter
	private int errorCount = 0;

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
		int line, int charPositionInLine, String msg, RecognitionException e) {
		log.warn(
			SafeResourceLoader.getString("SYNTAX_ERROR",
				ScriptManager.getResourceBundle()),
			line, charPositionInLine, msg);
		++errorCount;
	}

	@Override
	public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex,
		int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
		log.debug(SafeResourceLoader.getString("AMBIGUITY",
			ScriptManager.getResourceBundle()));
	}

	@Override
	public void reportAttemptingFullContext(Parser recognizer, DFA dfa,
		int startIndex, int stopIndex, BitSet conflictingAlts,
		ATNConfigSet configs) {
		log.debug(SafeResourceLoader.getString("FULL_CONTEXT",
			ScriptManager.getResourceBundle()));
	}

	@Override
	public void reportContextSensitivity(Parser recognizer, DFA dfa,
		int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
		log.debug(SafeResourceLoader.getString("CONTEXT_SENSITIVE",
			ScriptManager.getResourceBundle()));
	}

}
