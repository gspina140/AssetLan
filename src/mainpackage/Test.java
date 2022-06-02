package mainpackage;

import java.io.FileInputStream;

import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;

import ast.*;

import parser.AssetLanLexer;
import parser.AssetLanParser;

import util.Environment;
import util.SemanticError;
import util.TestLexerListener;

public class Test {
    /**
     *--------------------------------------------------------------------------------------
     * Main function, entry point of the compiler
     * SYNAPSIS:    java mainpackage/Test [filename]
     *--------------------------------------------------------------------------------------
     * It takes as only argument the name of a file with '.al' extension to be analyzed
     * We suggest to place it in the src directory with a proper name (e.g., 'input.al')
     * and to execute the program as follows:
     *  java mainpackage/Test input.al
     * Not providing an argument, or if the file does not exist or have the wrong extension
     * will cause an error, and the program will exit
     * Providing more than one argument will provide a warning and only the first argument
     * will be used (the execution will not be stopped)
     *--------------------------------------------------------------------------------------
     * @param String the name of the file with '.al' extension to be analyzed (and compiled)
     * @return void
     *--------------------------------------------------------------------------------------
     */
    public static void main(String[] args) throws Exception {

        /* Arguments check */
        if (args.length == 0) {
            System.err.println("Error:\t\tmust specify a file name\n" +
                               "SYNAPSIS:\tjava mainpackage/Test input.al\n" +
                               "(launching from console from AssetLan/src directory)\n" +
                               "Exiting.\n");
            return;
        } else if (args.length > 1) {
            System.err.println("Warning:\tmore than one argument found; only one required\n" +
                               "Only the first argument will be kept, while the others will be discarded\n");
        }

        /* The name of the file to be analyzed */
        String fileName = args[0];                                  // We decided AssetLan extension to be '.al'
        
        /* Checking file extension */
        Pattern pattern = Pattern.compile(".*\\.al$");
        Matcher matcher = pattern.matcher(args[0]);
        boolean matchFound = matcher.find();
        if(!matchFound) {
            System.err.println("Error:\tfile " + args[0] + " has the wrong file extension\n" +
                               "File must have '.al' extension\n" +
                               "Exiting.\n"); 
            return;
        }

        /* Setting the stream */
		FileInputStream is = new FileInputStream(fileName);         // Program to compile is in a file
        CharStream input = CharStreams.fromStream(is);              // From Antlr4.6 ANTLRInputStream is deprecated, CharStream is recommended instead
        
        /* Lexer */
        AssetLanLexer lexer = new AssetLanLexer(input);             // Instantiate Lexer
        lexer.removeErrorListeners();                               // Remove ConsoleErrorListener for lexer
        lexer.addErrorListener(new TestLexerListener());            // Add new custom ErrorListener
        CommonTokenStream tokens = new CommonTokenStream(lexer);    // Token stream

        /* Parser */
        AssetLanParser parser = new AssetLanParser(tokens);         // Instantiate Parser; parser errors will still be printed to console as default
        
        /* */
        System.out.println("\n\nAssetLan compilation\n\n");

        /* Visitor (semantic analysis) */
		AssetLanVisitorImpl visitor = new AssetLanVisitorImpl();    // Use custom visitor
		Node ast = visitor.visit(parser.program());                 // AST generation
        
        /* Visit the AST to find semantic errors (e.g., multiply declared variables or undeclared variables) */
        Environment env = new Environment();
        Environment sigma = new Environment();  // Effects analysis on liquidity

        ArrayList<SemanticError> err = ast.checkSemantics(env);
        if(err.size()>0) {
            System.out.println("You had: " + err.size() + " errors:");
            for (SemanticError e : err)
                System.out.println("\t" + e);
        }else{
            System.out.println("\n ###################### \n\nVisualizing AST... \n");
            System.out.println(ast.toPrint(""));

            System.out.println("\n ###################### \n\nStarting type checking... \n");
            Node type = ast.typeCheck(); //type-checking bottom-up 
            
            if(type == null) 
                System.out.println("Type checking ok! Type of the program is: Void");
            else
                System.out.println(type.toPrint("Type checking ok! Type of the program is: "));

            Boolean isLiquid = ast.checkLiquidity(sigma);  // Effects analysis on liquidity

            if(isLiquid == null)
                System.out.println("\nCould not define if the program was liquid or not.\n");
            else if (isLiquid)
                System.out.println("\nThe program is liquid.\n");
            else
                System.out.println("\nThe program is not liquid.\n");
        }
    }
}
