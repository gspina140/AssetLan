package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;
import util.AssetLanlib;

public class LogicOpNode implements Node{
    
    /**
     * Nodes containing the expression on the left (eL) and on the right (eR) side of the operator, respectively
     */
    private Node eL, eR;

    Boolean isAnd;

    /**
     * Class constructor; it takes as parameters both the expression nodes
     * @param e1 the node containing the expression on the left side of the operator
     * @param e2 the node containing the expression on the right side of the operator
     * @return an object of type BinExpNode
     */
    public LogicOpNode(Node e1, Node e2, Boolean kind){
        eL = e1;
        eR = e2;
        isAnd = kind;
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
        return s + "Expression:\tLeft expression: " + eL.toPrint(s + " ") +
                                    "\tRight expression: " + eR.toPrint(s + " "); 
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

    /**
     * Override of the typeCheck function
     * It checks if the type of the expression is correct
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return the type of the expression
     */
    @Override
    public Node typeCheck(){

        if (! (AssetLanlib.isSubtype(eL.typeCheck(), new BoolTypeNode()) && AssetLanlib.isSubtype(eR.typeCheck(), new BoolTypeNode()))){
            System.out.println("Both operator of logic operation (i.e., && - ||) must be of type boolean");
            System.exit(0);
        }

        return new BoolTypeNode();
    }

    @Override
    public String codeGeneration(){
        if(isAnd){
            String falseL = AssetLanlib.freshLabel();
            String endL = AssetLanlib.freshLabel();
            return eL.codeGeneration() +
                "li $t1 0\n"+
                "beq $a0 $t1" + falseL +"\n"+
                eR.codeGeneration()+
                "li $t1 0\n"+
                "beq $a0 $t1" + falseL +"\n"+
                "li $a0 1\n"+
                "b "+ endL + "\n"+
                falseL + ":\n"+
                "li $a0 0\n"+
                endL + ":\n";
        }else{
            String trueL = AssetLanlib.freshLabel();
            String endL = AssetLanlib.freshLabel();
            return eL.codeGeneration() +
                "li $t1 1\n"+
                "beq $a0 $t1" + trueL +"\n"+
                eR.codeGeneration()+
                "li $t1 1\n"+
                "beq $a0 $t1" + trueL +"\n"+
                "li $a0 0\n"+
                "b "+ endL + "\n"+
                trueL + ":\n"+
                "li $a0 1\n"+
                endL + ":\n";
        }
    }
}
