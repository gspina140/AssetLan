package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class ADecNode implements Node{
    
    private ArrayList<String> ids = new ArrayList<String>();

    public ADecNode(){}

    public void addId(String s){
        ids.add(s);
    }

    public String toPrint(String s){
        String res = "";

        for(String i:ids)
            res += i; 

    	return s+"Asset declaration\n" + res; 
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // env.offset = -2;
        HashMap<String,STentry> hm = env.symTable.get(env.nestingLevel);
        STentry entry = new STentry(env.nestingLevel, env.offset--); // Introducing "entry"
        
        for(String i:ids){
            if(hm.put(i, entry) != null){
                res.add(new SemanticError("Asset declaration id" + i + "already declared"));
            }
        }

        return res;
    }
}
