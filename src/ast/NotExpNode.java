package ast;
    
import java.util.ArrayList;

import util.AssetLanlib;
import util.Environment;
import util.SemanticError;

public class NotExpNode implements Node{

    /**
     * A node containing the expression to apply the not operator to
     */
    private Node exp;

    /**
     * The class constructor
     * @param e a node containing the expression to apply the not operator to
     * @return an object of type NegExpNode
     */
    public NotExpNode(Node e){
        exp = e;
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
        return s + "Negate expression\n" + exp.toPrint(s + " ");
    }

    /**
     * Override of the checkSemantics method
     * Method to check the semantics of the node
     * It delegate the check to the expression node
     * @param env the environment where the node is included
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Declare resulting list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        // Check semantics of expression
        res.addAll(exp.checkSemantics(env));
        
        return res;
    }

    @Override
    public Node typeCheck() {   //We are checking that the expression to be applyied the not operator is boolean (maybe it could be an integer??)

        if(! (exp.typeCheck() instanceof BoolTypeNode)){
            System.out.println("Error: cannot apply not operator (!) to a non-boolean expression");
            System.exit(0);
        }

        return null;
    }

    @Override
    public String codeGeneration(){
        String trueL = AssetLanlib.freshLabel();
        String endL = AssetLanlib.freshLabel();

        return exp.codeGeneration()+
        "li $t1 0\n"+
        "beq $a0 $t1"+trueL+"\n"+
        "li $a0 0\n"+
        "b"+endL+"\n"+
        trueL+":\n"+
        "li $a0 1\n"+
        endL +":\n";
    }
}
