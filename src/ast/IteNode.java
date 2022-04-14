package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class IteNode implements Node{
    
  private Node cond;
  private ArrayList<Node> StatementsList = new ArrayList<Node>();
  //private ArrayList<Node> elseStatementsList = new ArrayList<Node>();
  /*private Node th;
  private Node el;*/
  
  public IteNode (Node c/*, Node t, Node e*/) {
    cond=c;/*
    th=t;
    el=e;*/
  }
  
  public String toPrint(String s) {
    String str = "";
    for(Node st:StatementsList)
        str += st.toPrint(s+ "");    

    return s+"If\n" + cond.toPrint(s+"  ") 
                    + str;
  }
  
  public void addStatement(Node st){
    StatementsList.add(st);
  }

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
	  //create the result
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
	  
	  //check semantics in the condition
	  res.addAll(cond.checkSemantics(env));
	 	  
	  //check semantics in the then and in the else statement
      for(Node st:StatementsList)
        res.addAll(st.checkSemantics(env));

	  return res;
  }
  
}
