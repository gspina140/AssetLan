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

    private int nl;

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
        
        nl = env.getNestingLevel();

        return res;
    }

    /**
     * Override of the typeCheck function
     * It checks if both entries have type Asset
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return the type of the node
     */
    @Override
    public Node typeCheck() {
        if(! (entry1.getType() instanceof AssetTypeNode)) {
            System.out.println("Left id of operation move must be an asset");
            System.exit(0);
        }

        if(! (entry2.getType() instanceof AssetTypeNode)) {
            System.out.println("Right id of operation move must be an asset");
            System.exit(0);
        }

        return null;
    }

    /**
     * Function for liquidity checking
     * @param sigma the environment in which the check takes place (it contains the symTable)
     * @param verbosity parameter for output verbosity; if > 1 it prints the assets stack trace
     * @return if local liquidity is respected
     */
    public Boolean checkLiquidity(Environment sigma, int verbosity) {
        STentry leftAsset = sigma.lookup(id1);
        STentry rightAsset = sigma.lookup(id2);

        if (!((AssetTypeNode)leftAsset.getType()).isEmpty()) {
            ((AssetTypeNode)leftAsset.getType()).empty();
            ((AssetTypeNode)rightAsset.getType()).fill();
        }

        if (verbosity > 1) {
            System.out.println("Move: " + id1 + " empty: " + ((AssetTypeNode)sigma.lookup(id1).getType()).isEmpty());
            System.out.println("Move: " + id2 + " empty: " + ((AssetTypeNode)sigma.lookup(id2).getType()).isEmpty());
        }

        return true;
    }

    /**
     * Function for code generation
     * @param void
     * @return the string containing the generated code
     */
    @Override
    public String codeGeneration(){
        String getAR1="";
        String getAR2="";
        
        for(int i = 0; i < nl-entry1.getNestinglevel(); i++)
            getAR1+="lw $al 0($al)\n";

        for(int i = 0; i < nl-entry2.getNestinglevel(); i++)
            getAR2+="lw $al 0($al)\n";

        return "move $al $fp\n"+
                getAR1+
                "lw $a0 "+entry1.getOffset()+"($al)\n"+
                "push $a0\n"+
                "li $t1 0\n"+
                "sw $t1 "+entry1.getOffset()+"($al)\n"+
                "move $al $fp\n"+
                getAR2+
                "lw $a0 "+entry2.getOffset()+"($al)\n"+
                "lw $t1 0($sp)\n"+  // top
                "add $a0 $a0 $t1\n"+
                "sw $a0 "+entry2.getOffset()+"($al)\n"+
                "pop\n";
    }

}
