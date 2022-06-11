// Generated from AVM.g4 by ANTLR 4.9.3
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
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, LOADI=3, PUSH=4, POP=5, ADD=6, SUB=7, MULT=8, DIV=9, STOREW=10, 
		LOADW=11, STOREB=12, LOADB=13, BRANCH=14, BRANCHEQ=15, BRANCHLESST=16, 
		BRANCHLESSEQ=17, JAL=18, PRINT=19, HALT=20, MOVE=21, ADDI=22, JR=23, COL=24, 
		LABEL=25, NUMBER=26, REGISTER=27, WHITESP=28, ERR=29;
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
			"'jal'", "'print'", "'halt'", "'move'", "'addi'", "'jr'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "LOADI", "PUSH", "POP", "ADD", "SUB", "MULT", "DIV", 
			"STOREW", "LOADW", "STOREB", "LOADB", "BRANCH", "BRANCHEQ", "BRANCHLESST", 
			"BRANCHLESSEQ", "JAL", "PRINT", "HALT", "MOVE", "ADDI", "JR", "COL", 
			"LABEL", "NUMBER", "REGISTER", "WHITESP", "ERR"
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AVMVisitor ) return ((AVMVisitor<? extends T>)visitor).visitAssembly(this);
			else return visitor.visitChildren(this);
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LOADI) | (1L << PUSH) | (1L << POP) | (1L << ADD) | (1L << SUB) | (1L << MULT) | (1L << DIV) | (1L << STOREW) | (1L << LOADW) | (1L << STOREB) | (1L << LOADB) | (1L << BRANCH) | (1L << BRANCHEQ) | (1L << BRANCHLESST) | (1L << BRANCHLESSEQ) | (1L << JAL) | (1L << PRINT) | (1L << HALT) | (1L << MOVE) | (1L << ADDI) | (1L << JR) | (1L << LABEL))) != 0)) {
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
		public Token r1;
		public Token r2;
		public Token rDest;
		public Token rSrc;
		public Token l;
		public TerminalNode LOADI() { return getToken(AVMParser.LOADI, 0); }
		public TerminalNode PUSH() { return getToken(AVMParser.PUSH, 0); }
		public TerminalNode MOVE() { return getToken(AVMParser.MOVE, 0); }
		public TerminalNode POP() { return getToken(AVMParser.POP, 0); }
		public TerminalNode ADD() { return getToken(AVMParser.ADD, 0); }
		public TerminalNode ADDI() { return getToken(AVMParser.ADDI, 0); }
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
		public TerminalNode JR() { return getToken(AVMParser.JR, 0); }
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AVMVisitor ) return ((AVMVisitor<? extends T>)visitor).visitInstruction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_instruction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LOADI:
				{
				setState(10);
				match(LOADI);
				setState(11);
				((InstructionContext)_localctx).r = match(REGISTER);
				setState(12);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case PUSH:
				{
				setState(13);
				match(PUSH);
				setState(14);
				((InstructionContext)_localctx).r = match(REGISTER);
				}
				break;
			case MOVE:
				{
				setState(15);
				match(MOVE);
				setState(16);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(17);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case POP:
				{
				setState(18);
				match(POP);
				}
				break;
			case ADD:
				{
				setState(19);
				match(ADD);
				setState(20);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(21);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(22);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case ADDI:
				{
				setState(23);
				match(ADDI);
				setState(24);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(25);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(26);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case SUB:
				{
				setState(27);
				match(SUB);
				setState(28);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(29);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(30);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case MULT:
				{
				setState(31);
				match(MULT);
				setState(32);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(33);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(34);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case DIV:
				{
				setState(35);
				match(DIV);
				setState(36);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(37);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(38);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case STOREW:
				{
				setState(39);
				match(STOREW);
				setState(40);
				((InstructionContext)_localctx).rSrc = match(REGISTER);
				setState(41);
				((InstructionContext)_localctx).n = match(NUMBER);
				setState(42);
				match(T__0);
				setState(43);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(44);
				match(T__1);
				}
				break;
			case LOADW:
				{
				setState(45);
				match(LOADW);
				setState(46);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(47);
				((InstructionContext)_localctx).n = match(NUMBER);
				setState(48);
				match(T__0);
				setState(49);
				((InstructionContext)_localctx).rSrc = match(REGISTER);
				setState(50);
				match(T__1);
				}
				break;
			case STOREB:
				{
				setState(51);
				match(STOREB);
				setState(52);
				((InstructionContext)_localctx).rSrc = match(REGISTER);
				setState(53);
				((InstructionContext)_localctx).n = match(NUMBER);
				setState(54);
				match(T__0);
				setState(55);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(56);
				match(T__1);
				}
				break;
			case LOADB:
				{
				setState(57);
				match(LOADB);
				setState(58);
				((InstructionContext)_localctx).rDest = match(REGISTER);
				setState(59);
				((InstructionContext)_localctx).n = match(NUMBER);
				setState(60);
				match(T__0);
				setState(61);
				((InstructionContext)_localctx).rSrc = match(REGISTER);
				setState(62);
				match(T__1);
				}
				break;
			case LABEL:
				{
				setState(63);
				((InstructionContext)_localctx).l = match(LABEL);
				setState(64);
				match(COL);
				}
				break;
			case BRANCH:
				{
				setState(65);
				match(BRANCH);
				setState(66);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case BRANCHEQ:
				{
				setState(67);
				match(BRANCHEQ);
				setState(68);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(69);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(70);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case BRANCHLESSEQ:
				{
				setState(71);
				match(BRANCHLESSEQ);
				setState(72);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(73);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(74);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case BRANCHLESST:
				{
				setState(75);
				match(BRANCHLESST);
				setState(76);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(77);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(78);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case JAL:
				{
				setState(79);
				match(JAL);
				setState(80);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case JR:
				{
				setState(81);
				match(JR);
				setState(82);
				((InstructionContext)_localctx).r = match(REGISTER);
				}
				break;
			case PRINT:
				{
				setState(83);
				match(PRINT);
				setState(84);
				((InstructionContext)_localctx).r = match(REGISTER);
				}
				break;
			case HALT:
				{
				setState(85);
				match(HALT);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\37[\4\2\t\2\4\3\t"+
		"\3\3\2\7\2\b\n\2\f\2\16\2\13\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3Y\n\3\3"+
		"\3\2\2\4\2\4\2\2\2n\2\t\3\2\2\2\4X\3\2\2\2\6\b\5\4\3\2\7\6\3\2\2\2\b\13"+
		"\3\2\2\2\t\7\3\2\2\2\t\n\3\2\2\2\n\3\3\2\2\2\13\t\3\2\2\2\f\r\7\5\2\2"+
		"\r\16\7\35\2\2\16Y\7\34\2\2\17\20\7\6\2\2\20Y\7\35\2\2\21\22\7\27\2\2"+
		"\22\23\7\35\2\2\23Y\7\35\2\2\24Y\7\7\2\2\25\26\7\b\2\2\26\27\7\35\2\2"+
		"\27\30\7\35\2\2\30Y\7\35\2\2\31\32\7\30\2\2\32\33\7\35\2\2\33\34\7\35"+
		"\2\2\34Y\7\34\2\2\35\36\7\t\2\2\36\37\7\35\2\2\37 \7\35\2\2 Y\7\35\2\2"+
		"!\"\7\n\2\2\"#\7\35\2\2#$\7\35\2\2$Y\7\35\2\2%&\7\13\2\2&\'\7\35\2\2\'"+
		"(\7\35\2\2(Y\7\35\2\2)*\7\f\2\2*+\7\35\2\2+,\7\34\2\2,-\7\3\2\2-.\7\35"+
		"\2\2.Y\7\4\2\2/\60\7\r\2\2\60\61\7\35\2\2\61\62\7\34\2\2\62\63\7\3\2\2"+
		"\63\64\7\35\2\2\64Y\7\4\2\2\65\66\7\16\2\2\66\67\7\35\2\2\678\7\34\2\2"+
		"89\7\3\2\29:\7\35\2\2:Y\7\4\2\2;<\7\17\2\2<=\7\35\2\2=>\7\34\2\2>?\7\3"+
		"\2\2?@\7\35\2\2@Y\7\4\2\2AB\7\33\2\2BY\7\32\2\2CD\7\20\2\2DY\7\33\2\2"+
		"EF\7\21\2\2FG\7\35\2\2GH\7\35\2\2HY\7\33\2\2IJ\7\23\2\2JK\7\35\2\2KL\7"+
		"\35\2\2LY\7\33\2\2MN\7\22\2\2NO\7\35\2\2OP\7\35\2\2PY\7\33\2\2QR\7\24"+
		"\2\2RY\7\33\2\2ST\7\31\2\2TY\7\35\2\2UV\7\25\2\2VY\7\35\2\2WY\7\26\2\2"+
		"X\f\3\2\2\2X\17\3\2\2\2X\21\3\2\2\2X\24\3\2\2\2X\25\3\2\2\2X\31\3\2\2"+
		"\2X\35\3\2\2\2X!\3\2\2\2X%\3\2\2\2X)\3\2\2\2X/\3\2\2\2X\65\3\2\2\2X;\3"+
		"\2\2\2XA\3\2\2\2XC\3\2\2\2XE\3\2\2\2XI\3\2\2\2XM\3\2\2\2XQ\3\2\2\2XS\3"+
		"\2\2\2XU\3\2\2\2XW\3\2\2\2Y\5\3\2\2\2\4\tX";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}