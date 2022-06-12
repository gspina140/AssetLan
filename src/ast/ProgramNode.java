package ast;

import java.util.ArrayList;

import util.AssetLanlib;
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

    public Boolean checkLiquidity(Environment sigma, int verbosity) {
        sigma.enterScope();
        for (Node asset:assetlist) {
            if (verbosity > 1)
                System.out.println("Inserisco asset in Sigma!\n");  
            ((AssetNode)asset).checkLiquidity(sigma, verbosity);         
        }
        Boolean isLiquid = ((InitCallNode)initcall).checkLiquidity(sigma, verbosity);
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

    public String codeGeneration(){
        String decs = "";
        int k = 4; //k is the memory of the static scope, starts from 4 to allocate the $ra of initcall!
        //int k = 0;

        for (Node f : fieldlist){
            decs += f.codeGeneration();
            if(((FieldNode)f).getType() instanceof IntTypeNode)
                k+=4;
            else
                k+=1;
        }

        // Problem: for move to work properly, i need to instantiate assets at 0
        String adecs = "";
        for (Node a : assetlist) {
            adecs += a.codeGeneration();
        }

        k+= 4* assetlist.size();

        for(Node fn : functionlist)
            fn.codeGeneration();

        return  //"move $fp $sp\n"+
                "li $s0 0\n"+               // Register s0 is the wallet, i.e. the count of asset values stransfered
                "addi $sp $sp -"+k +"\n"+   // Memory allocation for global variables and assets
                "move $fp $sp\n"+
                decs+
                adecs+
                initcall.codeGeneration()+
                "addi $sp $sp "+k+"\n"+
                "halt\n"+
                AssetLanlib.getCode(); 
    }

}