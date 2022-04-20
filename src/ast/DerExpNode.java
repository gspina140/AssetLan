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
        
	    int nl = env.getNestingLevel();

        while (nl >= 0) {
            if (env.checkDeclaration(id, nl) == null) {
                nl--;
                if(nl < 0)
                    res.add(new SemanticError("Variable id " + id + " has not been declared"));
            } else {
                break;
            }
        }
    
	    return res;
	}

    @Override
    public String toPrint(String s) {
        return s + "Derivate expression\n" + id; 
    }
}
