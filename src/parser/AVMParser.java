// Generated from AVM.g4 by ANTLR 4.7.2
package parser;

import java.util.HashMap;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AVMParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, LOADI=3, PUSH=4, POP=5, ADD=6, SUB=7, MULT=8, DIV=9, STOREW=10, 
		LOADW=11, STOREB=12, LOADB=13, BRANCH=14, BRANCHEQ=15, BRANCHLESST=16, 
		BRANCHLESSEQ=17, JAL=18, PRINT=19, HALT=20, COL=21, LABEL=22, NUMBER=23, 
		REGISTER=24, WHITESP=25;
	public static final int
		RULE_assembly = 0, RULE_instruction = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"assembly", "instruction"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'li'", "'push'", "'pop'", "'add'", "'sub'", "'mult'", 
			"'div'", "'sw'", "'lw'", "'sb'", "'lb'", "'b'", "'beq'", "'blt'", "'ble'", 
			"'jal'", "'print'", "'halt'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "LOADI", "PUSH", "POP", "ADD", "SUB", "MULT", "DIV", 
			"STOREW", "LOADW", "STOREB", "LOADB", "BRANCH", "BRANCHEQ", "BRANCHLESST", 
			"BRANCHLESSEQ", "JAL", "PRINT", "HALT", "COL", "LABEL", "NUMBER", "REGISTER", 
			"WHITESP"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "AVM.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AVMParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class AssemblyContext extends ParserRuleContext {
		public List<InstructionContext> instruction() {
			return getRuleContexts(InstructionContext.class);
		}
		public InstructionContext instruction(int i) {
			return getRuleContext(InstructionContext.class,i);
		}
		public AssemblyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assembly; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AVMListener ) ((AVMListener)listener).enterAssembly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AVMListener ) ((AVMListener)listener).exitAssembly(this);
		}
	}

	public final AssemblyContext assembly() throws RecognitionException {
		AssemblyContext _localctx = new AssemblyContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_assembly);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(7);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LOADI) | (1L << PUSH) | (1L << POP) | (1L << ADD) | (1L << SUB) | (1L << MULT) | (1L << DIV) | (1L << STOREW) | (1L << LOADW) | (1L << STOREB) | (1L << LOADB) | (1L << BRANCH) | (1L << BRANCHEQ) | (1L << BRANCHLESST) | (1L << BRANCHLESSEQ) | (1L << JAL) | (1L << PRINT) | (1L << HALT) | (1L << LABEL))) != 0)) {
				{
				{
				setState(4);
				instruction();
				}
				}
				setState(9);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstructionContext extends ParserRuleContext {
		public Token r;
		public Token n;
		public Token l;
		public Token rDest;
		public Token r1;
		public Token r2;
		public Token rScr;
		public Token rSrc;
		public TerminalNode LOADI() { return getToken(AVMParser.LOADI, 0); }
		public TerminalNode PUSH() { return getToken(AVMParser.PUSH, 0); }
		public TerminalNode POP() { return getToken(AVMParser.POP, 0); }
		public TerminalNode ADD() { return getToken(AVMParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(AVMParser.SUB, 0); }
		public TerminalNode MULT() { return getToken(AVMParser.MULT, 0); }
		public TerminalNode DIV() { return getToken(AVMParser.DIV, 0); }
		public TerminalNode STOREW() { return getToken(AVMParser.STOREW, 0); }
		public TerminalNode LOADW() { return getToken(AVMParser.LOADW, 0); }
		public TerminalNode STOREB() { return getToken(AVMParser.STOREB, 0); }
		public TerminalNode LOADB() { return getToken(AVMParser.LOADB, 0); }
		public TerminalNode COL() { return getToken(AVMParser.COL, 0); }
		public TerminalNode BRANCH() { return getToken(AVMParser.BRANCH, 0); }
		public TerminalNode BRANCHEQ() { return getToken(AVMParser.BRANCHEQ, 0); }
		public TerminalNode BRANCHLESSEQ() { return getToken(AVMParser.BRANCHLESSEQ, 0); }
		public TerminalNode BRANCHLESST() { return getToken(AVMParser.BRANCHLESST, 0); }
		public TerminalNode JAL() { return getToken(AVMParser.JAL, 0); }
		public TerminalNode PRINT() { return getToken(AVMParser.PRINT, 0); }
		public TerminalNode HALT() { return getToken(AVMParser.HALT, 0); }
		public List<TerminalNode> REGISTER() { return getTokens(AVMParser.REGISTER); }
		public TerminalNode REGISTER(int i) {
			return getToken(AVMParser.REGISTER, i);
		}
		public TerminalNode NUMBER() { return getToken(AVMParser.NUMBER, 0); }
		public TerminalNode LABEL() { return getToken(AVMParser.LABEL, 0); }
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AVMListener ) ((AVMListener)listener).enterInstruction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AVMListener ) ((AVMListener)listener).exitInstruction(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_instruction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(10);
				match(LOADI);
				setState(11);
				((InstructionContext)_localctx).r = match(REGISTER);
				setState(12);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case 2:
				{
				setState(13);
				match(PUSH);
				setState(14);
				((InstructionContext)_localctx).r = match(REGISTER);
				}
				break;
			case 3:
				{
				setState(15);
				match(PUSH);
				setState(16);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case 4:
				{
				setState(17);
				match(PUSH);
				setState(18);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 5:
				{
				setState(19);
				match(POP);
				}
				break;
			case 6:
				{
				setState(20);
				match(ADD);
				setState(21);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(22);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(23);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case 7:
				{
				setState(24);
				match(SUB);
				setState(25);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(26);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(27);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case 8:
				{
				setState(28);
				match(MULT);
				setState(29);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(30);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(31);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case 9:
				{
				setState(32);
				match(DIV);
				setState(33);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(34);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(35);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case 10:
				{
				setState(36);
				match(STOREW);
				setState(37);
				((InstructionContext)_localctx).rScr = match(REGISTER);
				setState(38);
				((InstructionContext)_localctx).n = match(NUMBER);
				setState(39);
				match(T__0);
				setState(40);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(41);
				match(T__1);
				}
				break;
			case 11:
				{
				setState(42);
				match(LOADW);
				setState(43);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(44);
				((InstructionContext)_localctx).n = match(NUMBER);
				setState(45);
				match(T__0);
				setState(46);
				((InstructionContext)_localctx).rSrc = match(REGISTER);
				setState(47);
				match(T__1);
				}
				break;
			case 12:
				{
				setState(48);
				match(STOREB);
				setState(49);
				((InstructionContext)_localctx).rScr = match(REGISTER);
				setState(50);
				((InstructionContext)_localctx).n = match(NUMBER);
				setState(51);
				match(T__0);
				setState(52);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(53);
				match(T__1);
				}
				break;
			case 13:
				{
				setState(54);
				match(LOADB);
				setState(55);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(56);
				((InstructionContext)_localctx).n = match(NUMBER);
				setState(57);
				match(T__0);
				setState(58);
				((InstructionContext)_localctx).rSrc = match(REGISTER);
				setState(59);
				match(T__1);
				}
				break;
			case 14:
				{
				setState(60);
				((InstructionContext)_localctx).l = match(LABEL);
				setState(61);
				match(COL);
				}
				break;
			case 15:
				{
				setState(62);
				match(BRANCH);
				setState(63);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 16:
				{
				setState(64);
				match(BRANCHEQ);
				setState(65);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(66);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(67);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 17:
				{
				setState(68);
				match(BRANCHLESSEQ);
				setState(69);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(70);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(71);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 18:
				{
				setState(72);
				match(BRANCHLESST);
				setState(73);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(74);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(75);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 19:
				{
				setState(76);
				match(JAL);
				setState(77);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 20:
				{
				setState(78);
				match(PRINT);
				}
				break;
			case 21:
				{
				setState(79);
				match(HALT);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\33U\4\2\t\2\4\3\t"+
		"\3\3\2\7\2\b\n\2\f\2\16\2\13\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3S\n\3\3\3\2\2\4\2\4\2\2\2g\2\t\3"+
		"\2\2\2\4R\3\2\2\2\6\b\5\4\3\2\7\6\3\2\2\2\b\13\3\2\2\2\t\7\3\2\2\2\t\n"+
		"\3\2\2\2\n\3\3\2\2\2\13\t\3\2\2\2\f\r\7\5\2\2\r\16\7\32\2\2\16S\7\31\2"+
		"\2\17\20\7\6\2\2\20S\7\32\2\2\21\22\7\6\2\2\22S\7\31\2\2\23\24\7\6\2\2"+
		"\24S\7\30\2\2\25S\7\7\2\2\26\27\7\b\2\2\27\30\7\32\2\2\30\31\7\32\2\2"+
		"\31S\7\32\2\2\32\33\7\t\2\2\33\34\7\32\2\2\34\35\7\32\2\2\35S\7\32\2\2"+
		"\36\37\7\n\2\2\37 \7\32\2\2 !\7\32\2\2!S\7\32\2\2\"#\7\13\2\2#$\7\32\2"+
		"\2$%\7\32\2\2%S\7\32\2\2&\'\7\f\2\2\'(\7\32\2\2()\7\31\2\2)*\7\3\2\2*"+
		"+\7\32\2\2+S\7\4\2\2,-\7\r\2\2-.\7\32\2\2./\7\31\2\2/\60\7\3\2\2\60\61"+
		"\7\32\2\2\61S\7\4\2\2\62\63\7\16\2\2\63\64\7\32\2\2\64\65\7\31\2\2\65"+
		"\66\7\3\2\2\66\67\7\32\2\2\67S\7\4\2\289\7\17\2\29:\7\32\2\2:;\7\31\2"+
		"\2;<\7\3\2\2<=\7\32\2\2=S\7\4\2\2>?\7\30\2\2?S\7\27\2\2@A\7\20\2\2AS\7"+
		"\30\2\2BC\7\21\2\2CD\7\32\2\2DE\7\32\2\2ES\7\30\2\2FG\7\23\2\2GH\7\32"+
		"\2\2HI\7\32\2\2IS\7\30\2\2JK\7\22\2\2KL\7\32\2\2LM\7\32\2\2MS\7\30\2\2"+
		"NO\7\24\2\2OS\7\30\2\2PS\7\25\2\2QS\7\26\2\2R\f\3\2\2\2R\17\3\2\2\2R\21"+
		"\3\2\2\2R\23\3\2\2\2R\25\3\2\2\2R\26\3\2\2\2R\32\3\2\2\2R\36\3\2\2\2R"+
		"\"\3\2\2\2R&\3\2\2\2R,\3\2\2\2R\62\3\2\2\2R8\3\2\2\2R>\3\2\2\2R@\3\2\2"+
		"\2RB\3\2\2\2RF\3\2\2\2RJ\3\2\2\2RN\3\2\2\2RP\3\2\2\2RQ\3\2\2\2S\5\3\2"+
		"\2\2\4\tR";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}