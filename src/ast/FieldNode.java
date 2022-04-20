package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class FieldNode implements Node {

    private Node type;
    private String id;
    private Node exp;

    public FieldNode(Node t, String i, Node e) {
        type = t;
        id = i;
        exp = e;
    }

    public String toPrint(String s) {
        return s + "Field\n" + type.toPrint(s + " ") + id + exp.toPrint(s + " ");
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // env.offset = -2;
        //HashMap<String, STentry> hm = env.symTable.get(env.nestingLevel);
        //STentry entry = new STentry(env.nestingLevel, type, env.offset--); // Introducing "entry"

        if (env.addEntry(type, id) != null)
            res.add(new SemanticError("Id " + id + " already declared"));

        if(exp != null)
            res.addAll(exp.checkSemantics(env));

        return res;
    }

    // public Node typeCheck () {}

    // public String codeGeneration() {}

}