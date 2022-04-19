package mainPackage;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;

import parser.AssetLanLexer;
import ast.*;
import parser.AssetLanParser;
import util.Environment;
import util.SemanticError;
import util.TestLexerListener;

public class Test {
    public static void main(String[] args) throws Exception {

        String fileName = "input.al";   // We decided AssetLan extension to be "".al"

		FileInputStream is = new FileInputStream(fileName);         // Program to compile is in a file
        CharStream input = CharStreams.fromStream(is);              // From Antlr4.6 ANTLRInputStream is deprecated, CharStream is recommended instead
        
        /* Lexer */
        AssetLanLexer lexer = new AssetLanLexer(input);             // Instantiate Lexer
        lexer.removeErrorListeners();                               // Remove ConsoleErrorListener for lexer
        lexer.addErrorListener(new TestLexerListener());            // Add new custom ErrorListener
        CommonTokenStream tokens = new CommonTokenStream(lexer);    // Token stream

        /* Parser */
        AssetLanParser parser = new AssetLanParser(tokens);         // Instantiate Parser
        //parser.removeErrorListeners();                              // Remove ConsoleErrorListener for parser
        
        /* Visitor (semantic analysis) */
		AssetLanVisitorImpl visitor = new AssetLanVisitorImpl();    // Use custom visitor
		Node ast = visitor.visit(parser.program());                    // Generazione AST
        
        Environment env = new Environment();
        ArrayList<SemanticError> err = ast.checkSemantics(env);
        if(err.size()>0) {
            System.out.println("You had: " + err.size() + " errors:");
            for (SemanticError e : err)
                System.out.println("\t" + e);
        }
        //parser.program();                                           // Parse as usual
    }
}
