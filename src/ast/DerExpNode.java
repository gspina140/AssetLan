package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;


public class DerExpNode implements Node{

    /**
     * The derivated id
     */
    private String id ;

    /**
     * Class constructor; it takes as parameter the derivated
     * @param i the derivated id
     * @return an object of type DerExpNode
     */
    public DerExpNode(String i){
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
        
        // Get current nesting level
	    int nl = env.getNestingLevel();

        // Look-up for the derivated id
        while (nl >= 0) {
            if (env.checkDeclaration(id, nl--) != null) {
                // Id found
                break;
            }
        }

        // At this point, if nl is less than 0 it means the id has not been found and an error should be provided
        if(nl < -1)
            res.add(new SemanticError("Variable id " + id + " has not been declared"));
    
	    return res;
	}
}
