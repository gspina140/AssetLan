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

        // Get the current nesting level
        int nl = env.getNestingLevel();
        
        // Look-up for the first id
        if(env.checkDeclaration(id1, nl) == null ){ 
            
            // Could not find the first id in the current scope
            // Try searching in the enclosing scopes (iterative decrease the nesting level and check them)
            while(--nl >= 0){ 
                if(env.checkDeclaration(id1, nl) != null){
                    break;  // Id found
                }
            }

            // At this point, if nl is less than 0 it means the first id has not been found and an error should be provided
            if (nl < 0) {
                res.add(new SemanticError("Variable (asset) id " + id1 + " has not been declared"));
            }
        }
        
        // Reset nl to current nesting level
        nl = env.getNestingLevel();
        
        // Look-up for the second id
        if(env.checkDeclaration(id2, nl) == null){    

            // Could not find the second id in the current scope
            // Try searching in the enclosing scopes (iterative decrease the nesting level and check them)
            while(--nl >= 0){
                if(env.checkDeclaration(id2, nl) != null){
                    break;  // Id found
                }
            }

            // At this point, if nl is less than 0 it means the second id has not been found and an error should be provided
            if(nl < 0){
                res.add(new SemanticError("Variable (asset) id " + id2 + " has not been declared"));
            }
        }
        
        return res;
    }
}
