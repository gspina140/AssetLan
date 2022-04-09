package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

// ci serve davvero questa classe?
public class DecNode implements Node{
    
    private Node type;
    private String id;

    public DecNode(Node t, String i){
        type = t;
        id   = i;
    }

    public String toPrint(String s){
    	return s+"Dec\n" + type.toPrint(s+" ") + id; 
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // env.offset = -2;
        HashMap<String,STentry> hm = env.symTable.get(env.nestingLevel);
        STentry entry = new STentry(env.nestingLevel,type, env.offset--); // Introducing "entry"
        
        if ( hm.put(id,entry) != null )
    		res.add(new SemanticError("Dec id "+id+" already declared"));
        
        // credo non ci sia più nulla da controllare... type non ci interessa
        return res;
    }
}
