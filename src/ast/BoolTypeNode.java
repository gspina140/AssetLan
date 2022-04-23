package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BoolTypeNode implements Node {

    /**
     * The constructor of the class
     */
    public BoolTypeNode() {
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
        return s + "Bool Type\n";
    }

    /**
     * Override of the checkSemantics method
     * Method to check the semantics of the node
     * @param env the environment where the node is included
     * @return an empty list of semantic errors 
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<SemanticError>();
    }
}
    
