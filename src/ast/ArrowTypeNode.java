package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class ArrowTypeNode implements Node {

    /**
     * A list containing the declarations of the parameters of the function
     */
    private ArrayList<Node> parlist;

    /**
     * A list containing the declarations of the assets of the function
     */
    private ArrayList<Node> asslist;

    /**
     * The relative function node where the body is stored (used for checkLiquidity in recursive functions)
     */
    private FunctionNode func;

    /**
     * Number of assets
     */
    private int noassets;

    /**
     * The return type
     */
    private Node ret;

    /**
     * The constructor of the ArrowTypeNode class
     * @param p the list of function parameters
     * @param a the list of function assets
     * @param noa the number of assets
     * @param r the return type
     * @return an object of type ArrowTypeNode
     */
    public ArrowTypeNode(ArrayList<Node> p, ArrayList<Node> a, int noa, Node r) {
        parlist = p;
        asslist = a;
        noassets = noa;
        ret = r;
    }

    /**
     * Another constructor of the ArrowTypeNode class
     * @param a an ArrowTypeNode to copy
     * @return an object of type ArrowTypeNode
     */
    public ArrowTypeNode(ArrowTypeNode a) {
        new ArrowTypeNode(a.getParList(), a.getAssList(), a.getNoa(), a.getRet());
    }

    /**
     * Getter of the return type node
     * @param void
     * @return the return type node
     */
    public Node getRet() {
        return ret;
    }

    /**
     * Setter of the function node relative to the ArrowTypeNode
     * @param f the function node
     * @return void
     */
    public void addFunction(FunctionNode f) {
        func = f;
    }

    /**
     * Getter of the function node relative to the ArrowTypeNode
     * @param void
     * @return the function node
     */
    public FunctionNode getFunction() {
        return func;
    }

    /**
     * Getter of the list of parameters
     * @param void
     * @return the list of parameters
     */
    public ArrayList<Node> getParList() {
        return parlist;
    }

    /**
     * Getter of the list of assets
     * @param void
     * @return the list of assets
     */
    public ArrayList<Node> getAssList() {
        return asslist;
    }

    /**
     * Getter of the number of assets
     * @param void
     * @return the number of assets
     */
    public int getNoa() {
        return this.noassets;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    public String toPrint(String s) {
        String parlstr = "";
        for (Node par : parlist)
            parlstr += par.toPrint(s + "  ");
        return s + "ArrowType\n" + parlstr + "Number of assets: " + noassets + ret.toPrint(s + "  ->");
    }

    /**
     * Override of the checkSemantics function
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<SemanticError>();
    }

    /**
     * Function for type checking
     * @param void
     * @return void
     */
    @Override
    public Node typeCheck() {
        return null;
    }

    /**
     * Function for code generation
     * @param void
     * @return void
     */
    @Override
    public String codeGeneration() {
        return "";
    }

}