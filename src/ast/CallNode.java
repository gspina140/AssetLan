package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class CallNode implements Node {

    /**
     * The id of the called function
     */
    private String id;

    /**
     * The expressions defining the parameters
     */
    private ArrayList<Node> explist;

    /**
     * The ids of the function assets
     */
    private ArrayList<String> idlist;

    /**
     * Class constructor; it takes as parameters the id of the function and instantiates two lists
     * containing the espressions defining the parameters and the id of the assets respectively
     * @param id a String containing the id of the function
     * @return an object of type CallNode
     */
    public CallNode(String id) {
        this.id = id;
        idlist  = new ArrayList<String>();
        explist = new ArrayList<Node>();
    }

    /**
     * Add expression node to the list of expressions that define the parameters
     * @param n the node containing the expression
     * @return void
     */
    public void addExp(Node n) {
        explist.add(n);
    }

    /**
     * Add asset id to the list of assets ids
     * @param id the asset id to be added to the list
     * @return void
     */
    public void addId(String id) {
        idlist.add(id);
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

        // String containing the expressions that define the parameters
        String e = "";   

        // String containing the ids of the assets
        String i = "";

        for(Node a : explist)
            e += a.toPrint(s + " ");
        
        for(String p : idlist)
            i += p;

        return s + "Call:\t" + id + e + i; 
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when calling a function
     * It checks if the function id, the ids contained in the expressions that define the
     * parameters and the assets ids have been declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // Get the current nesting level
        int nl = env.getNestingLevel();

        // Look-up for the function id
        if (env.checkDeclaration(id, nl) == null) {

            // Could not find the function id in the current scope
            // Try searching in the enclosing scopes (iterative decrease the nesting level and check them)
            while (--nl >= 0) {
                if (env.checkDeclaration(id, nl) != null) {
                    break;  // Id found
                }
            }

            // At this point, if nl is less than 0 it means the id has not been found and an error should be provided
            if (nl < 0) {
                res.add(new SemanticError("Function id " + id + " has not been declared"));
            }
        }

        // Delegate semantic check of expressions that define the parameters to relative nodes
        for (Node e : explist) {
            res.addAll(e.checkSemantics(env));
        }

        // Reset nl to current nesting level
        nl = env.getNestingLevel();
        
        // Look-up for each asset id
        for (String a : idlist) {
            if (env.checkDeclaration(a, nl2) == null) {

                // Could not find the function id in the current scope
                // Try searching in the enclosing scopes (iterative decrease the nesting level and check them)
                while (--nl >= 0) {
                    if (env.checkDeclaration(a, nl) != null) {
                        break;
                    }

                    // At this point, if nl is less than 0 it means the id has not been found and an error should be provided
                    if (nl < 0) {
                        res.add(new SemanticError("Asset id " + a + " has not been declared"));
                    }
                }
            }
        }

        return res;
    }
}