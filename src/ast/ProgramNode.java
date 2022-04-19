package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class ProgramNode implements Node {

    private ArrayList<Node> fieldlist;
    private ArrayList<Node> assetlist;
    private ArrayList<Node> functionlist;
    private Node initcall;

    public ProgramNode(ArrayList<Node> fields, ArrayList<Node> assets, ArrayList<Node> functions, Node ic) {
        fieldlist = fields;
        assetlist = assets;
        functionlist = functions;
        initcall = ic;
    }

    public String toPrint(String s) {
        String fieldliststr = "";
        for (Node field : fieldlist)
            fieldliststr += field.toPrint(s + "  ");
        String assetliststr = "";
        for (Node asset : assetlist)
            assetliststr += asset.toPrint(s + "  ");
        String functionliststr = "";
        for (Node function : functionlist)
            functionliststr += function.toPrint(s + "  ");
        return s + "Program\n" + fieldliststr + assetliststr + functionliststr + initcall.toPrint(s + "  ");
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        env.nestingLevel++;
        HashMap<String, STentry> hm = new HashMap<String, STentry>();
        env.symTable.add(hm);

        // Declare resulting list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // Check semantics in the field list
        if (fieldlist.size() > 0) {
            env.offset = -2;
            // If there are children then check semantics for every child and save the
            // results
            for (Node n : fieldlist)
                res.addAll(n.checkSemantics(env));
        }

        // Check semantics in the asset list
        if (assetlist.size() > 0) {
            env.offset = -2;
            // If there are children then check semantics for every child and save the
            // results
            for (Node n : assetlist)
                res.addAll(n.checkSemantics(env));
        }

        // Check semantics in the functions list
        if (functionlist.size() > 0) {
            env.offset = -2;
            // If there are children then check semantics for every child and save the
            // results
            for (Node n : functionlist)
                res.addAll(n.checkSemantics(env));
        }

        // Check semantics in the initcall body
        if(initcall != null)
            res.addAll(initcall.checkSemantics(env));
      
        // Clean the scope, we are leaving a program scope
        env.symTable.remove(env.nestingLevel--);

        // Return the result
        return res;
    }

    // public Node typeCheck () {}

    // public String codeGeneration() {}

}