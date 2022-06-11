package ast;

import java.util.ArrayList;

import javax.lang.model.util.ElementScanner6;

//import javax.lang.model.util.ElementScanner6;

import util.AssetLanlib;
import util.Environment;
import util.SemanticError;

public class FunctionNode implements Node {

    /**
     * A node containing the type of the function (can be 'int' or 'bool', or null if the function returns 'void')
     */
    private Node type;

    /**
     * A string containing the id of the funcction
     */
    private String id;

    /**
     * A node containing the declarations of the parameters of the function
     */
    private Node parameters;

    /**
     * A node containing the declarations of the assets of the function
     */
    private Node assets;

    /**
     * A list containing the inner declarations of the function
     */
    private ArrayList<Node> declarations;

    /**
     * A list containing the statements of the function
     */
    private ArrayList<Node> statementlist;
    
    /**
     * The constructor of the function class
     * @param t the type of the function (null if 'void')
     * @param i the id of the function
     */
    public FunctionNode (Node t, String i){
        type          = t;
        id            = i;
        parameters    = null;
        assets        = null;
        declarations  = new ArrayList<Node>();
        statementlist = new ArrayList<Node>();
    }
  
    /**
     * Add a declaration node to the function node containing a list of parameter declarations
     * @param p the declaration node containing the parameter declarations
     * @return void
     */
    public void addPar (Node p) {
        parameters = p;
    }

    /**
     * Add an asset node to the function node containing a list of asset declarations
     * @param a the asset declaration node containing the assets declarations
     * @return void
     */
    public void addAsset (Node a) {
        assets = a;
    }

    /**
     * Add a declaration node to the list of inner declaration nodes
     * @param d the declaration node to be added to the list of inner declaration nodes (can contain more than one declaration)
     * @return void
     */
    public void addDec(Node d){
        declarations.add(d);
    }

    /**
     * Add a statement node to the list of statement nodes of the function node
     * @param s the node containing the statement to be added to the list of statement nodes og the function node
     * @return void
     */
    public void addStatement (Node s) {
        statementlist.add(s);
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    @Override
    public String toPrint(String s) {
        String res = "";

        if(type == null)
            res += "\nFunction: " + id + " Void\n";
        else
            res += "\nFunction: " + id + " " + type.toPrint("") + "\n";

        String dec = "";

        for(Node d : declarations)
            dec += d.toPrint(s + "");
        
        String statementlstr="";
        for (Node statement : statementlist)
            statementlstr += statement.toPrint(s+"") + "\n\t\t";

        if(parameters != null && assets != null)
            return s + res + "\tParameter(s) " 
                + parameters.toPrint(s) + "\t"
                + assets.toPrint(s) + "\n\t"
                + dec + "\t"
                + statementlstr + "\t";
        else if(parameters != null && assets == null)
            return s + res + "\tParameter(s) "
                + parameters.toPrint(s) + "\n\t"
                + dec + "\t"
                + statementlstr + "\t";
        else if(parameters == null && assets != null)
            return s + res + "\t"
                + assets.toPrint(s) + "\n\t"
                + dec + "\t"
                + statementlstr + "\t";
        else
            return s + res + "\t"
                + dec + "\t"
                + statementlstr;
    }
  
    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when introducing a new function
     * It checks if the function id has already been declared in this scope and then open a new scope;
     * then delegates semantic checks to parameters, assets, inner declarations and statements accordingly
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  
        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        if ( env.addEntry(null, id) != null )
            res.add(new SemanticError("Function id "+id+" already declared"));
        else {

            STentry entry = env.lookup(id);

            // Enter a new scope (increase nesting level and add relative hashmap to symTable)
            env.enterScope();

            ArrayList<Node> parTypes = new ArrayList<>();
            // Delegate parameter declarations semantic check to respective node
            if(parameters != null) {
                res.addAll(parameters.checkSemantics(env));
                parTypes = ((DecNode)parameters).getTypeList();
            }

            ArrayList<Node> assetL = new ArrayList<Node>();
            int noa = 0;    // number of assets
            // Delegate asset declarations semantic check to respective node
            if(assets != null) {
                res.addAll(assets.checkSemantics(env));
                assetL = ((AdecNode)assets).getAsslist();
                noa = ((AdecNode)assets).getNumberOfAssets();
            }

            entry.addType( new ArrowTypeNode(parTypes, assetL, noa, type));

            // Delegate inner declarations semantic check to respective nodes
            for(Node n : declarations){
                res.addAll(n.checkSemantics(env));
            }
            
            // Delegate statements semantic check to respective nodes
            for(Node s : statementlist){
                try {
                    res.addAll(s.checkSemantics(env));
                } catch (NullPointerException e) {
                    System.err.println("Warning: could not resolve statement\n");
                }
            }
            
            ((ArrowTypeNode)entry.getType()).addFunction(this);
        
            // Close scope (remove hashmap relative to the scope and decrease nesting level)
            env.exitScope();        
        }

        return res;
	}

    @Override
    public Node typeCheck() {

        if (parameters != null)
            parameters.typeCheck();

        if (assets != null)
            assets.typeCheck();

        for (Node declaration:declarations)
            declaration.typeCheck();

        boolean returnFound = false;    // Flag (ho trovato il return node)
        for (Node statement:statementlist) {
            
            if (statement instanceof ReturnNode) {
                if ( ! (statement.typeCheck() == null && type == null) ) {
                    if (! (AssetLanlib.isSubtype(statement.typeCheck(), type))) {
                        System.out.println("incompatible value for function "+id);
                        System.exit(0);
                    }           
                }
                returnFound = true;
            }
            statement.typeCheck();
        }
        if (type != null && !returnFound) {
            System.out.println("No return value for function of type " + type + " found.\n");
            System.exit(0);
        }

        return null;
    }

    public Boolean checkLiquidity(Environment sigma, String id, ArrayList<String> parlist, ArrayList<Node> oldAss) {
        Boolean isLiquid = true;
        sigma.enterScope();

        ArrayList<Node> ass = null;

        if (assets != null) {
            ((AdecNode)assets).checkLiquidity(sigma);

            ass = new ArrayList<Node>();

            ArrayList<String> assetsIds = ((AdecNode)assets).getIds();
            for (String aid : assetsIds) {
                ass.add(sigma.lookup(aid).getType());
            }

            if(parlist != null) {
                AssetTypeNode aux = null;
                for(int i=0; i < parlist.size(); i++){
                    aux = (AssetTypeNode)sigma.lookup(parlist.get(i)).getType();
                    
                    if(! (aux.isEmpty())){
                        aux.empty();
                        ((AssetTypeNode)ass.get(i)).fill();
                    }
                }
            } else {
                for(Node a : ass) {
                    ((AssetTypeNode)a).fill();
                }
            }

            /** DEBUG */
            //ArrayList<String> assetsIds = ((AdecNode)assets).getIds();
            for (String aid : assetsIds) {
                System.out.println("Asset " + aid + " empty: " + ((AssetTypeNode)sigma.lookup(aid).getType()).isEmpty() + "\n");
            }
            /** DEBUG */
        } 

        for(Node statement : statementlist){
            //System.out.println(statement.getClass().getName()+ "\n");
            if(statement instanceof MoveNode)
                ((MoveNode)statement).checkLiquidity(sigma);

            if(statement instanceof TransferNode)
                ((TransferNode)statement).checkLiquidity(sigma);

            if(statement instanceof IteNode){
                if (((IteNode)statement).checkLiquidity(sigma,id,ass) == null)
                    return null;
            }

            if(statement instanceof CallNode){
                if( ((CallNode)statement).getId().equals(id) ){            
                    if(oldAss != null){
                        //controllo di aver raggiunto il punto fisso
                        for(int i=0; i < oldAss.size(); i++){
                            //System.out.println("\n\n " + ((AssetTypeNode)ass.get(i)).isEmpty() + "\n");
                            if( ((AssetTypeNode)ass.get(i)).isEmpty() ^ ((AssetTypeNode)oldAss.get(i)).isEmpty() )
                                break;

                            if(i == oldAss.size()-1){
                                System.out.println("Fixpoint reached\n\n"); //punto fisso
                                //check
                                for(Node a : ass){

                                //System.out.println("\nAsset\n" + ((AssetTypeNode)a).isEmpty() + "\n");  
                                    if(! ((AssetTypeNode)a).isEmpty())
                                        return false;
                                }
                                return true;
                            }
                        }
                    }

                    //Call
                    if(!((CallNode)statement).checkLiquidity(sigma,ass))
                        isLiquid = false;

                    //f -> .... f-> chechliq -> f [0..0] -> f [0..0]
                } else{
                    Boolean temp =((CallNode)statement).checkLiquidity(sigma,null);
                    if(temp == null)
                        return null;
                    else if(temp == false)
                        isLiquid = false;
                }

            }
        }

        if(isLiquid && ass != null) {
            for (Node a : ass) {
                if(!((AssetTypeNode)a).isEmpty()) {
                    isLiquid = false;
                    break;
                }
            }
        }

        sigma.exitScope();

        return isLiquid;
    }

    @Override
    public String codeGeneration(){
        String funL = AssetLanlib.freshFunLabel(id);
        String stmCode = "";

        int k = 4;
        int kIn = 0; //space for inner declarations

        if(parameters != null){
           ArrayList<Node> decL = ((DecNode)parameters).getTypeList();
           for(Node t : decL){
               if(t instanceof IntTypeNode)
                   k+=4;
               else
                   k+=1;
           }
        }

        if(assets != null)
            k+= 4 * ((AdecNode)assets).getNumberOfAssets();

        for(Node in : declarations){
            ArrayList<Node> innerDecL = ((DecNode)in).getTypeList();

            for(Node t : innerDecL){
                if(t instanceof IntTypeNode)
                    kIn+=4;
                else
                    kIn+=1;
            }
        }

        for(Node s : statementlist)
            stmCode += s.codeGeneration();
        
        String ret="";
        if(type != null)
            ret+="move $a0 $v0\n";

        String allocateDec = "";
        if(kIn != 0)
            allocateDec+= "addi $sp $sp -"+kIn+"\n";

        AssetLanlib.putCode(funL+":\n"+
                            "move $fp $sp\n"+
                            "push $ra\n"+
                            allocateDec+
                            stmCode+
                            "lw $ra 0($sp)\n"+
                            "addi $sp $sp "+(k+kIn+4)+"\n"+  // (k+kIn+4) is the length of AR, + 4 for ra
                            "lw $fp 0($sp)\n"+
                            "pop\n"+
                            ret+
                            "jr $ra\n");
        
        return ""; 
    }

}