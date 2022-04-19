package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class InitCallNode implements Node {

	private String id;
	private ArrayList<Node> explist = new ArrayList<Node>();
	
	public InitCallNode(String id) {
		this.id = id;
	
	}
	
	@Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        //Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        HashMap<String,STentry> hm = env.symTable.get(env.nestingLevel);
        
        if(hm.get(id) == null) {
        	int nl = env.nestingLevel - 1;
        	
        	while(nl >= 0) {
        		hm = env.symTable.get(nl);
        		
        		if(hm.get(id) == null) 
        			nl--;
        		else {
        			for(Node e : explist) 
                		res.addAll(e.checkSemantics(env));
                	
        			return res;
        		}
        		if(nl < 0) {
        			res.add(new SemanticError("Function id" + id + "has not been declared"));
        		}
        		
        	}
             	
        }else {
        	for(Node e : explist) {
        		res.addAll(e.checkSemantics(env));
        	}
        }
        return res;
	}
	
	public void addExp(Node n) {
		explist.add(n);
	}

	
	@Override
	public String toPrint(String indent) {
		// TODO Auto-generated method stub
		return null;
	}


}
