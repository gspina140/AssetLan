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
       
        if(hm.get(id) != null){ // It is all good, variable has been declared in the actual scope. So the assignment can be performed
            res.addAll(exp.checkSemantics(env));
        }else{ 
            //If the variable has been declared in an outer scope, it is still legit to perform the assignment(?)
            int nl = env.nestingLevel - 1;
            
            while(nl >= 0){
                hm = env.symTable.get(nl);
                
                if(hm.get(id) != null)
                    res.addAll(exp.checkSemantics(env));
                else 
                    nl--;
            }
            //Now all outer nesting levels has been checked, the variable has not been declared.
            res.add(new SemanticError("Variable id " +id+ " has not been declared, cannot perform the assignment"));
        }
        
        return res;
    }

}
