package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class AssignmentNode implements Node {
    
    /**
     * String containing the id of the variable we are assigning to
     */
    private String id;
    
    /**
     * Expression containing the value to be [computed and then] assigned
     */
    private Node exp;

    /**
     * Class constructor; it takes as parameters both an id and an expression
     * @param i the id of the variable we are assigning to
     * @param e the espression containing the value to be [computed and then] assigned
     * @return an object of type AssignmentNode
     */
    public AssignmentNode(String i, Node e){
        id  = i;
        exp = e;
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
        return s + "Assignment:\t" + id + exp.toPrint(s + " "); 
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when making an assignment
     * It checks if the ids we are using in the assignment and relative expression have been
     * declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // Get the current nesting level
        int nl = env.getNestingLevel();
        
        // Look-up for the id
        if(env.checkDeclaration(id, nl) == null ){

            // Could not find the id of the left side of the assignment in the current scope
            // Try searching in the enclosing scopes (iterative decrease the nesting level and check them)
            while(--nl >= 0){
                if(env.checkDeclaration(id, nl) != null){
                    // Id found
                    break;
                }
            }

            // At this point, if nl is less than 0 it means the id has not been found and an error should be provided
            if(nl < 0)
                res.add(new SemanticError("Variable id " + id+ " has not been declared"));
        }
        
        // Delegate semantic check of expression to relative node
        res.addAll(exp.checkSemantics(env));
        
        return res;
    }
}