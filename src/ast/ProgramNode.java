package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class ProgramNode implements Node {

    /**
     * A list of nodes containing the field declarations
     */
    private ArrayList<Node> fieldlist;

    /**
     * A list of nodes containing the asset declarations
     */
    private ArrayList<Node> assetlist;

    /**
     * A list of nodes containing the function declarations
     */
    private ArrayList<Node> functionlist;

    /**
     * A node containing the initiation function call
     */
    private Node initcall;

    /**
     * The class constructor
     * @param fields a list of nodes containing the field declarations
     * @param assets a list of nodes containing the asset declarations
     * @param functions a list of nodes containing the function declarations
     * @param ic a node containing the initiation function call
     * @return an object of type ParamNode
     */
    public ProgramNode(ArrayList<Node> fields, ArrayList<Node> assets, ArrayList<Node> functions, Node ic) {
        fieldlist    = fields;
        assetlist    = assets;
        functionlist = functions;
        initcall     = ic;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    public String toPrint(String s) {

        String fieldliststr = "\n";
        for (Node field : fieldlist)
            fieldliststr += field.toPrint(s + " ");

        String assetliststr = "\n";
        for (Node asset : assetlist)
            assetliststr += asset.toPrint(s + " ");

        String functionliststr = "\n";
        for (Node function : functionlist)
            functionliststr += function.toPrint(s + " ");
            
        return s + "Program\n" + fieldliststr + assetliststr + functionliststr + "\n\n" + initcall.toPrint(s + " ") + "\n";
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors in the program
     * Basically delegate everything to child nodes
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        // Declare resulting list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        // Entering a new scope, increase the nesting level and add relative hash-table to the symTable
        env.enterScope();

        // Check semantics in the fields list
        if (fieldlist.size() > 0) {
            
            // If there are children then check semantics for every child and save the results
            for (Node n : fieldlist)
                res.addAll(n.checkSemantics(env));
        }

        // Check semantics in the assets list
        if (assetlist.size() > 0) {
            
            // If there are children then check semantics for every child and save the results
            for (Node n : assetlist)
                res.addAll(n.checkSemantics(env));
        }

        // Check semantics in the functions list
        if (functionlist.size() > 0) {
            
            // If there are children then check semantics for every child and save the results
            for (Node n : functionlist)
                res.addAll(n.checkSemantics(env));
        }

        // Check semantics in the initcall body
        if(initcall != null)
            res.addAll(initcall.checkSemantics(env));
      
        // Clean the scope, we are leaving a program scope
        env.exitScope();

        // Return the result
        return res;
    }

    @Override
    public Node typeCheck () {
        for (Node field:fieldlist)
            field.typeCheck();
        for (Node asset:assetlist)
            asset.typeCheck();
        for (Node function:functionlist)
            function.typeCheck();
        return initcall.typeCheck();
    }

    public Boolean checkLiquidity(Environment sigma) {
        sigma.enterScope();
        for (Node asset:assetlist) {
            System.out.println("Inserisco asset in Sigma!\n");   // DEBUG   
            ((AssetNode)asset).checkLiquidity(sigma);         
        }
        Boolean isLiquid = ((InitCallNode)initcall).checkLiquidity(sigma);
        if(isLiquid == null)
            return null;
        else if (isLiquid) {
            for (Node asset:assetlist) {
                STentry a = sigma.lookup(((AssetNode)asset).getId());
                if (!((AssetTypeNode)a.getType()).isEmpty()) {
                    isLiquid = false;
                    break;
                }
            }
        }
        sigma.exitScope();

        // return false = not liquid, true = liquid, null = top (could not define if liquid or not)
        return isLiquid;
    }
}