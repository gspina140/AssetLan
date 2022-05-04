package ast;

import util.Environment;
import util.SemanticError;

import java.util.ArrayList;

public class ExpListNode implements Node{
    
    private ArrayList<Node> expList;

    public ExpListNode (Node e) {
        expList.add(e);
    }

    public void addExp(Node e){
        expList.add(e);
    }

    public ArrayList<Node> getExps(){
        return expList;
    }

    @Override
    public String toPrint(String s){ 
        String res = "";
        
        for(Node e : expList){
            res += e.toPrint(s + "  ");
        }
        
        return s;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // For every id try introducing it to the symTable and, if already declared, give an error
        for(Node exp : expList){
            res.addAll(exp.checkSemantics(env));
        }

        return res;
    }

    @Override
    public Node typeCheck() {

        for(Node exp : expList){
            exp.typeCheck();
        }

        return null;
    }

}
