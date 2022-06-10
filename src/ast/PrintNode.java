package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class PrintNode implements Node {

    /**
     * A node containing the expression to be printed
     */
    private Node exp;

    /**
     * The class constructor
     * @param e a node containing the expression to be printed
     * @return an object of type PrintNode
     */
    public PrintNode(Node e) {
        exp = e;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    public String toPrint(String s) {
        return s + "Print\n\t\t\t" + exp.toPrint(s);
    }

    /**
     * Override of the checkSemantics method
     * Method to check the semantics of the node
     * It delegate the check to the expression node
     * @param env the environment where the node is included
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        // Declare resulting list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        // Check semantics of expression
        res.addAll(exp.checkSemantics(env));
        
        return res;
    }

    @Override
    public Node typeCheck() {
        return exp.typeCheck();
    }

    @Override
    public String codeGeneration(){
        return exp.codeGeneration()+
                "print $a0\n";
    }
}