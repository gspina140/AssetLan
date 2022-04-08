package ast;
import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class AssetNode implements Node {

  // Probably need a fake type node (e.g., type Asset) for STEntry?
  //private Node type
  private String id;
  
  public AssetNode (String i) {
    //Type = new TypeNode(...)
    id      = i;
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
        STentry entry = new STentry(env.nestingLevel,type, env.offset--); // Introducing "entry"
        
        if ( hm.put(id,entry) != null )
        		res.add(new SemanticError("Asset id "+id+" already declared"));
        
        res.addAll(exp.checkSemantics(env));
        
        return res;
	}
  
  //public Node typeCheck () {}
  
  //public String codeGeneration() {}

}  