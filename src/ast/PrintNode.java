package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class PrintNode implements Node {

  private Node exp;
  
  public PrintNode (Node e) {
    exp = e;
  }
  
  public String toPrint(String s) {
    return s+"Print\n" + exp.toPrint(s+"  ") ;
  }
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {

 	  return exp.checkSemantics(env);
 	}
    
}  