package ast;
import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class ReturnNode extends Node{   

    private Node exp;

    public ReturnNode(Node exp){
        this.exp = exp;
    }

    public String toPrint(String s){
        return s + "Return\n " +  exp.toPrint(s+" ");
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        return exp.checkSemantics(env);
    }
}