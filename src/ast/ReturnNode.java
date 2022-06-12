package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class ReturnNode implements Node {   

    /**
     * A node containing an expression to return
     */
    private Node exp;

    /**
     * The class constructor; it takes as only parameter a node
     * containing the expression to return (can be null)
     * @param exp a node containing the expression to return (can be null)
     */
    public ReturnNode(Node exp) {
        this.exp = exp;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    public String toPrint(String s) {
        if (exp != null)
            return s + "Return: " +  exp.toPrint(s+" ");
        else
            return s + "Return: void";
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors in the return statement
     * It basically delegates the search for semantic errors in the expression to the respective
     * node if an expression is present
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){        
        if(exp != null) {
            return exp.checkSemantics(env);
        } else {
            return new ArrayList<SemanticError>();
        }
    }

    /**
     * Function for type checking
     * It baically delegates to the expression node
     * @param void
     * @return the result of exp.typecheck() if it exists, null otherwise
     */
    @Override
    public Node typeCheck() {
        if(exp != null) 
            return exp.typeCheck();
        else 
            return null;
    }

    /**
     * Function for code generation
     * @param void
     * @return the string containing the generated code
     */
    @Override
    public String codeGeneration(){
        return exp.codeGeneration()+
                "move $v0 $a0\n";
    }
}
