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
    
    public FunctionNode (Node t, String i){//, Node p, Node a, ArrayList<Node> d) {
        type         = t;
        id           = i;
        declarations = new ArrayList<Node>();
        parameters   = null;
        assets       = null;
    }
  
    @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  
        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        //env.offset = -2;
        HashMap<String,STentry> hm = env.symTable.get(env.nestingLevel);
        STentry entry = new STentry(env.nestingLevel, type, env.offset--); // Entry introduction ---- If return type id void, there will be type==null
        
        if ( hm.put(id,entry) != null )
            res.add(new SemanticError("Function id "+id+" already declared"));
        else{
            // Create a new HashMap for the symTable
            env.nestingLevel++;
            HashMap<String,STentry> hmn = new HashMap<String,STentry> ();
            env.symTable.add(hmn);
            
            //int paroffset=1;
            
            // Check args
            if(parameters != null) {
                DecNode par = (DecNode) parameters;
                res.addAll(par.checkSemantics(env));
            }

            if(assets != null) {
                // Check assets
                ADecNode as = (ADecNode) assets;

                res.addAll(as.checkSemantics(env));
            }

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
  
    public void addPar (Node p) {
        parameters = p;
    }

    public void addAsset (Node a) {
        assets = a;
    }

    public void addBody(ArrayList<Node> sl) {
        statementlist = sl;
    }

    public void addDec(Node d){
        declarations.add(d);
    }


    public String toPrint(String s) {
        String dec = "";
        if(declarations.size() > 0)
            for(Node d : declarations)
                dec += d.toPrint(s + " ");
        
        String statementlstr="";
        if (statementlist!=null)
            for (Node statement:statementlist)
                statementlstr+=statement.toPrint(s+" ");

        return s+"Function:" + id +"\n"
            +type.toPrint(s + " ")
            +parameters.toPrint(s + " ")
            +assets.toPrint(s + " ")
            +dec
            +statementlstr ;
    }

    //public Node typeCheck () {}
    
    //public String codeGeneration() {}
  
}  