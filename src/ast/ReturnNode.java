package ast;
import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class ReturnNode implements Node{   

    private Node exp;

    public ReturnNode(Node exp){
        this.exp = exp;
    }

    public String toPrint(String s){
        return s + "Return\n " +  exp.toPrint(s+" ");
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        if(exp == null){
            return new ArrayList<SemanticError>();
        }else {
            return exp.checkSemantics(env);
        }
    }
}
