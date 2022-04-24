package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class MoveNode implements Node {
    
    /**
     * Two strings containing the id of the assets involved in the move operation
     */
    private String id1, id2;

    /**
     * Class constructor; it takes as parameters the ids 
     * @param i1 a String containing the id of the first operator
     * @param i2 a String containing the id of the second operator
     * @return an object of type MoveNode
     */
    public MoveNode(String i1, String i2){
        id1 = i1;
        id2 = i2;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    public String toPrint(String s){
        return s + "Move\n" + id1 + " " + id2;
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when moving assets
     * It checks if the ids were been declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // Look-up for the first id
        if (!env.lookup(id1))
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Asset " + id1 + " han not been declared"));

        // Look-up for the second id
        if (!env.lookup(id2))
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Asset " + id2 + " han not been declared"));
        
        return res;
    }
}
