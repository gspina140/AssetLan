package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class FunctionNode implements Node {

    private Node type;    // Add fake type for void?
    private String id;

    private Node parameters;
    private Node assets;
    private ArrayList<Node> declarations;
    /*
    private ArrayList<Node> parlist;    // Function parameters (figuring as delist in the grammar, but we also need a declist for the body)
    private ArrayList<Node> assetlist;
    /* Body */
    // Inner declarations should already have been considered in parlist because they share the same context
    //private ArrayList<Node> declist;
    private ArrayList<Node> statementlist;
    
    public FunctionNode (Node t, String i, Node p, Node a, ArrayList<Node> d) {
        type         = t;
        id           = i;
        parameters   = p;
        assets       = a;
        declarations = d;
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
            
            int paroffset=1;
            
            // Check args 
            DecNode par = (DecNode) parameters;
            
            res.addAll(par.checkSemantics(env));
            /*
            for(Node p : parlist){
                ParNode arg = (ParNode) p;
                parTypes.add(arg.getType());
                if ( hmn.put(p.getId(),new STentry(env.nestingLevel,arg.getType(),paroffset++)) != null  )
                    System.out.println("Parameter id "+arg.getId()+" already declared");                
            }*/

            // Check assets
            ADecNode as = (ADecNode) assets;
            
            res.addAll(as.checkSemantics(env));

            /*
            for(Node a : assetlist){
                ADecNode arg = (ADecNode) a;
                //parTypes.add(arg.getType());
                if ( hmn.put(arg.getId(),new STentry(env.nestingLevel,paroffset++)) != null  )
                    System.out.println("Asset id "+arg.getId()+" already declared");                
            }*/
            /*
            // Set function type
            entry.addType( new ArrowTypeNode(parTypes, type) );
            */
            // Check body
            // Inner declarations should already have been checked with parameters cause of the shared context--NOT IN THAT VERSION!!!
            //DecNode dec = (DecNode) declarations;
            for(Node n : declarations){
                DecNode dec = (DecNode) n;
                res.addAll(dec.checkSemantics(env));
            }
            /*for(Node d : declist)
                res.addAll(d.checkSemantics(env));
            */
            for(Node s : statementlist){
                res.addAll(s.checkSemantics(env));
            }
            
            // Close scope
            env.symTable.remove(env.nestingLevel--);            
        }
        
        return res;
	}
  /*
    public void addPar (Node p) {
        parlist.add(p);
    }

    public void addAsset (Node a) {
        assetlist.add(a);
    }
*/
    public void addBody(ArrayList<Node> sl) {
        statementlist = sl;
    }
/*
    public void addDec(Node d){
        declist.add(d);
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
            +statementlstr ;
    }
*/
    //Just to avoid error
    @Override
    public String toPrint(String s){
        return s;
    }
    //public Node typeCheck () {}
    
    //public String codeGeneration() {}
  
}  