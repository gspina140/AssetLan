package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class AssetNode implements Node {

    // Probably need a fake type node (e.g., type Asset) for STEntry?
    // private Node type
    private String id;

    public AssetNode(String i) {
        // Type = new TypeNode(...)
        id = i;
    }

    public String toPrint(String s) {
        return s + "Asset\n" + id + " ";
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // env.offset = -2;
        HashMap<String, STentry> hm = env.symTable.get(env.nestingLevel);
        STentry entry = new STentry(env.nestingLevel, env.offset--); // Introducing "entry"

        if (hm.put(id, entry) != null)
            res.add(new SemanticError("Id " + id + " already declared"));

        return res;
    }

    // public Node typeCheck () {}

    // public String codeGeneration() {}

}