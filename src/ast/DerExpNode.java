package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;


public class DerExpNode implements Node{

    /**
     * The derivated id
     */
    private String id;

    /**
     * Class constructor; it takes as parameter the derivated
     * @param i the derivated id
     * @return an object of type DerExpNode
     */
    public DerExpNode(String i) {
        id = i;
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
        return s + "Derivated id:\t" + id; 
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when derivating the id
     * It checks if the id has been declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {

	    // Create result list
	    ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        // Look-up for the id
        if (env.lookup(id) == null) 
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Identifier " + id + " han not been declared"));
    
	    return res;
	}


    @Override
    public Node typeCheck() {
        return null;
    }
}
