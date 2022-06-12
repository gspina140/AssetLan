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
     * Entry of the asset in the symTable; stored for liquidity check and codegeneration
     * (it contains information about the offset and the nesting level of the asset in the symTable)
     */
    private STentry entry;

    /**
     * Current nesting level (used for lookups in codegeneration)
     */
    private int nl;
    
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

        // Store current nesting level for lookups in code generation
        nl = env.getNestingLevel();

        return res;
    }

    /**
     * Function for type checking
     * It checks that the id corresponds to an asset in the symTable
     * @param void
     * @return null
     */
    @Override
    public Node typeCheck() {
        if (! (entry.getType() instanceof AssetTypeNode)) {
            System.out.println("Transfer operation requires an asset");
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
    public Boolean checkLiquidity(Environment sigma, int verbosity){
        STentry asset = sigma.lookup(id);
        ((AssetTypeNode)asset.getType()).empty();

        if (verbosity > 1)
            System.out.println("Transfer: " + id + "empty: " + ((AssetTypeNode)sigma.lookup(id).getType()).isEmpty());

        return true;
    }

    /**
     * Function for code generation
     * @param void
     * @return the string containing the generated code
     */
    @Override
    public String codeGeneration(){
        String getAR = "";

        for(int i = 0; i < nl-entry.getNestinglevel(); i++)
            getAR += "lw $al 0($al)\n";

        return "move $al $fp\n"+
                getAR+
                "lw $a0 "+entry.getOffset()+"($al)\n"+
                "add $s0 $s0 $a0\n"+
                "li $t1 0\n"+
                "sw $t1 "+entry.getOffset()+"($al)\n";
    }
}
