package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class FunctionNode implements Node {

    private Node type;    // Add fake type for void?
    private String id;
    private ArrayList<Node> parlist;    // Function parameters (figuring as delist in the grammar, but we also need a declist for the body)
    private ArrayList<Node> assetlist;
    /* Body */
    // Inner declarations should already have been considered in parlist because they share the same context
    private ArrayList<Node> declist;
    private ArrayList<Node> statementlist;
    
    public FunctionNode (Node t, String i) {
        type    = t;
        id      = i;
    }
  
    @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  
        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        //env.offset = -2;
        HashMap<String,STentry> hm = env.symTable.get(env.nestingLevel);
        STentry entry = new STentry(env.nestingLevel,env.offset--); // Entry introduction
        
        if ( hm.put(id,entry) != null )
            res.add(new SemanticError("Function id "+id+" already declared"));
        else{
            // Create a new HashMap for the symTable
            env.nestingLevel++;
            HashMap<String,STentry> hmn = new HashMap<String,STentry> ();
            env.symTable.add(hmn);
            
            ArrayList<Node> parTypes = new ArrayList<Node>();
            int paroffset=1;
            
            // Check args
            for(Node p : parlist){
                ParNode arg = (ParNode) p;
                parTypes.add(arg.getType());
                if ( hmn.put(arg.getId(),new STentry(env.nestingLevel,arg.getType(),paroffset++)) != null  )
                    System.out.println("Parameter id "+arg.getId()+" already declared");                
            }

            // Check assets
            for(Node a : assetlist){
                ADecNode arg = (ADecNode) a;
                //parTypes.add(arg.getType());
                if ( hmn.put(arg.getId(),new STentry(env.nestingLevel,paroffset++)) != null  )
                    System.out.println("Asset id "+arg.getId()+" already declared");                
            }
            
            // Set function type
            entry.addType( new ArrowTypeNode(parTypes, type) );
            
            // Check body
            // Inner declarations should already have been checked with parameters cause of the shared context
            for(Node s : statementlist){
                res.addAll(s.checkSemantics(env));
            }
            
            // Close scope
            env.symTable.remove(env.nestingLevel--);            
        }
        
        return res;
	}
  
    public void addPar (Node p) {
        parlist.add(p);
    }

    public void addAsset (Node a) {
        assetlist.add(a);
    }

    public void addBody(ArrayList<Node> sl) {
        statementlist = sl;
    }
    
    public String toPrint(String s) {
        String parlstr="";
        for (Node par:parlist)
            parlstr+=par.toPrint(s+"  ");
        String assetlstr="";
        if (assetlist!=null) 
            for (Node asset:assetlist)
                assetlstr+=asset.toPrint(s+"  ");
        String statementlstr="";
        if (statementlist!=null)
            for (Node statement:statementlist)
                statementlstr+=statement.toPrint(s+" ");
        return s+"Function:" + id +"\n"
            +type.toPrint(s+"  ")
            +parlstr
            +assetlstr
            +statementlstr
            +body.toPrint(s+"  ") ; 
    }

    //public Node typeCheck () {}
    
    //public String codeGeneration() {}
  
}  