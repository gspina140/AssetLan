package ast;

public class DerExpNode {
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
}
