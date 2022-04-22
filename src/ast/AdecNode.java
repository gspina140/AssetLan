package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class AdecNode implements Node{

    /**
     * The list of ids (it contains at least one element)
     */
    private ArrayList<String> ids = new ArrayList<String>();
    
    /**
     * Class constructor using the first id (it is always present)
     * @param i a String containing the id of the first asset declaration
     * @return An object of type ADecNode
     */
    public AdecNode (String id) {
        addId(id);
    }
    
    /**
     * Adds an id to the ids list
     * @param id a string containing the id to be added
     * @return void
     */
    public void addId(String id){
        ids.add(id);
    }

    /**
     * Returns the list of ids
     * @param void
     * @return the list of ids (which is an ArrayList of Strings)
     */
    public ArrayList<String> getIds(){
        return ids;
    }

    /**
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    public String toPrint(String s){
        String res = "";

        for(String id:ids)
            res += "\t" + id; 

    	return s+"Asset declaration\n" + res; 
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when declaring assets
     * It checks if the id has already been used in this scope and if so it provides an error
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        // Introducing "entry"
        // If addEntry returns null, it means that another declaration with the same id
        // has been found in the same scope, and therefore an "already declared id" is provided
        for(String id:ids){
            if(env.addEntry(null, id) != null){
                res.add(new SemanticError("Error when declaring asset of id " + id +"\n" +
                                          "Id already used for declaration in the same scope"));
            }
        }

        return res;
    }
}
