package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BinExpNode implements Node{
    private Node eL, eR;

    public BinExpNode(Node e1, Node e2){
        eL = e1;
        eR = e2;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        res.addAll(eL.checkSemantics(env));
        res.addAll(eR.checkSemantics(env));

        return res;
    }
}
