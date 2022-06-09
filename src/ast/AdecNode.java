package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class AdecNode implements Node{

    /**
     * The list of ids (it contains at least one element)
     */
    private ArrayList<String> ids;
    
    private ArrayList<Node> asslist;

    /**
     * Class constructor using the first id (it is always present)
     * @param i a String containing the id of the first asset declaration
     * @return an object of type AdecNode
     */
    public AdecNode (String id) {
        ids = new ArrayList<String>();
        addId(id);

        asslist = new ArrayList<Node>();
        addAss(new AssetTypeNode());
    }
    
    /**
     * Adds an id to the ids list
     * @param id a string containing the id to be added
     * @return void
     */
    public void addId(String id){
        ids.add(id);
    }

    public void addAss(Node ass){
        asslist.add(ass);
    }

    /**
     * Returns the list of ids
     * @param void
     * @return the list of ids (which is an ArrayList of Strings)
     */
    public ArrayList<String> getIds(){
        return ids;
    }

    public ArrayList<Node> getAsslist(){
        return asslist;
    }

    public int getNumberOfAssets() {
        return ids.size();
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

        // String containing the message
        String res = "";

        // String of asset ids separated by tabs
        for(String id:ids)
            res += id + " "; 

    	return s+"Asset(s) declaration(s): " + res; 
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

        // For every id try introducing it to the symTable and, if already declared, give an error
        for(String id : ids){
        
            // Introducing "entry"
            // If addEntry returns null, it means that another declaration with the same id
            // has been found in the same scope, and therefore an "already declared id" is provided
            STentry aux = env.addEntry(new AssetTypeNode(), id);

            if(aux != null){
                res.add(new SemanticError("Error when declaring asset of id " + id +"\n" +
                                          "Id already used for declaration in the same scope"));
            }

            addAss(env.lookup(id).getType());
        }

        return res;
    }

    @Override
    public Node typeCheck() {
        return null;
    }

    public Boolean checkLiquidity(Environment sigma) {
        for (String id : ids) {
            sigma.addEntry(new AssetTypeNode(), id);
            System.out.println("Inserito asset " + id + " in Sigma, empty: " + ((AssetTypeNode)sigma.lookup(id).getType()).isEmpty() + "\n");  // DEBUG
        }
        return true;
    }

    @Override
    public String codeGeneration(Environment env){return "";}
}