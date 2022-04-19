package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class DecNode implements Node{
    //Since in the dec rule at least one declaration must exist, we construct the node with it
    private Node type;
    private String id;

    //Then there is a kleene star, so 0 or more declarations remaining, we store them in 2 lists
    private ArrayList<Node>   typeList = new ArrayList<Node>();
    private ArrayList<String> idList   = new ArrayList<String>();

    public DecNode(Node t, String i){
        type = t;
        id   = i;
    }

    public DecNode(){} //empty constructor, usefull in some cases

    public String toPrint(String s){
    	return s+"Dec\n" + type.toPrint(s+" ") + id; 
    }

    public void addDeclaration(Node t, String i){
        typeList.add(t);
        idList.add(i);
    }
/*
    public ArrayList<Node> getType(){
        return typeList;
    }

    public ArrayList<String> getID(){
        return idList;
    }
*/
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // env.offset = -2;
        HashMap<String,STentry> hm = env.symTable.get(env.nestingLevel);
        STentry entry = new STentry(env.nestingLevel,type, env.offset--); // Introducing "entry"
        
        if ( hm.put(id,entry) != null )
    		res.add(new SemanticError("Dec id "+id+" already declared"));
        
        for(int i=0; i < typeList.size(); i++){
            entry = new STentry(env.nestingLevel, typeList.get(i), env.offset--);
            String s = idList.get(i);

            if( hm.put(s, entry) != null)
                res.add(new SemanticError("Dec id " + s + " already declared"));

        }

        return res;
    }
}
