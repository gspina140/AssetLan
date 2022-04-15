package ast;

public class BoolExpNode {
    private boolean val;
  
    public BoolNode (boolean n) {
        val=n;
    }
  
    public String toPrint(String s) {
        if (val) return s+"Bool:true\n";
        else return s+"Bool:false\n";  
    }
  
    @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {
 	  return new ArrayList<SemanticError>();
 	}
  
}
