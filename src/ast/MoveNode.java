package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class MoveNode implements Node {
    
    /**
     * Two strings containing the id of the assets involved in the move operation
     */
    private String id1, id2;

    private STentry entry1, entry2;

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
        return s + "Move: " + id1 + " to " + id2;
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

        entry1 = env.lookup(id1);
        // Look-up for the first id
        if (entry1 == null)
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Asset " + id1 + " han not been declared"));

        entry2 = env.lookup(id2);
        // Look-up for the second id
        if (entry2 == null)
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Asset " + id2 + " han not been declared"));
        
        return res;
    }

    @Override
    public Node typeCheck() {
        if(! (entry1.getType() instanceof AssetTypeNode) ) {
            System.out.println("Left id of operation move must be an asset");
            System.exit(0);
        }


        if(! (entry2.getType() instanceof AssetTypeNode) ) {
            System.out.println("Right id of operation move must be an asset");
            System.exit(0);
        }

        return null;
    }

    public Boolean checkLiquidity(Environment sigma) {
        STEntry leftAsset = sigma.lookup(id1);
        STEntry rightAsset = sigma.lookup(id2);
        if (! ((AssetTypeNode)leftAsset.getType()).isEmpty() ) {
            ((AssetTypeNode)leftAsset.getType()).empty();
            ((AssetTypeNode)rightAsset.getType()).fill();
        }
        return true;
    }
}
