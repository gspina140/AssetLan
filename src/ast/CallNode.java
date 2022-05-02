package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;
import util.AssetLanlib;

public class CallNode implements Node {

    /**
     * The id of the called function
     */
    private String id;

    /**
     * The expressions defining the parameters
     */
    private ArrayList<Node> explist;

    /**
     * The ids of the function assets
     */
    private ArrayList<String> idlist;

    private STentry entry;

    private ArrayList<STentry> aentries;

    /**
     * Class constructor; it takes as parameter the id of the function and instantiates two lists
     * containing the espressions defining the parameters and the id of the assets respectively
     * @param id a String containing the id of the function
     * @return an object of type CallNode
     */
    public CallNode(String id) {
        this.id = id;
        idlist  = new ArrayList<String>();
        explist = new ArrayList<Node>();
        this.entry = null;
    }

    /**
     * Add expression node to the list of expressions that define the parameters
     * @param n the node containing the expression
     * @return void
     */
    public void addExp(Node n) {
        explist.add(n);
    }

    /**
     * Add asset id to the list of assets ids
     * @param id the asset id to be added to the list
     * @return void
     */
    public void addId(String id) {
        idlist.add(id);
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

        // String containing the expressions that define the parameters
        String e = "";   

        // String containing the ids of the assets
        String i = "";

        for(Node a : explist)
            e += a.toPrint(s + " ");
        
        for(String p : idlist)
            i += p;

        return s + "Call:\t" + id + e + i; 
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when calling a function
     * It checks if the function id, the ids contained in the expressions that define the
     * parameters and the assets ids have been declared in this scope or in an enclosing one
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // Look-up for the function id
        entry = env.lookup(id);
        if (entry == null)
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Function " + id + " han not been declared"));

        // Delegate semantic check of expressions that define the parameters to relative nodes
        for (Node e : explist) {
            res.addAll(e.checkSemantics(env));
        }
        
        // Look-up for each asset id
        for (String a : idlist) {
            aentries.add(env.lookup(a));
            // Look-up for the asset id a
            if (aentries.get(aentries.size()) == null)
                // The id has not been found and an error should be provided
                res.add(new SemanticError("Asset " + a + " han not been declared"));
        }

        return res;
    }

    @Override
    public Node typeCheck() {
        ArrayList<Node> parlist = ((ArrowTypeNode)entry.getType()).getParList();
        int noa = ((ArrowTypeNode)entry.getType()).getNoa();
        if (parlist.size() != explist.size()) {
            System.out.println("Wrong number of parameters for function "+id);
            System.exit(0);
        }
        for (int i = 0; i < explist.size(); i++) {
            if (! (AssetLanlib.isSubtype(explist.get(i).typeCheck(),parlist.get(i) ) ) ) {
                System.out.println("Error type in expression " + explist.get(i) + "Expecting: " + parlist.get(i));
                System.exit(0);
            }
        }
        if (idlist.size() != noa) {
            System.out.println("Wrong number of assets for function "+id);
            System.exit(0);
        }
        for (int i = 0; i < aentries.size(); i++) {
            if (! (aentries.get(i).getType() instanceof AssetTypeNode) ) {
                System.out.println("Type error: " + idlist.get(i) + " is not of an asset");
                System.exit(0);
            }
        }
        return null;
    }

}