package ast;

import java.util.ArrayList;
    
import util.Environment;
import util.SemanticError;
    
public class ValExpNode implements Node{
    private Integer val;

    public ValExpNode(Integer n) {
      val=n;
    }

    public String toPrint(String s) {
      return s+"Int:" + Integer.toString(val) +"\n";
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<SemanticError>();
    }
     
}