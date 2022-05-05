package ast;

import java.util.ArrayList;

import util.AssetLanlib;
import util.Environment;
import util.SemanticError;

public class InitCallNode implements Node {

	/**
     * The id of the init function
     */
	private String id;

    private Node parameters;

    private Node assets;

    private STentry entry;
	
	/**
     * Class constructor; it takes as parameter the id of the function and instantiates
	 * a list containing the espressions defining the parameters and the assets
     * @param id a String containing the id of the function
     * @return an object of type InitCallNode
     */
	public InitCallNode(String id) {
		this.id    = id;
		parameters = null;
        assets     = null;
	}

    public void addPar(Node p) {
        parameters = p;
    }

    public void addAs(Node as) {
        assets = as;
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
        return s + "Initialization call:\t" + id + parameters.toPrint(s + " ") + assets.toPrint(s + " ");
	}
	
	/**
     * Override of the checkSemantics function
     * Check for possible semantics errors when calling a init function
     * It checks if the function id and the ids contained in the expressions that define the
     * parameters and the assets have been declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
	@Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        
        entry = env.lookup(id);
		// Look-up for the function id
        if (entry == null)
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Function " + id + " han not been declared"));
             	
        // Delegate semantic check of expressions that define the parameters and the assets to relative nodes
		if(parameters != null)
            res.addAll(parameters.checkSemantics(env));

        if(assets != null)
            res.addAll(assets.checkSemantics(env));

        return res;
	}

    @Override
    public Node typeCheck(){

        ArrayList<Node> parlist = ((ArrowTypeNode)entry.getType()).getParList();
        
        ArrayList<Node> pars = new ArrayList<Node>();

        if(parameters != null)
		    pars = ((ExpListNode)parameters).getExps();

        ArrayList<Node> aslist= new ArrayList<Node>();
        
        if(assets != null)
            aslist = ((ExpListNode)assets).getExps();

        int noa = ((ArrowTypeNode)entry.getType()).getNoa();

        if(parlist.size() != pars.size()){
            System.out.println("Error: wrong number of parameters in initialization call for function " + id);
            System.exit(0);
        }

        for(int i = 0; i < pars.size(); i++){
            if(! (AssetLanlib.isSubtype(parlist.get(i), pars.get(i).typeCheck()))){
                System.out.println("Type error, expression : " + pars.get(i) + "expecting: " + parlist.get(i));
                System.exit(0);
            }
        }

        if(aslist.size() != noa){
            System.out.println("Error: wrong number of assets in initialization call for function " + id);
            System.exit(0);
        }

        for(int i = 0; i < aslist.size(); i++){
            if(! (aslist.get(i).typeCheck() instanceof AssetTypeNode)){
                System.out.println("Type error, expression : " + aslist.get(i) + "is not of an asset");
                System.exit(0);
            }
        }

        return ((ArrowTypeNode) entry.getType()).getRet();
    }
}
