package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class InitCallNode implements Node {

	/**
     * The id of the init function
     */
	private String id;

	/**
     * The expressions defining the parameters and the assets of the function
     */
	private ArrayList<Node> explist;
	
	/**
     * Class constructor; it takes as parameter the id of the function and instantiates
	 * a list containing the espressions defining the parameters and the assets
     * @param id a String containing the id of the function
     * @return an object of type InitCallNode
     */
	public InitCallNode(String id) {
		this.id = id;
		explist = new ArrayList<Node>();
	}
	
	/**
	 * Add an expression node to the list of espression nodes defining the parameters and the assets of the function
	 * @param n the node to be added to the list
	 * @return void
	 */
	public void addExp(Node n) {
		explist.add(n);
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

		// String containing the expressions that define the parameters and the assets
        String e = "";

        for(Node a : explist)
            e += a.toPrint(s + " ");

        return s + "Initialization call:\t" + id + e;
	}
	
	/**
     * Override of the checkSemantics function
     * Check for possible semantics errors when calling a init function
     * It checks if the function id and the ids contained in the expressions that define the
     * parameters and the assets have been declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
	@Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
		// Look-up for the function id
        if (!env.lookup(id))
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Function " + id + " han not been declared"));
             	
        // Delegate semantic check of expressions that define the parameters and the assets to relative nodes
		for(Node e : explist) {
			res.addAll(e.checkSemantics(env));
		}
			
        return res;
	}
}
