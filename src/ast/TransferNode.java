package ast;
import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class TransferNode implements Node{
    
    private String id;
    
    public TransferNode(String id){
        this.id = id;
    }

    public String toPrint(String s){
        return s+"Transfer\n" + id;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        HashMap<String,STentry> hm = env.symTable.get(env.nestingLevel);

        if(hm.get(id) == null){
            int nl = env.nestingLevel - 1;
            hm = env.symTable.get(nl);

            while(nl >= 0){
                if(hm.get(id) != null){
                    return res;
                }else{
                    nl--;
                }
            }

            res.add(new SemanticError("Transfer id " + id + " undeclared"));
        }else return res;

        return res;
    }
}
