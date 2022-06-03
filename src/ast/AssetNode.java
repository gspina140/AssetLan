package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class AssetNode implements Node {
    
    /**
     * The id of the asset
     */
    private String id;

    private Node type;

    /**
     * The class constructor; it take as argument the asset id
     * @param i the asset id
     * @return an object of type AssetNode
     */
    public AssetNode(String i) {
        id = i;
        type = new AssetTypeNode();
    }

    /**
     * Returns the asset id
     * @param void
     * @return the asset id
     */
    public String getId() {
        return id;
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
        return s + "\nAsset declaration: " + id;
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when declaring assets
     * It checks if the id has already been used in this scope and if so it provides an error
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();      

        // Introducing "entry"
        // If addEntry returns null, it means that another declaration with the same id
        // has been found in the same scope, and therefore an "already declared id" is provided
        if (env.addEntry(type, id) != null)
        res.add(new SemanticError("Error when declaring asset of id " + id +"\n" +
                                  "Id already used for declaration in the same scope"));
        return res;
    }

    @Override
    public Node typeCheck() {
        return null;
    }

    public Boolean checkLiquidity(Environment sigma) {
        sigma.addEntry(type, id);
        return true;
    }
}