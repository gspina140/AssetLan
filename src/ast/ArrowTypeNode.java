package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class ArrowTypeNode implements Node {

    private ArrayList<Node> parlist;
    private ArrayList<Node> asslist;
    private FunctionNode func;
    private int noassets; // number of assets
    private Node ret;

    public ArrowTypeNode(ArrayList<Node> p, ArrayList<Node> a, int noa, Node r) {
        parlist = p;
        asslist = a;
        noassets = noa;
        ret = r;
    }

    public ArrowTypeNode(ArrowTypeNode a) {
        new ArrowTypeNode(a.getParList(), a.getAssList(), a.getNoa(), a.getRet());
    }

    public String toPrint(String s) { //
        String parlstr = "";
        for (Node par : parlist)
            parlstr += par.toPrint(s + "  ");
        return s + "ArrowType\n" + parlstr + "Number of assets: " + noassets + ret.toPrint(s + "  ->");
    }

    public Node getRet() { //
        return ret;
    }

    public void addFunction(FunctionNode f) { //
        func = f;
    }

    public FunctionNode getFunction() { //
        return func;
    }

    public ArrayList<Node> getParList() { //
        return parlist;
    }

    public ArrayList<Node> getAssList() { //
        return asslist;
    }

    public int getNoa() {
        return this.noassets;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<SemanticError>();
    }

    // non utilizzato
    public Node typeCheck() {
        return null;
    }

    // non utilizzato
    public String codeGeneration() {
        return "";
    }

}