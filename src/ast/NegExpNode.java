package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class NegExpNode implements Node{
    private Node exp;

    public NegExpNode(Node e){
        exp = e;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        res.addAll(exp.checkSemantics(env));
        
        return res;
    }
    
    @Override
    public String toPrint(String s) {
        return s;
    }
}
