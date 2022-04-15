package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class ADecNode implements Node{
    // Probably need a fake type node (e.g., type Asset) for STEntry?
  //private Node type
  private String id;
  
  public ADecNode (String i) {
    //Type = new TypeNode(...)
    id      = i;
  }

  public String getId(){
      return id;
  }
  public String toPrint(String s) {
	return s+"Asset\n" + id + " "; 
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	    // Create result list
  		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  	  
  		// env.offset = -2;
  		HashMap<String,STentry> hm = env.symTable.get(env.nestingLevel);
        STentry entry = new STentry(env.nestingLevel, env.offset--); // Introducing "entry"
        
        if ( hm.put(id,entry) != null )
        		res.add(new SemanticError("Asset id "+id+" already declared"));
        
        return res;
	}
  
    /*
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
*/
}
