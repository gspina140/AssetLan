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
    private Node expressions;

    private int nl;

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
        aentries = new ArrayList<STentry>();
        expressions = null;
        this.entry = null;
    }

    public String getId() {
        return id;
    }

    public ArrayList<STentry> getAentries(){
        return aentries;
    }
    
    /**
     * Add asset id to the list of assets ids
     * @param id the asset id to be added to the list
     * @return void
     */
    public void addId(String id) {
        idlist.add(id);
    }

    public void addExp(Node e){
        expressions = e;
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

        // String containing the ids of the assets
        String i = "";

        for(String p : idlist)
            i += p + " ";

        if(expressions != null)
            return s + "Call:\t" + id + ", parameters " + expressions.toPrint(s + " ") + ", assets " + i; 
        else
            return s + "Call:\t" + id + ", assets " + i; 
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
        res.addAll(expressions.checkSemantics(env));
        
        // Look-up for each asset id
        for (String a : idlist) {
            STentry aentry = env.lookup(a);
            
            // Look-up for the asset id a
            if (aentry == null)
                // The id has not been found and an error should be provided
                res.add(new SemanticError("Asset " + a + " han not been declared"));
            
            aentries.add(aentry);
        }

        nl = env.getNestingLevel();

        return res;
    }

    @Override
    public Node typeCheck() {

        ArrowTypeNode t=null;
        if (entry.getType() instanceof ArrowTypeNode) t=(ArrowTypeNode) entry.getType(); 
        else {
          System.out.println("Invocation of a non-function "+id);
          System.exit(0);
        }

        ArrayList<Node> parlist = t.getParList();
        ArrayList<Node> pars = new ArrayList<Node>();

        if(expressions != null) {
            pars = ((ExpListNode)expressions).getExps();

            if (parlist.size() != pars.size()) {
                System.out.println("Wrong number of parameters for function "+id);
                System.exit(0);
            }

            for (int i = 0; i < pars.size(); i++) {
                if (! (AssetLanlib.isSubtype(pars.get(i).typeCheck(), parlist.get(i) ) ) ) {
                    System.out.println("Error type in expression " + pars.get(i) + "Expecting: " + parlist.get(i));
                    System.exit(0);
                }
            }
        }

        int noa = t.getNoa();

        if(idlist.size() > 0) {
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
        }

        return t.getRet();
    }

    public Boolean checkLiquidity(Environment sigma, ArrayList<Node> oldAss) {
        // Look-up for each asset id
        // for (String a : idlist) {
        //     STentry aentry = sigma.lookup(a);
        //     ((AssetTypeNode)aentry.getType()).empty();
        // }

        return ((ArrowTypeNode)entry.getType()).getFunction().checkLiquidity(sigma, id, idlist, oldAss);    
    } 

    @Override
    public String codeGeneration(){
        String parCode = "";
        String assCode = "";
        String getAR   = ""; //AR where the fun is defined (is always 0)

        if(expressions != null){
           ArrayList<Node> expr = ((ExpListNode)expressions).getExps();

           for(int i=expr.size()-1; i>=0; i--){
               parCode+= expr.get(i).codeGeneration()+"\n";
               parCode+= "push $a0\n";
           }
        }

        for(int i=aentries.size()-1; i>=0;i--){
            System.out.println("DIREI CHE : "+idlist.get(i)+"\n");
            for(int j=0; j< nl-aentries.get(i).getNestinglevel();j++)
                assCode+="lw $al 0($al)\n";
            assCode+="lw $a0 "+aentries.get(i).getOffset()+"($al)\n"+
                    "push $a0\n"+
                    "li $t1 0\n"+
                    "sw $t1 "+aentries.get(i).getOffset()+"($al)\n";
        }

        for(int i=0; i<nl-entry.getNestinglevel();i++) 
            getAR+="lw $al 0($al)\n";

        return "push $fp\n"+
                parCode+
                "move $al $fp\n"+
                assCode+
                "move $al $fp\n"+
                getAR+
                "push $al\n"+
                "jal function_"+id+"\n";
    }
}
