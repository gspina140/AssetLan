package ast;

import java.util.ArrayList;
    
import util.Environment;
import util.SemanticError;
    
public class ValExpNode implements Node{

    /**
     * The derivated value (constant)
     */
    private Integer val;

    /**
     * Class constructor; it takes as parameter the derivated value
     * @param n the derivated value (constant)
     */
    public ValExpNode(Integer n) {
        val = n;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    public String toPrint(String s) {
      return s + "value : " + Integer.toString(val) + " ";
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when derivating the value
     * It basically returns an empty list
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<SemanticError>();
    }     

    @Override
    public Node typeCheck() {
        return new IntTypeNode();
    }

    @Override
    public String codeGeneration() {
        return "li $a0 " + val + "\n";
    }
}