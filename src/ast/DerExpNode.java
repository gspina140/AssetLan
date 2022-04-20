package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;


public class DerExpNode implements Node{
    private String id ;

    private STentry entry;
    private int nestinglevel;

    public DerExpNode(String i){
        id = i;
    }

    @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	    //create result list
	    ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
	    int j=env.nestingLevel;
	    STentry tmp=null; 

	    while (j>=0 && tmp==null)
    	    tmp=(env.symTable.get(j--)).get(id);
        
        if (tmp==null)
            res.add(new SemanticError("Id "+id+" not declared"));
        else{
    	  	entry = tmp;
    	  	nestinglevel = env.nestingLevel;
        }
    
	    return res;
	}

    @Override
    public String toPrint(String s) {
        return s + "Derivate expression\n" + id; 
    }
}
