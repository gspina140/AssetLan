package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BoolExpNode implements Node{
    private boolean val;

    public BoolExpNode(boolean n) {
        val=n;
    }

    @Override
    public String toPrint(String s) {
        if (val)
            return s + "Bool:true\n";
        else
            return s + "Bool:false\n";
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<SemanticError>();
    }

}
