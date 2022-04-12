package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class IteNode extends Node{
    
  private Node cond;
  private Node th;
  private Node el;
  
  public IteNode (Node c, Node t, Node e) {
    cond=c;
    th=t;
    el=e;
  }
  
  public String toPrint(String s) {
    return s+"If\n" + cond.toPrint(s+"  ") 
                    + th.toPrint(s+"  ")   
                    + el.toPrint(s+"  "); 
  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
	  //create the result
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
	  
	  //check semantics in the condition
	  res.addAll(cond.checkSemantics(env));
	 	  
	  //check semantics in the then and in the else exp
	  res.addAll(th.checkSemantics(env));
	  res.addAll(el.checkSemantics(env));
	  
	  return res;
  }
  
}
