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

    private int nl;

    /**
     * Class constructor; it takes as parameter the id of the function and
     * instantiates
     * a list containing the espressions defining the parameters and the assets
     * 
     * @param id a String containing the id of the function
     * @return an object of type InitCallNode
     */
    public InitCallNode(String id) {
        this.id = id;
        parameters = null;
        assets = null;
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
     * 
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    @Override
    public String toPrint(String s) {

        if (parameters != null && assets != null)
            return s + "Initialization call:\t" + id + ", parameters " + parameters.toPrint(s + " ") + ", assets "
                    + assets.toPrint(s + " ");
        else if (parameters != null && assets == null)
            return s + "Initialization call:\t" + id + ", parameters " + parameters.toPrint(s + " ");
        else if (parameters == null && assets != null)
            return s + "Initialization call:\t" + id + ", assets " + assets.toPrint(s + " ");
        else
            return s + "Initialization call:\t" + id;
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when calling a init function
     * It checks if the function id and the ids contained in the expressions that
     * define the
     * parameters and the assets have been declared in this scope or in an enclosing
     * one
     * 
     * @param env the environment in which the check takes place (it contains the
     *            symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        entry = env.lookup(id);
        // Look-up for the function id
        if (entry == null)
            // The id has not been found and an error should be provided
            res.add(new SemanticError("Function " + id + " han not been declared"));

        // Delegate semantic check of expressions that define the parameters and the
        // assets to relative nodes
        if (parameters != null)
            res.addAll(parameters.checkSemantics(env));

        if (assets != null)
            res.addAll(assets.checkSemantics(env));

        nl = env.getNestingLevel();

        return res;
    }

    @Override
    public Node typeCheck() {

        ArrowTypeNode t = null;
        if (entry.getType() instanceof ArrowTypeNode)
            t = (ArrowTypeNode) entry.getType();
        else {
            System.out.println("Invocation of a non-function " + id);
            System.exit(0);
        }

        ArrayList<Node> parlist = t.getParList();

        ArrayList<Node> pars = new ArrayList<Node>();

        if (parameters != null) {
            pars = ((ExpListNode) parameters).getExps();

            if (parlist.size() != pars.size()) {
                System.out.println("Error: wrong number of parameters in initialization call for function " + id);
                System.exit(0);
            }

            for (int i = 0; i < pars.size(); i++) {
                if (!(AssetLanlib.isSubtype((pars.get(i)).typeCheck(), parlist.get(i)))) {
                    System.out.println("Type error, expression : " + pars.get(i) + "expecting: " + parlist.get(i));
                    System.exit(0);
                }
            }
        }

        ArrayList<Node> aslist = new ArrayList<Node>();

        if (assets != null) {
            aslist = ((ExpListNode) assets).getExps();

            int noa = t.getNoa();

            if (aslist.size() != noa) {
                System.out.println("Error: wrong number of assets in initialization call for function " + id);
                System.exit(0);
            }

            for (int i = 0; i < aslist.size(); i++) {
                if (!(aslist.get(i).typeCheck() instanceof IntTypeNode)) { // For initialization calls, integers or
                                                                           // assets variables are allowed
                    System.out.println("Type error, expression : " + aslist.get(i) + "is not an integer");
                    System.exit(0);
                }
            }
        }

        return t.getRet();
    }

    public Boolean checkLiquidity(Environment sigma, int verbosity) {
        ArrowTypeNode t = (ArrowTypeNode) entry.getType();
        return t.getFunction().checkLiquidity(sigma, id, null, null, verbosity);
    }

    @Override
    public String codeGeneration() {
        String parCode = "";
        String assCode = "";
        String getAR = "";

        if (parameters != null) {
            ArrayList<Node> expr = ((ExpListNode) parameters).getExps();

            for (int i = expr.size() - 1; i >= 0; i--) {
                parCode += expr.get(i).codeGeneration();
                parCode += "push $a0\n";
            }
        }

        if (assets != null) {
            ArrayList<Node> assExpr = ((ExpListNode) assets).getExps();

            for (int i = assExpr.size() - 1; i >= 0; i--) {
                assCode += assExpr.get(i).codeGeneration();
                assCode += "push $a0\n";
            }
        }

        for (int i = 0; i < nl - entry.getNestinglevel(); i++)
            getAR += "lw $al 0($al)\n";

        return "sw $fp 0($sp)\n" +
                assCode +
                parCode +
                "move $al $fp\n" +
                getAR +
                "push $al\n" +
                "jal function_" + id + "\n";
    }

}
