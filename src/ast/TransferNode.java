package ast;
import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class TransferNode implements Node{
    
    /**
     * A string containing the id of the asset to transfer from
     */
    private String id;
    
    /**
     * The class constructor; it take as parameter a string containing an asset id
     * @param id the id of the asset to transfer from
     * @return an object of type TransferNode
     */
    public TransferNode(String id){
        this.id = id;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    public String toPrint(String s){
        return s + "Transfer:\t" + id;
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors in the transfer statement
     * Check if the asset id has been declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        // Get current nesting level
        int nl = env.getNestingLevel();

        // Look-up for the id
        while(nl >= 0){
            if(env.checkDeclaration(id, nl) != null){
                // Id found
                return res;
            } else {
                // Could not find the id of the left side of the assignment in the current scope
                // Try searching in the enclosing scopes (iterative decrease the nesting level and check them)
                nl--;
            }
        }

        // If this point is reached, it means that the id has not been found and an error should be provided
        res.add(new SemanticError("Asset " + id + " han not been declared"));

        return res;
    }
}
