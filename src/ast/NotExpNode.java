package ast;
    
import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class NotExpNode {
    private Node exp;

    public NotExpNode(Node e){
        exp = e;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        res.addAll(exp.checkSemantics(env));
        
        return res;
    }
}
