package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BaseExpNode implements Node{

    /**
     * A node containing the expression
     */
    private Node exp;


    /**
     * Class constructor; it take as only argument the node containing the expression
     * @param e the node containing the expression
     * @return an object of type BaseExpNode
     */
    public BaseExpNode(Node e){
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
    public String toPrint(String s){
        return s + "Base expression:\t" + exp.toPrint(s + " "); 
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors in the expression
     * It checks if the ids we are using in the expression have been declared in this scope
     * or in an enclosing one (it basically delegate this check to the expression node)
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        // Delegate semantic check of expression to relative node
        res.addAll(exp.checkSemantics(env));
        
        return res;
    }

    @Override
    public Node typeCheck() {
        return exp.typeCheck();
    }

    @Override
    public String codeGeneration(){
        return exp.codeGeneration();
    }
}
