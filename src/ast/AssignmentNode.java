package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;
import util.AssetLanlib;

public class AssignmentNode implements Node {
    
    /**
     * String containing the id of the variable we are assigning to
     */
    private String id;
    
    /**
     * Expression containing the value to be [computed and then] assigned
     */
    private Node exp;

    private STentry entry;

    private int nl;


    /**
     * Class constructor; it takes as parameters both an id and an expression
     * @param i the id of the variable we are assigning to
     * @param e the espression containing the value to be [computed and then] assigned
     * @return an object of type AssignmentNode
     */
    public AssignmentNode(String i, Node e){
        id    = i;
        exp   = e;
        entry = null;
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
        return s + "Assignment:\t" + id + " equals-> "+ exp.toPrint(s + " "); 
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
        
        // Look-up for the  id
        entry = env.lookup(id);
        if (entry == null)
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Identifier (assignment) of " + id + " has not been declared"));
        
        // Delegate semantic check of expression to relative node
        res.addAll(exp.checkSemantics(env));
        
        nl = env.getNestingLevel();

        return res;
    }

    @Override
    public Node typeCheck() {

        // It is possible to assign the value of an asset to an integer (see program 2 of exercise 3)
        if (! (entry.getType() instanceof IntTypeNode && exp.typeCheck() instanceof AssetTypeNode)) 
            if (! (AssetLanlib.isSubtype(entry.getType(), exp.typeCheck())) ) {
                System.out.println("Incompatible value for variable "+id);
                System.exit(0);
            }
        
        return null;
    }

    @Override
    public String codeGeneration(){
        String getAR = "";
        String store = "";

        for(int i=0; i< nl-entry.getNestinglevel(); i++ ){
            getAR+="lw $al 0($al)\n";
        }
        
        if(entry.getType() instanceof BoolTypeNode)
            store = "sb $a0 "+ entry.getOffset() +"($al)\n";
        else
            store = "sw $a0 "+ entry.getOffset() +"($al)\n";

        return "move $al $fp\n"+
                getAR+
                exp.codeGeneration()+
                store;
    }
}