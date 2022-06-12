package mainpackage;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;

import ast.AssetLanVisitorImpl;
import ast.AVMVisitorImpl;
import ast.Node;
import ast.ProgramNode;

import interpreter.ExecuteVM;

import parser.AssetLanLexer;
import parser.AssetLanParser;
import parser.AVMLexer;
import parser.AVMParser;

import util.Environment;
import util.SemanticError;
import util.TestLexerListener;

public class Test {
    /**
     *--------------------------------------------------------------------------------------
     * Main function, entry point of the compiler
     * SYNAPSIS:    java mainpackage.Test "filename" ["verbosity"]
     *--------------------------------------------------------------------------------------
     * It takes as first argument the name of a file with '.al' extension to be analyzed.
     * We suggest to place it in the src directory with a proper name (e.g., 'input.al')
     * and to execute the program as follows:
     *  java mainpackage.Test input.al
     * Not providing an argument, or if the file does not exist or have the wrong extension,
     * will cause an error, and the program will exit.
     * Providing more than two arguments will provide a warning and only the first two
     * arguments will be used (the execution will not be stopped).
     * UPDATE: now a second (optional) parameter is provided to specify the verbosity of
     * the output (if greater than 0, it will visualize the AST; if greater than 1, it will
     * also print a stack trace of the assets, e.g., when they are filled or emptied).
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
        } else if (args.length > 2) {
            System.err.println("Warning:\tmore than two arguments found; only two required\n" +
                               "Only the first two arguments will be kept, while the others will be discarded\n");
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

        /* Verbosity of the output */
        int verbosity = 0;

        /* Chek verbosity */
        try {
            if (args.length > 1)
                verbosity = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.err.println("Wrong argument provided for verbosity: " + e +
                               "\nOnly integers are supported.\n" +
                               "\tVerbosity=1: visualize AST\n" +
                               "\tVerbosity=2: visualize AST and stack trace of the assets\n" +
                               "Exiting.\n"); 
            System.exit(0);
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
        
        /* Communicate that the compilation has started */
        System.out.println("\n\nAssetLan compilation\n");

        /* Visitor (semantic analysis) */
		AssetLanVisitorImpl visitor = new AssetLanVisitorImpl();    // Use custom visitor
		Node ast = visitor.visit(parser.program());                 // AST generation
        
        /* Visit the AST to find semantic errors (e.g., multiply declared variables or undeclared variables) */
        Environment env = new Environment();
        Environment sigma = new Environment();                      // Effects analysis on liquidity

        System.out.println("Running check semantics...");
        ArrayList<SemanticError> err = ast.checkSemantics(env);     // Running semantic evaluation

        if(err.size() > 0) {
            /* At least an error has occured: print the errors and stop the compilation */
            System.out.println("You had: " + err.size() + " errors:");
            for (SemanticError e : err)
                System.out.println("\t" + e);
        } else {
            System.out.println("Semantic check was successful!");
            /* If verbosity > 0, visualize the AST */
            if (verbosity > 0) {
                System.out.println("\n######################\n" +
                                   "\n# Visualizing AST... #\n" +
                                   ast.toPrint("") +
                                   "\n######################\n");
            }

            /* Visit the AST to find type errors */
            System.out.println("Running type checking...");
            Node type = ast.typeCheck();                            // Running type checking 
            
            /* At this point, if type check found an error, the execution has already been stopped */
            if(type == null) 
                System.out.println("Type checking was successful!\nThe initcall of the program is of type: Void");
            else
                System.out.println(type.toPrint("Type checking was successful!\nThe initcall of theprogram is of type: "));

            /* Visit the AST to check if the program is liquid */
            System.out.println("Running liquidity checking...");
            Boolean isLiquid = ((ProgramNode)ast).checkLiquidity(sigma, verbosity);  // Effects analysis on liquidity

            if(isLiquid == null)
                System.out.println("Could not define if the program was liquid or not.");
            else if (isLiquid)
                System.out.println("The program is liquid.");
            else
                System.out.println("The program is not liquid.");

            /* Code generation */
            String code=ast.codeGeneration(); 
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName.replace(".al",".asm"))); 
            out.write(code);
            out.close(); 
            System.out.println("Code generated! Assembling and running generated code.");            

            /* Setting the stream for intermidiate code */
            FileInputStream isASM = new FileInputStream(fileName+".asm");
            CharStream inputASM = CharStreams.fromStream(isASM); 
            
            /* Lexer for intermidiate code */
            AVMLexer lexerASM = new AVMLexer(inputASM);
            CommonTokenStream tokensASM = new CommonTokenStream(lexerASM);
            AVMParser parserASM = new AVMParser(tokensASM);
            
            /* Parser for intermidiate code */
            AVMVisitorImpl visitorAVM = new AVMVisitorImpl();
            visitorAVM.visit(parserASM.assembly()); 

            /* If verbosity > 0 print lexical errors for intermidiate code (used for debugging) */
            if (verbosity > 0) {
                System.out.println("\nYou had: "+lexerASM.lexicalErrors+" lexical errors and "+parserASM.getNumberOfSyntaxErrors()+" syntax errors.");
                if (lexerASM.lexicalErrors>0 || parserASM.getNumberOfSyntaxErrors()>0) System.exit(1);
            }

            /* Executing intermidiate code */
            System.out.println("\nStarting Virtual Machine...");
            ExecuteVM vm = new ExecuteVM(visitorAVM.code);
            vm.cpu();
        }
    }
}
