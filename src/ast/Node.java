package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;
    
/**
 * Node interface, every node must implement this interface
 */
public interface Node {

    /**
     * toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    String toPrint(String indent);

    /**
     * checkSemantics method
     * Method to check the semantics of the node
     * @param env the environment where the node is included
     * @return a list of semantic errors (can be empty)
     */
    ArrayList<SemanticError> checkSemantics(Environment env);

    Node typeCheck();
}