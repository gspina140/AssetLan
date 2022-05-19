package ast;

import java.util.ArrayList;

import util.AssetLanlib;
import util.Environment;
import util.SemanticError;

public class IteNode implements Node {

    /**
     * An expression node providing the condition of the if-then-else block
     */
    private Node cond;

    /**
     * A list of nodes containing all the statements in the 'then' and 'else' blocks
     */
    private ArrayList<Node> thenStsL;

    private ArrayList<Node> elseStsL;

    /**
     * Class constructor; it take as parameter an expression node containing the condition
     * @param c the expression node containing the condition
     * @return an object of type IteNode
     */
    public IteNode(Node c) {
        cond = c;
        thenStsL = new ArrayList<Node>();
        elseStsL = new ArrayList<Node>();
    }

    /**
     * Add statement node to the list of statements in the IteNode
     * @param st the statement to be added to the list
     * @return null
     */
    public void addThenStatement(Node st) {
        thenStsL.add(st);
    }

    public void addElseStatement(Node st) {
        elseStsL.add(st);
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    public String toPrint(String s) {

        // String containing all the statements in the 'then' and 'else' blocks
        String str = "";

        for (Node st : thenStsL)
            str += st.toPrint(s + "");

        for (Node st : elseStsL)
        str += st.toPrint(s + "");

        return s + "If-Then-Else\n\t\t\t" + "Condition: " + cond.toPrint(s + "  ") + "\n\t\t\tStatements:\t" + str;
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors in an if-then-else block
     * It checks if the ids we are using in the condition or in the statements
     * have been declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        // Create the result
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // Check semantics in the condition
        res.addAll(cond.checkSemantics(env));

        // Check semantics in the then and in the else statement
        for (Node st : thenStsL)
            res.addAll(st.checkSemantics(env));

        for (Node st : elseStsL)
            res.addAll(st.checkSemantics(env));

        return res;
    }

    @Override
    public Node typeCheck() {


        if(! (cond.typeCheck() instanceof BoolTypeNode)){
            System.out.println("Error: condition of if-then-else must be of type bool");
            System.exit(0);
        }

        Node r1 = null ;
        Node r2 = null;

        for (Node statement:thenStsL){
            if(statement instanceof ReturnNode)
                r1 = statement;
            else    
                statement.typeCheck();
        }

        for (Node statement:elseStsL){
            if(statement instanceof ReturnNode)
                r2 = statement;
            else    
                statement.typeCheck();
        }
        
        if(r1 != null && r2 != null){

            if((r1.typeCheck() == null || r2.typeCheck() == null) ){
                if(r1.typeCheck() != r2.typeCheck()){
                    System.out.println("Error: return statements in if-then-else must have the same type");
                    System.exit(0);
                }
                return null;
            }

            if(! AssetLanlib.isSubtype(r1.typeCheck(), r2.typeCheck()) ){
                System.out.println("Error: return statements in if-then-else must have the same type");
                System.exit(0);
            }

            return r1.typeCheck();

        }else if(r1 != null || r2 != null){
            System.out.println("Error: return statements in if-then-else must have the same type");
            System.exit(0);
        }
        
        return null;
    }
}