package mainPackage;

import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;

import java.util.*;

import parser.AssetLanLexer;
import parser.AssetLanParser;
import util.TestLexerListener;

public class Test {
    public static class MyParserListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg,
                                RecognitionException e)
        {
            List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
            Collections.reverse(stack);
            System.err.println("rule stack: "+stack+"\tRecognizer: Parser");
            System.err.println("line "+line+":"+charPositionInLine+" at "+
                                offendingSymbol+": "+msg);
        }

    }

    public static class MyLexerListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg,
                                RecognitionException e)
        {
            String outError = "line "+line+":"+charPositionInLine+": "+msg;

            System.err.println("Recognizer: Lexer");
            System.err.println("line "+line+":"+charPositionInLine+" at "+
                                offendingSymbol+": "+msg);

            try(FileWriter fw = new FileWriter("log.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(outError);
            } catch (IOException ioe) {
                System.err.println("Error writing to file");
            }
        }
    }

    public static void main(String[] args) throws Exception {

        String fileName = "input.al";   // We decided AssetLan extension to be "".al"

		FileInputStream is = new FileInputStream(fileName);
        CharStream input = CharStreams.fromStream(is);  // From Antlr4.6, ANTLRInputStream is deprecated, and CharStream is recommended instead
        AssetLanLexer lexer = new AssetLanLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new TestLexerListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AssetLanParser parser = new AssetLanParser(tokens);
        parser.removeErrorListeners();  // remove ConsoleErrorListener
        //parser.addErrorListener(new MyParserListener()); // add ours
        parser.program();  // parse as usual
    }
}
