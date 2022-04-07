package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public interface Node {
   
  String toPrint(String indent);
  
  ArrayList<SemanticError> checkSemantics(Environment env);

  //Node typeCheck();
  
  //String codeGeneration();
  
}  