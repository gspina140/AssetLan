package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class NegExpNode implements Node{

    /**
     * A node containing the expression to be negated
     */
    private Node exp;

    /**
     * The class constructor
     * @param e a node containing the expression to be negated
     * @return an object of type NegExpNode
     */
    public NegExpNode(Node e){
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
    public Node typeCheck() {

        if(! (exp.typeCheck() instanceof IntTypeNode)){
            System.out.println("Error: cannot negate a non-integer expression");
            System.exit(0);
        }

        return null;
    }

    @Override
    public String codeGeneration(){
        return exp.codeGeneration()+
        "li $t1 0\nsub $a0 $t1 $a0\n";
    }
}
