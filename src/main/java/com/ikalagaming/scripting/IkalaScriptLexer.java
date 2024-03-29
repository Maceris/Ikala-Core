package com.ikalagaming.scripting;

// Generated from IkalaScriptLexer.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class IkalaScriptLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int BOOLEAN = 1,
            BREAK = 2,
            CASE = 3,
            CHAR = 4,
            CONTINUE = 5,
            DEFAULT = 6,
            DO = 7,
            DOUBLE = 8,
            ELSE = 9,
            EXIT = 10,
            FINAL = 11,
            FOR = 12,
            GOTO = 13,
            IF = 14,
            INT = 15,
            STRING = 16,
            SWITCH = 17,
            VOID = 18,
            WHILE = 19,
            IntegerLiteral = 20,
            FloatingPointLiteral = 21,
            BooleanLiteral = 22,
            CharacterLiteral = 23,
            StringLiteral = 24,
            NullLiteral = 25,
            Identifier = 26,
            LPAREN = 27,
            RPAREN = 28,
            LBRACE = 29,
            RBRACE = 30,
            LBRACK = 31,
            RBRACK = 32,
            SEMICOLON = 33,
            COMMA = 34,
            DOT = 35,
            ASSIGN = 36,
            GT = 37,
            LT = 38,
            NOT = 39,
            QUESTION = 40,
            COLON = 41,
            EQUAL = 42,
            LTE = 43,
            GTE = 44,
            NOTEQUAL = 45,
            AND = 46,
            OR = 47,
            INC = 48,
            DEC = 49,
            ADD = 50,
            SUB = 51,
            MUL = 52,
            DIV = 53,
            MOD = 54,
            ADD_ASSIGN = 55,
            SUB_ASSIGN = 56,
            MUL_ASSIGN = 57,
            DIV_ASSIGN = 58,
            MOD_ASSIGN = 59,
            WS = 60,
            COMMENT = 61,
            LINE_COMMENT = 62;
    public static String[] channelNames = {"DEFAULT_TOKEN_CHANNEL", "HIDDEN"};

    public static String[] modeNames = {"DEFAULT_MODE"};

    public static final String[] ruleNames = IkalaScriptLexer.makeRuleNames();
    private static final String[] _LITERAL_NAMES = IkalaScriptLexer.makeLiteralNames();

    private static final String[] _SYMBOLIC_NAMES = IkalaScriptLexer.makeSymbolicNames();
    public static final Vocabulary VOCABULARY =
            new VocabularyImpl(IkalaScriptLexer._LITERAL_NAMES, IkalaScriptLexer._SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated public static final String[] tokenNames;

    static {
        tokenNames = new String[IkalaScriptLexer._SYMBOLIC_NAMES.length];
        for (int i = 0; i < IkalaScriptLexer.tokenNames.length; i++) {
            IkalaScriptLexer.tokenNames[i] = IkalaScriptLexer.VOCABULARY.getLiteralName(i);
            if (IkalaScriptLexer.tokenNames[i] == null) {
                IkalaScriptLexer.tokenNames[i] = IkalaScriptLexer.VOCABULARY.getSymbolicName(i);
            }

            if (IkalaScriptLexer.tokenNames[i] == null) {
                IkalaScriptLexer.tokenNames[i] = "<INVALID>";
            }
        }
    }

    public static final String _serializedATN =
            "\u0004\u0000>\u0198\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"
                    + "\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"
                    + "\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"
                    + "\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"
                    + "\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"
                    + "\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"
                    + "\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"
                    + "\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"
                    + "\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"
                    + "\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"
                    + "\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"
                    + "!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007"
                    + "&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007"
                    + "+\u0002,\u0007,\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u0007"
                    + "0\u00021\u00071\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u0007"
                    + "5\u00026\u00076\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007"
                    + ":\u0002;\u0007;\u0002<\u0007<\u0002=\u0007=\u0001\u0000\u0001\u0000\u0001"
                    + "\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"
                    + "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"
                    + "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"
                    + "\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"
                    + "\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"
                    + "\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"
                    + "\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"
                    + "\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"
                    + "\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t"
                    + "\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"
                    + "\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001"
                    + "\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e"
                    + "\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"
                    + "\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"
                    + "\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011"
                    + "\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"
                    + "\u0001\u0012\u0001\u0012\u0001\u0013\u0004\u0013\u00eb\b\u0013\u000b\u0013"
                    + "\f\u0013\u00ec\u0001\u0014\u0004\u0014\u00f0\b\u0014\u000b\u0014\f\u0014"
                    + "\u00f1\u0001\u0014\u0001\u0014\u0005\u0014\u00f6\b\u0014\n\u0014\f\u0014"
                    + "\u00f9\t\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u00fd\b\u0014\u0001"
                    + "\u0014\u0004\u0014\u0100\b\u0014\u000b\u0014\f\u0014\u0101\u0003\u0014"
                    + "\u0104\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"
                    + "\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u010f\b\u0015"
                    + "\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017"
                    + "\u0005\u0017\u0117\b\u0017\n\u0017\f\u0017\u011a\t\u0017\u0001\u0017\u0001"
                    + "\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"
                    + "\u0019\u0001\u0019\u0005\u0019\u0125\b\u0019\n\u0019\f\u0019\u0128\t\u0019"
                    + "\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c"
                    + "\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f"
                    + "\u0001 \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001#\u0001$\u0001"
                    + "$\u0001%\u0001%\u0001&\u0001&\u0001\'\u0001\'\u0001(\u0001(\u0001)\u0001"
                    + ")\u0001)\u0001*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001,\u0001,\u0001"
                    + ",\u0001-\u0001-\u0001-\u0001.\u0001.\u0001.\u0001/\u0001/\u0001/\u0001"
                    + "0\u00010\u00010\u00011\u00011\u00012\u00012\u00013\u00013\u00014\u0001"
                    + "4\u00015\u00015\u00016\u00016\u00016\u00017\u00017\u00017\u00018\u0001"
                    + "8\u00018\u00019\u00019\u00019\u0001:\u0001:\u0001:\u0001;\u0004;\u017a"
                    + "\b;\u000b;\f;\u017b\u0001;\u0001;\u0001<\u0001<\u0001<\u0001<\u0005<\u0184"
                    + "\b<\n<\f<\u0187\t<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001=\u0001=\u0001"
                    + "=\u0001=\u0005=\u0192\b=\n=\f=\u0195\t=\u0001=\u0001=\u0001\u0185\u0000"
                    + ">\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006"
                    + "\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e"
                    + "\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017"
                    + "/\u00181\u00193\u001a5\u001b7\u001c9\u001d;\u001e=\u001f? A!C\"E#G$I%"
                    + "K&M\'O(Q)S*U+W,Y-[.]/_0a1c2e3g4i5k6m7o8q9s:u;w<y={>\u0001\u0000\t\u0001"
                    + "\u000009\u0002\u0000EEee\u0002\u0000++--\u0003\u0000\n\n\r\r\'\'\u0004"
                    + "\u0000\n\n\r\r\"\"\\\\\u0002\u0000AZaz\u0004\u000009AZ__az\u0003\u0000"
                    + "\t\n\f\r  \u0002\u0000\n\n\r\r\u01a3\u0000\u0001\u0001\u0000\u0000\u0000"
                    + "\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"
                    + "\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"
                    + "\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"
                    + "\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"
                    + "\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"
                    + "\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"
                    + "\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f"
                    + "\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000"
                    + "\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000"
                    + "\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000"
                    + "-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001"
                    + "\u0000\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000"
                    + "\u0000\u00007\u0001\u0000\u0000\u0000\u00009\u0001\u0000\u0000\u0000\u0000"
                    + ";\u0001\u0000\u0000\u0000\u0000=\u0001\u0000\u0000\u0000\u0000?\u0001"
                    + "\u0000\u0000\u0000\u0000A\u0001\u0000\u0000\u0000\u0000C\u0001\u0000\u0000"
                    + "\u0000\u0000E\u0001\u0000\u0000\u0000\u0000G\u0001\u0000\u0000\u0000\u0000"
                    + "I\u0001\u0000\u0000\u0000\u0000K\u0001\u0000\u0000\u0000\u0000M\u0001"
                    + "\u0000\u0000\u0000\u0000O\u0001\u0000\u0000\u0000\u0000Q\u0001\u0000\u0000"
                    + "\u0000\u0000S\u0001\u0000\u0000\u0000\u0000U\u0001\u0000\u0000\u0000\u0000"
                    + "W\u0001\u0000\u0000\u0000\u0000Y\u0001\u0000\u0000\u0000\u0000[\u0001"
                    + "\u0000\u0000\u0000\u0000]\u0001\u0000\u0000\u0000\u0000_\u0001\u0000\u0000"
                    + "\u0000\u0000a\u0001\u0000\u0000\u0000\u0000c\u0001\u0000\u0000\u0000\u0000"
                    + "e\u0001\u0000\u0000\u0000\u0000g\u0001\u0000\u0000\u0000\u0000i\u0001"
                    + "\u0000\u0000\u0000\u0000k\u0001\u0000\u0000\u0000\u0000m\u0001\u0000\u0000"
                    + "\u0000\u0000o\u0001\u0000\u0000\u0000\u0000q\u0001\u0000\u0000\u0000\u0000"
                    + "s\u0001\u0000\u0000\u0000\u0000u\u0001\u0000\u0000\u0000\u0000w\u0001"
                    + "\u0000\u0000\u0000\u0000y\u0001\u0000\u0000\u0000\u0000{\u0001\u0000\u0000"
                    + "\u0000\u0001}\u0001\u0000\u0000\u0000\u0003\u0085\u0001\u0000\u0000\u0000"
                    + "\u0005\u008b\u0001\u0000\u0000\u0000\u0007\u0090\u0001\u0000\u0000\u0000"
                    + "\t\u0095\u0001\u0000\u0000\u0000\u000b\u009e\u0001\u0000\u0000\u0000\r"
                    + "\u00a6\u0001\u0000\u0000\u0000\u000f\u00a9\u0001\u0000\u0000\u0000\u0011"
                    + "\u00b0\u0001\u0000\u0000\u0000\u0013\u00b5\u0001\u0000\u0000\u0000\u0015"
                    + "\u00ba\u0001\u0000\u0000\u0000\u0017\u00c0\u0001\u0000\u0000\u0000\u0019"
                    + "\u00c4\u0001\u0000\u0000\u0000\u001b\u00c9\u0001\u0000\u0000\u0000\u001d"
                    + "\u00cc\u0001\u0000\u0000\u0000\u001f\u00d0\u0001\u0000\u0000\u0000!\u00d7"
                    + "\u0001\u0000\u0000\u0000#\u00de\u0001\u0000\u0000\u0000%\u00e3\u0001\u0000"
                    + "\u0000\u0000\'\u00ea\u0001\u0000\u0000\u0000)\u00ef\u0001\u0000\u0000"
                    + "\u0000+\u010e\u0001\u0000\u0000\u0000-\u0110\u0001\u0000\u0000\u0000/"
                    + "\u0114\u0001\u0000\u0000\u00001\u011d\u0001\u0000\u0000\u00003\u0122\u0001"
                    + "\u0000\u0000\u00005\u0129\u0001\u0000\u0000\u00007\u012b\u0001\u0000\u0000"
                    + "\u00009\u012d\u0001\u0000\u0000\u0000;\u012f\u0001\u0000\u0000\u0000="
                    + "\u0131\u0001\u0000\u0000\u0000?\u0133\u0001\u0000\u0000\u0000A\u0135\u0001"
                    + "\u0000\u0000\u0000C\u0137\u0001\u0000\u0000\u0000E\u0139\u0001\u0000\u0000"
                    + "\u0000G\u013b\u0001\u0000\u0000\u0000I\u013d\u0001\u0000\u0000\u0000K"
                    + "\u013f\u0001\u0000\u0000\u0000M\u0141\u0001\u0000\u0000\u0000O\u0143\u0001"
                    + "\u0000\u0000\u0000Q\u0145\u0001\u0000\u0000\u0000S\u0147\u0001\u0000\u0000"
                    + "\u0000U\u014a\u0001\u0000\u0000\u0000W\u014d\u0001\u0000\u0000\u0000Y"
                    + "\u0150\u0001\u0000\u0000\u0000[\u0153\u0001\u0000\u0000\u0000]\u0156\u0001"
                    + "\u0000\u0000\u0000_\u0159\u0001\u0000\u0000\u0000a\u015c\u0001\u0000\u0000"
                    + "\u0000c\u015f\u0001\u0000\u0000\u0000e\u0161\u0001\u0000\u0000\u0000g"
                    + "\u0163\u0001\u0000\u0000\u0000i\u0165\u0001\u0000\u0000\u0000k\u0167\u0001"
                    + "\u0000\u0000\u0000m\u0169\u0001\u0000\u0000\u0000o\u016c\u0001\u0000\u0000"
                    + "\u0000q\u016f\u0001\u0000\u0000\u0000s\u0172\u0001\u0000\u0000\u0000u"
                    + "\u0175\u0001\u0000\u0000\u0000w\u0179\u0001\u0000\u0000\u0000y\u017f\u0001"
                    + "\u0000\u0000\u0000{\u018d\u0001\u0000\u0000\u0000}~\u0005b\u0000\u0000"
                    + "~\u007f\u0005o\u0000\u0000\u007f\u0080\u0005o\u0000\u0000\u0080\u0081"
                    + "\u0005l\u0000\u0000\u0081\u0082\u0005e\u0000\u0000\u0082\u0083\u0005a"
                    + "\u0000\u0000\u0083\u0084\u0005n\u0000\u0000\u0084\u0002\u0001\u0000\u0000"
                    + "\u0000\u0085\u0086\u0005b\u0000\u0000\u0086\u0087\u0005r\u0000\u0000\u0087"
                    + "\u0088\u0005e\u0000\u0000\u0088\u0089\u0005a\u0000\u0000\u0089\u008a\u0005"
                    + "k\u0000\u0000\u008a\u0004\u0001\u0000\u0000\u0000\u008b\u008c\u0005c\u0000"
                    + "\u0000\u008c\u008d\u0005a\u0000\u0000\u008d\u008e\u0005s\u0000\u0000\u008e"
                    + "\u008f\u0005e\u0000\u0000\u008f\u0006\u0001\u0000\u0000\u0000\u0090\u0091"
                    + "\u0005c\u0000\u0000\u0091\u0092\u0005h\u0000\u0000\u0092\u0093\u0005a"
                    + "\u0000\u0000\u0093\u0094\u0005r\u0000\u0000\u0094\b\u0001\u0000\u0000"
                    + "\u0000\u0095\u0096\u0005c\u0000\u0000\u0096\u0097\u0005o\u0000\u0000\u0097"
                    + "\u0098\u0005n\u0000\u0000\u0098\u0099\u0005t\u0000\u0000\u0099\u009a\u0005"
                    + "i\u0000\u0000\u009a\u009b\u0005n\u0000\u0000\u009b\u009c\u0005u\u0000"
                    + "\u0000\u009c\u009d\u0005e\u0000\u0000\u009d\n\u0001\u0000\u0000\u0000"
                    + "\u009e\u009f\u0005d\u0000\u0000\u009f\u00a0\u0005e\u0000\u0000\u00a0\u00a1"
                    + "\u0005f\u0000\u0000\u00a1\u00a2\u0005a\u0000\u0000\u00a2\u00a3\u0005u"
                    + "\u0000\u0000\u00a3\u00a4\u0005l\u0000\u0000\u00a4\u00a5\u0005t\u0000\u0000"
                    + "\u00a5\f\u0001\u0000\u0000\u0000\u00a6\u00a7\u0005d\u0000\u0000\u00a7"
                    + "\u00a8\u0005o\u0000\u0000\u00a8\u000e\u0001\u0000\u0000\u0000\u00a9\u00aa"
                    + "\u0005d\u0000\u0000\u00aa\u00ab\u0005o\u0000\u0000\u00ab\u00ac\u0005u"
                    + "\u0000\u0000\u00ac\u00ad\u0005b\u0000\u0000\u00ad\u00ae\u0005l\u0000\u0000"
                    + "\u00ae\u00af\u0005e\u0000\u0000\u00af\u0010\u0001\u0000\u0000\u0000\u00b0"
                    + "\u00b1\u0005e\u0000\u0000\u00b1\u00b2\u0005l\u0000\u0000\u00b2\u00b3\u0005"
                    + "s\u0000\u0000\u00b3\u00b4\u0005e\u0000\u0000\u00b4\u0012\u0001\u0000\u0000"
                    + "\u0000\u00b5\u00b6\u0005e\u0000\u0000\u00b6\u00b7\u0005x\u0000\u0000\u00b7"
                    + "\u00b8\u0005i\u0000\u0000\u00b8\u00b9\u0005t\u0000\u0000\u00b9\u0014\u0001"
                    + "\u0000\u0000\u0000\u00ba\u00bb\u0005f\u0000\u0000\u00bb\u00bc\u0005i\u0000"
                    + "\u0000\u00bc\u00bd\u0005n\u0000\u0000\u00bd\u00be\u0005a\u0000\u0000\u00be"
                    + "\u00bf\u0005l\u0000\u0000\u00bf\u0016\u0001\u0000\u0000\u0000\u00c0\u00c1"
                    + "\u0005f\u0000\u0000\u00c1\u00c2\u0005o\u0000\u0000\u00c2\u00c3\u0005r"
                    + "\u0000\u0000\u00c3\u0018\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005g\u0000"
                    + "\u0000\u00c5\u00c6\u0005o\u0000\u0000\u00c6\u00c7\u0005t\u0000\u0000\u00c7"
                    + "\u00c8\u0005o\u0000\u0000\u00c8\u001a\u0001\u0000\u0000\u0000\u00c9\u00ca"
                    + "\u0005i\u0000\u0000\u00ca\u00cb\u0005f\u0000\u0000\u00cb\u001c\u0001\u0000"
                    + "\u0000\u0000\u00cc\u00cd\u0005i\u0000\u0000\u00cd\u00ce\u0005n\u0000\u0000"
                    + "\u00ce\u00cf\u0005t\u0000\u0000\u00cf\u001e\u0001\u0000\u0000\u0000\u00d0"
                    + "\u00d1\u0005s\u0000\u0000\u00d1\u00d2\u0005t\u0000\u0000\u00d2\u00d3\u0005"
                    + "r\u0000\u0000\u00d3\u00d4\u0005i\u0000\u0000\u00d4\u00d5\u0005n\u0000"
                    + "\u0000\u00d5\u00d6\u0005g\u0000\u0000\u00d6 \u0001\u0000\u0000\u0000\u00d7"
                    + "\u00d8\u0005s\u0000\u0000\u00d8\u00d9\u0005w\u0000\u0000\u00d9\u00da\u0005"
                    + "i\u0000\u0000\u00da\u00db\u0005t\u0000\u0000\u00db\u00dc\u0005c\u0000"
                    + "\u0000\u00dc\u00dd\u0005h\u0000\u0000\u00dd\"\u0001\u0000\u0000\u0000"
                    + "\u00de\u00df\u0005v\u0000\u0000\u00df\u00e0\u0005o\u0000\u0000\u00e0\u00e1"
                    + "\u0005i\u0000\u0000\u00e1\u00e2\u0005d\u0000\u0000\u00e2$\u0001\u0000"
                    + "\u0000\u0000\u00e3\u00e4\u0005w\u0000\u0000\u00e4\u00e5\u0005h\u0000\u0000"
                    + "\u00e5\u00e6\u0005i\u0000\u0000\u00e6\u00e7\u0005l\u0000\u0000\u00e7\u00e8"
                    + "\u0005e\u0000\u0000\u00e8&\u0001\u0000\u0000\u0000\u00e9\u00eb\u0007\u0000"
                    + "\u0000\u0000\u00ea\u00e9\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000"
                    + "\u0000\u0000\u00ec\u00ea\u0001\u0000\u0000\u0000\u00ec\u00ed\u0001\u0000"
                    + "\u0000\u0000\u00ed(\u0001\u0000\u0000\u0000\u00ee\u00f0\u0007\u0000\u0000"
                    + "\u0000\u00ef\u00ee\u0001\u0000\u0000\u0000\u00f0\u00f1\u0001\u0000\u0000"
                    + "\u0000\u00f1\u00ef\u0001\u0000\u0000\u0000\u00f1\u00f2\u0001\u0000\u0000"
                    + "\u0000\u00f2\u00f3\u0001\u0000\u0000\u0000\u00f3\u00f7\u0005.\u0000\u0000"
                    + "\u00f4\u00f6\u0007\u0000\u0000\u0000\u00f5\u00f4\u0001\u0000\u0000\u0000"
                    + "\u00f6\u00f9\u0001\u0000\u0000\u0000\u00f7\u00f5\u0001\u0000\u0000\u0000"
                    + "\u00f7\u00f8\u0001\u0000\u0000\u0000\u00f8\u0103\u0001\u0000\u0000\u0000"
                    + "\u00f9\u00f7\u0001\u0000\u0000\u0000\u00fa\u00fc\u0007\u0001\u0000\u0000"
                    + "\u00fb\u00fd\u0007\u0002\u0000\u0000\u00fc\u00fb\u0001\u0000\u0000\u0000"
                    + "\u00fc\u00fd\u0001\u0000\u0000\u0000\u00fd\u00ff\u0001\u0000\u0000\u0000"
                    + "\u00fe\u0100\u0007\u0000\u0000\u0000\u00ff\u00fe\u0001\u0000\u0000\u0000"
                    + "\u0100\u0101\u0001\u0000\u0000\u0000\u0101\u00ff\u0001\u0000\u0000\u0000"
                    + "\u0101\u0102\u0001\u0000\u0000\u0000\u0102\u0104\u0001\u0000\u0000\u0000"
                    + "\u0103\u00fa\u0001\u0000\u0000\u0000\u0103\u0104\u0001\u0000\u0000\u0000"
                    + "\u0104*\u0001\u0000\u0000\u0000\u0105\u0106\u0005t\u0000\u0000\u0106\u0107"
                    + "\u0005r\u0000\u0000\u0107\u0108\u0005u\u0000\u0000\u0108\u010f\u0005e"
                    + "\u0000\u0000\u0109\u010a\u0005f\u0000\u0000\u010a\u010b\u0005a\u0000\u0000"
                    + "\u010b\u010c\u0005l\u0000\u0000\u010c\u010d\u0005s\u0000\u0000\u010d\u010f"
                    + "\u0005e\u0000\u0000\u010e\u0105\u0001\u0000\u0000\u0000\u010e\u0109\u0001"
                    + "\u0000\u0000\u0000\u010f,\u0001\u0000\u0000\u0000\u0110\u0111\u0005\'"
                    + "\u0000\u0000\u0111\u0112\b\u0003\u0000\u0000\u0112\u0113\u0005\'\u0000"
                    + "\u0000\u0113.\u0001\u0000\u0000\u0000\u0114\u0118\u0005\"\u0000\u0000"
                    + "\u0115\u0117\b\u0004\u0000\u0000\u0116\u0115\u0001\u0000\u0000\u0000\u0117"
                    + "\u011a\u0001\u0000\u0000\u0000\u0118\u0116\u0001\u0000\u0000\u0000\u0118"
                    + "\u0119\u0001\u0000\u0000\u0000\u0119\u011b\u0001\u0000\u0000\u0000\u011a"
                    + "\u0118\u0001\u0000\u0000\u0000\u011b\u011c\u0005\"\u0000\u0000\u011c0"
                    + "\u0001\u0000\u0000\u0000\u011d\u011e\u0005n\u0000\u0000\u011e\u011f\u0005"
                    + "u\u0000\u0000\u011f\u0120\u0005l\u0000\u0000\u0120\u0121\u0005l\u0000"
                    + "\u0000\u01212\u0001\u0000\u0000\u0000\u0122\u0126\u0007\u0005\u0000\u0000"
                    + "\u0123\u0125\u0007\u0006\u0000\u0000\u0124\u0123\u0001\u0000\u0000\u0000"
                    + "\u0125\u0128\u0001\u0000\u0000\u0000\u0126\u0124\u0001\u0000\u0000\u0000"
                    + "\u0126\u0127\u0001\u0000\u0000\u0000\u01274\u0001\u0000\u0000\u0000\u0128"
                    + "\u0126\u0001\u0000\u0000\u0000\u0129\u012a\u0005(\u0000\u0000\u012a6\u0001"
                    + "\u0000\u0000\u0000\u012b\u012c\u0005)\u0000\u0000\u012c8\u0001\u0000\u0000"
                    + "\u0000\u012d\u012e\u0005{\u0000\u0000\u012e:\u0001\u0000\u0000\u0000\u012f"
                    + "\u0130\u0005}\u0000\u0000\u0130<\u0001\u0000\u0000\u0000\u0131\u0132\u0005"
                    + "[\u0000\u0000\u0132>\u0001\u0000\u0000\u0000\u0133\u0134\u0005]\u0000"
                    + "\u0000\u0134@\u0001\u0000\u0000\u0000\u0135\u0136\u0005;\u0000\u0000\u0136"
                    + "B\u0001\u0000\u0000\u0000\u0137\u0138\u0005,\u0000\u0000\u0138D\u0001"
                    + "\u0000\u0000\u0000\u0139\u013a\u0005.\u0000\u0000\u013aF\u0001\u0000\u0000"
                    + "\u0000\u013b\u013c\u0005=\u0000\u0000\u013cH\u0001\u0000\u0000\u0000\u013d"
                    + "\u013e\u0005>\u0000\u0000\u013eJ\u0001\u0000\u0000\u0000\u013f\u0140\u0005"
                    + "<\u0000\u0000\u0140L\u0001\u0000\u0000\u0000\u0141\u0142\u0005!\u0000"
                    + "\u0000\u0142N\u0001\u0000\u0000\u0000\u0143\u0144\u0005?\u0000\u0000\u0144"
                    + "P\u0001\u0000\u0000\u0000\u0145\u0146\u0005:\u0000\u0000\u0146R\u0001"
                    + "\u0000\u0000\u0000\u0147\u0148\u0005=\u0000\u0000\u0148\u0149\u0005=\u0000"
                    + "\u0000\u0149T\u0001\u0000\u0000\u0000\u014a\u014b\u0005<\u0000\u0000\u014b"
                    + "\u014c\u0005=\u0000\u0000\u014cV\u0001\u0000\u0000\u0000\u014d\u014e\u0005"
                    + ">\u0000\u0000\u014e\u014f\u0005=\u0000\u0000\u014fX\u0001\u0000\u0000"
                    + "\u0000\u0150\u0151\u0005!\u0000\u0000\u0151\u0152\u0005=\u0000\u0000\u0152"
                    + "Z\u0001\u0000\u0000\u0000\u0153\u0154\u0005&\u0000\u0000\u0154\u0155\u0005"
                    + "&\u0000\u0000\u0155\\\u0001\u0000\u0000\u0000\u0156\u0157\u0005|\u0000"
                    + "\u0000\u0157\u0158\u0005|\u0000\u0000\u0158^\u0001\u0000\u0000\u0000\u0159"
                    + "\u015a\u0005+\u0000\u0000\u015a\u015b\u0005+\u0000\u0000\u015b`\u0001"
                    + "\u0000\u0000\u0000\u015c\u015d\u0005-\u0000\u0000\u015d\u015e\u0005-\u0000"
                    + "\u0000\u015eb\u0001\u0000\u0000\u0000\u015f\u0160\u0005+\u0000\u0000\u0160"
                    + "d\u0001\u0000\u0000\u0000\u0161\u0162\u0005-\u0000\u0000\u0162f\u0001"
                    + "\u0000\u0000\u0000\u0163\u0164\u0005*\u0000\u0000\u0164h\u0001\u0000\u0000"
                    + "\u0000\u0165\u0166\u0005/\u0000\u0000\u0166j\u0001\u0000\u0000\u0000\u0167"
                    + "\u0168\u0005%\u0000\u0000\u0168l\u0001\u0000\u0000\u0000\u0169\u016a\u0005"
                    + "+\u0000\u0000\u016a\u016b\u0005=\u0000\u0000\u016bn\u0001\u0000\u0000"
                    + "\u0000\u016c\u016d\u0005-\u0000\u0000\u016d\u016e\u0005=\u0000\u0000\u016e"
                    + "p\u0001\u0000\u0000\u0000\u016f\u0170\u0005*\u0000\u0000\u0170\u0171\u0005"
                    + "=\u0000\u0000\u0171r\u0001\u0000\u0000\u0000\u0172\u0173\u0005/\u0000"
                    + "\u0000\u0173\u0174\u0005=\u0000\u0000\u0174t\u0001\u0000\u0000\u0000\u0175"
                    + "\u0176\u0005%\u0000\u0000\u0176\u0177\u0005=\u0000\u0000\u0177v\u0001"
                    + "\u0000\u0000\u0000\u0178\u017a\u0007\u0007\u0000\u0000\u0179\u0178\u0001"
                    + "\u0000\u0000\u0000\u017a\u017b\u0001\u0000\u0000\u0000\u017b\u0179\u0001"
                    + "\u0000\u0000\u0000\u017b\u017c\u0001\u0000\u0000\u0000\u017c\u017d\u0001"
                    + "\u0000\u0000\u0000\u017d\u017e\u0006;\u0000\u0000\u017ex\u0001\u0000\u0000"
                    + "\u0000\u017f\u0180\u0005/\u0000\u0000\u0180\u0181\u0005*\u0000\u0000\u0181"
                    + "\u0185\u0001\u0000\u0000\u0000\u0182\u0184\t\u0000\u0000\u0000\u0183\u0182"
                    + "\u0001\u0000\u0000\u0000\u0184\u0187\u0001\u0000\u0000\u0000\u0185\u0186"
                    + "\u0001\u0000\u0000\u0000\u0185\u0183\u0001\u0000\u0000\u0000\u0186\u0188"
                    + "\u0001\u0000\u0000\u0000\u0187\u0185\u0001\u0000\u0000\u0000\u0188\u0189"
                    + "\u0005*\u0000\u0000\u0189\u018a\u0005/\u0000\u0000\u018a\u018b\u0001\u0000"
                    + "\u0000\u0000\u018b\u018c\u0006<\u0000\u0000\u018cz\u0001\u0000\u0000\u0000"
                    + "\u018d\u018e\u0005/\u0000\u0000\u018e\u018f\u0005/\u0000\u0000\u018f\u0193"
                    + "\u0001\u0000\u0000\u0000\u0190\u0192\b\b\u0000\u0000\u0191\u0190\u0001"
                    + "\u0000\u0000\u0000\u0192\u0195\u0001\u0000\u0000\u0000\u0193\u0191\u0001"
                    + "\u0000\u0000\u0000\u0193\u0194\u0001\u0000\u0000\u0000\u0194\u0196\u0001"
                    + "\u0000\u0000\u0000\u0195\u0193\u0001\u0000\u0000\u0000\u0196\u0197\u0006"
                    + "=\u0000\u0000\u0197|\u0001\u0000\u0000\u0000\r\u0000\u00ec\u00f1\u00f7"
                    + "\u00fc\u0101\u0103\u010e\u0118\u0126\u017b\u0185\u0193\u0001\u0006\u0000"
                    + "\u0000";

    public static final ATN _ATN =
            new ATNDeserializer().deserialize(IkalaScriptLexer._serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[IkalaScriptLexer._ATN.getNumberOfDecisions()];
        for (int i = 0; i < IkalaScriptLexer._ATN.getNumberOfDecisions(); i++) {
            IkalaScriptLexer._decisionToDFA[i] =
                    new DFA(IkalaScriptLexer._ATN.getDecisionState(i), i);
        }
    }

    private static String[] makeLiteralNames() {
        return new String[] {
            null,
            "'boolean'",
            "'break'",
            "'case'",
            "'char'",
            "'continue'",
            "'default'",
            "'do'",
            "'double'",
            "'else'",
            "'exit'",
            "'final'",
            "'for'",
            "'goto'",
            "'if'",
            "'int'",
            "'string'",
            "'switch'",
            "'void'",
            "'while'",
            null,
            null,
            null,
            null,
            null,
            "'null'",
            null,
            "'('",
            "')'",
            "'{'",
            "'}'",
            "'['",
            "']'",
            "';'",
            "','",
            "'.'",
            "'='",
            "'>'",
            "'<'",
            "'!'",
            "'?'",
            "':'",
            "'=='",
            "'<='",
            "'>='",
            "'!='",
            "'&&'",
            "'||'",
            "'++'",
            "'--'",
            "'+'",
            "'-'",
            "'*'",
            "'/'",
            "'%'",
            "'+='",
            "'-='",
            "'*='",
            "'/='",
            "'%='"
        };
    }

    private static String[] makeRuleNames() {
        return new String[] {
            "BOOLEAN",
            "BREAK",
            "CASE",
            "CHAR",
            "CONTINUE",
            "DEFAULT",
            "DO",
            "DOUBLE",
            "ELSE",
            "EXIT",
            "FINAL",
            "FOR",
            "GOTO",
            "IF",
            "INT",
            "STRING",
            "SWITCH",
            "VOID",
            "WHILE",
            "IntegerLiteral",
            "FloatingPointLiteral",
            "BooleanLiteral",
            "CharacterLiteral",
            "StringLiteral",
            "NullLiteral",
            "Identifier",
            "LPAREN",
            "RPAREN",
            "LBRACE",
            "RBRACE",
            "LBRACK",
            "RBRACK",
            "SEMICOLON",
            "COMMA",
            "DOT",
            "ASSIGN",
            "GT",
            "LT",
            "NOT",
            "QUESTION",
            "COLON",
            "EQUAL",
            "LTE",
            "GTE",
            "NOTEQUAL",
            "AND",
            "OR",
            "INC",
            "DEC",
            "ADD",
            "SUB",
            "MUL",
            "DIV",
            "MOD",
            "ADD_ASSIGN",
            "SUB_ASSIGN",
            "MUL_ASSIGN",
            "DIV_ASSIGN",
            "MOD_ASSIGN",
            "WS",
            "COMMENT",
            "LINE_COMMENT"
        };
    }

    private static String[] makeSymbolicNames() {
        return new String[] {
            null,
            "BOOLEAN",
            "BREAK",
            "CASE",
            "CHAR",
            "CONTINUE",
            "DEFAULT",
            "DO",
            "DOUBLE",
            "ELSE",
            "EXIT",
            "FINAL",
            "FOR",
            "GOTO",
            "IF",
            "INT",
            "STRING",
            "SWITCH",
            "VOID",
            "WHILE",
            "IntegerLiteral",
            "FloatingPointLiteral",
            "BooleanLiteral",
            "CharacterLiteral",
            "StringLiteral",
            "NullLiteral",
            "Identifier",
            "LPAREN",
            "RPAREN",
            "LBRACE",
            "RBRACE",
            "LBRACK",
            "RBRACK",
            "SEMICOLON",
            "COMMA",
            "DOT",
            "ASSIGN",
            "GT",
            "LT",
            "NOT",
            "QUESTION",
            "COLON",
            "EQUAL",
            "LTE",
            "GTE",
            "NOTEQUAL",
            "AND",
            "OR",
            "INC",
            "DEC",
            "ADD",
            "SUB",
            "MUL",
            "DIV",
            "MOD",
            "ADD_ASSIGN",
            "SUB_ASSIGN",
            "MUL_ASSIGN",
            "DIV_ASSIGN",
            "MOD_ASSIGN",
            "WS",
            "COMMENT",
            "LINE_COMMENT"
        };
    }

    public IkalaScriptLexer(CharStream input) {
        super(input);
        _interp =
                new LexerATNSimulator(
                        this,
                        IkalaScriptLexer._ATN,
                        IkalaScriptLexer._decisionToDFA,
                        IkalaScriptLexer._sharedContextCache);
    }

    @Override
    public ATN getATN() {
        return IkalaScriptLexer._ATN;
    }

    @Override
    public String[] getChannelNames() {
        return IkalaScriptLexer.channelNames;
    }

    @Override
    public String getGrammarFileName() {
        return "IkalaScriptLexer.g4";
    }

    @Override
    public String[] getModeNames() {
        return IkalaScriptLexer.modeNames;
    }

    @Override
    public String[] getRuleNames() {
        return IkalaScriptLexer.ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return IkalaScriptLexer._serializedATN;
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return IkalaScriptLexer.tokenNames;
    }

    @Override
    public Vocabulary getVocabulary() {
        return IkalaScriptLexer.VOCABULARY;
    }
}
