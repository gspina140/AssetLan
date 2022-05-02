package ast;
import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class ArrowTypeNode implements Node {

  private ArrayList<Node> parlist;
  private int noassets;  // number of assets
  private Node ret;
  
  public ArrowTypeNode (ArrayList<Node> p, int noa, Node r) {
    parlist=p;
    noassets=noa;
    ret=r;
  }
    
  public String toPrint(String s) { //
  String parlstr="";
    for (Node par:parlist)
      parlstr+=par.toPrint(s+"  ");
  return s+"ArrowType\n" + parlstr + "Number of assets: " + noassets + ret.toPrint(s+"  ->") ; 
  }
  
  public Node getRet () { //
    return ret;
  }
  
  public ArrayList<Node> getParList () { //
    return parlist;
  }

  public int getNoa() {
    return this.noassets;
  }

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
    return new ArrayList<SemanticError>();
  }
  
  //non utilizzato
  public Node typeCheck () {
    return null;
  }

  //non utilizzato
  public String codeGeneration() {
    return "";
  }

}