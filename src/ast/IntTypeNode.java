package ast;

import java.util.ArrayList;

import util.SemanticError;
import util.Environment;

public class IntTypeNode implements Node{

    /**
     * The constructor of the class
     */
    public IntTypeNode(){
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    @Override
    public String toPrint(String s){
        return s + "Int Type\n";
    }

    /**
     * Override of the checkSemantics method
     * Method to check the semantics of the node
     * @param env the environment where the node is included
     * @return an empty list of semantic errors 
     */  
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        return new ArrayList<SemanticError>();
    }

    @Override
    public Node typeCheck() {
        return null;
    }

    @Override
    public String codeGeneration(){return "";}
}
