package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BinExpNode implements Node{

    /**
     * Nodes containing the expression on the left (eL) and on the right (eR) side of the operator, respectively
     */
    private Node eL, eR;

    /**
     * Class constructor; it takes as parameters both the expression nodes
     * @param e1 the node containing the expression on the left side of the operator
     * @param e2 the node containing the expression on the right side of the operator
     * @return an object of type BinExpNode
     */
    public BinExpNode(Node e1, Node e2){
        eL = e1;
        eR = e2;
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
        return s + "Binary expression:\tLeft expression:\t" + eL.toPrint(s + " ") +
                                    "\tRight expression:\t" + eR.toPrint(s + " "); 
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors in both expression
     * It checks if the ids we are using in the expressions have been declared in this scope
     * or in an enclosing one; it basically delegate this check to both nodes accordingly
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // Delegate semantic check of expression to relative nodes
        res.addAll(eL.checkSemantics(env));
        res.addAll(eR.checkSemantics(env));

        return res;
    }

    @Override
    public Node typeCheck() {
        if (! (AssetLanlib.isSubtype(entry.getType(), exp.typeCheck())) ) {
            System.out.println("incompatible value for variable "+id);
            System.exit(0);
        }
    }
}