package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;


public class DerExpNode implements Node{

    /**
     * The derivated id
     */
    private String id;

    private STentry entry;

    private int nl;

    /**
     * Class constructor; it takes as parameter the derivated
     * @param i the derivated id
     * @return an object of type DerExpNode
     */
    public DerExpNode(String i) {
        id = i;
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
        return s + "Id:\t" + id; 
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when derivating the id
     * It checks if the id has been declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {

	    // Create result list
	    ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        entry = env.lookup(id);
        // Look-up for the id
        if (entry == null) 
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Identifier " + id + " han not been declared"));
        
        nl = env.getNestingLevel();
        
	    return res;
	}

    @Override
    public Node typeCheck() {
        return entry.getType();
    }

    @Override
    public String codeGeneration(){
        String getAR = "";

        for(int i=0; i<nl-entry.getNestinglevel();i++)
            getAR+="lw $al 0($al)\n";

        if(entry.getType() instanceof IntTypeNode || entry.getType() instanceof AssetTypeNode){
            return "move $al $fp\n"+
                    getAR+
                    "lw $a0 "+entry.getOffset()+"($al)\n";
        }else{
            return "move $al $fp\n"+
            getAR+
            "lb $a0 "+entry.getOffset()+"($al)\n";
        }
    }
}
