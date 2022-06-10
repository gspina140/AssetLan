package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BoolExpNode implements Node{

    /**
     * The boolean value (constant, can be 'true' or 'false')
     */
    private boolean val;

    /**
     * Class constructor; it takes as parameter a boolean value
     * @param b a boolean value (can be 'true' or 'false')
     * @return an object of type BoolExpNode
     */
    public BoolExpNode(boolean b) {
        val = b;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    @Override
    public String toPrint(String s) {
        if (val)
            return s + "Boolean expression:\ttrue\n";
        else
            return s + "Boolean expression:\tfalse\n";
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors
     * It returns an empty list
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (it is always empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<SemanticError>();
    }

    @Override
    public Node typeCheck() {
        return new BoolTypeNode();
    }

    @Override
    public String codeGeneration(){
        if(val)
            return "li $a0 1\n";
        else
            return "li $a0 0\n";
    }
}