package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class AssignmentNode implements Node {
    
    private String id; 
    private Node exp;

    public AssignmentNode(String i, Node e){
        id  = i;
        exp = e;
    }

    public String toPrint(String s){
        return s+"Assignment\n" + id + exp.toPrint(s+" ") ; 
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        //Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        HashMap<String,STentry> hm = env.symTable.get(env.nestingLevel);
       
        
        if(hm.get(id) == null ){ //First ID doesn't exists, i'm not taking into account if it is an asset or a function or whatever
            int nl = env.nestingLevel -1;

            while(nl >= 0){ //Check in outer scopes
                hm = env.symTable.get(nl);
        
                if(hm.get(id) == null){
                    nl--;
                }else{ //ID declared
                    res.addAll(exp.checkSemantics(env));
                    break; //Exit cycle;
                }    
                if(nl < 0)
                    res.add(new SemanticError("Variable id " + id+ " has not been declared"));
            }
        }
        
        return res;
    }

}
