package ast;
import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class TransferNode implements Node{
    
    /**
     * A string containing the id of the asset to transfer from
     */
    private String id;

    private STentry entry;
    
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
    @Override
    public String toPrint(String s){
        return s + "Transfer:\t" + id + "\n";
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

        // Look-up for the asset id
        entry = env.lookup(id);
        if (entry == null)
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Asset " + id + " han not been declared"));

        return res;
    }

    @Override
    public Node typeCheck() {
        if (! (entry.getType() instanceof AssetTypeNode)) {
            System.out.println("Transfer operation requires an asset");
            System.exit(0);
        }
        
        return null;
    }

    public Boolean checkLiquidity(Environment sigma){
        STentry asset = sigma.lookup(id);
        ((AssetTypeNode)asset.getType()).empty();
        return true;
    }
}
