package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class MoveNode implements Node {
    
    private String id1, id2;

    public MoveNode(String i1, String i2){
        id1 = i1;
        id2 = i2;
    }

    public String toPrint(String s){
        return s + "Move\n" + id1 + " " + id2;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        //HashMap<String, STentry> hm = env.symTable.get(env.nestingLevel);
        int nl = env.getNestingLevel();
        
        if(env.checkDeclaration(id1, nl) == null ){ //First ID doesn't exists, i'm not taking into account if it is an asset or a function or whatever
            nl--;

            while(nl >= 0){ //Check in outer scopes
                //hm = env.symTable.get(nl);
        
                if(env.checkDeclaration(id1, nl) == null){
                    nl--;
                }else //ID declared
                    break; //Exit cycle;

                if(nl < 0)
                    res.add(new SemanticError("Variable (asset) id " + id1 + " has not been declared"));
            }
        }
        
        if(env.checkDeclaration(id2, nl) == null){    
            nl--;

            while(nl >= 0){
                //hm = env.symTable.get(nl);
        
                if(env.checkDeclaration(id2, nl) == null){
                    nl--;
                }else //ID declared
                    break; //Exit cycle;

                if(nl < 0)
                    res.add(new SemanticError("Variable (asset) id " + id2 + " has not been declared"));
            }
        }
        
        return res;
    }
}
