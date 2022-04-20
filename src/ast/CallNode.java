package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class CallNode implements Node {

    private String id;
    private ArrayList<String> idlist;
    private ArrayList<Node> explist;

    public CallNode(String id) {
        this.id = id;
        idlist  = new ArrayList<String>();
        explist = new ArrayList<Node>();
    }

    public void addId(String id) {
        idlist.add(id);
    }

    public void addExp(Node n) {
        explist.add(n);
    }

    @Override
    public String toPrint(String s) {    
        String i = "";
        String e = "";
        
        for(String p : idlist)
            i += p;

        for(Node a : explist)
            e += a.toPrint(s + " ");

        return s + "Call\n" + id + i + e; 
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        //HashMap<String, STentry> hm = env.symTable.get(env.nestingLevel);

        int nl = env.getNestingLevel();

        if (env.checkDeclaration(id, nl) == null) {
            nl--;

            while (nl >= 0) {
                //hm = env.symTable.get(nl);

                if (env.checkDeclaration(id, nl) == null) {
                    nl--;
                } else {
                    for (Node e : explist) {
                        res.addAll(e.checkSemantics(env));
                    }
                    for (String s : idlist) {
                        int nl2 = env.getNestingLevel();
                        if (env.checkDeclaration(s, nl2) == null) {
                            nl2--;

                            //HashMap<String, STentry> hm2;
                            while (nl2 >= 0) {
                                //hm2 = env.symTable.get(nl2);
                                if (env.checkDeclaration(s, nl2) == null) {
                                    nl2--;
                                } else {
                                    break;
                                }

                                if (nl2 < 0) {
                                    res.add(new SemanticError("Variable id " + s + " has not been declared"));
                                }
                            }
                        }
                    }
                    break;
                }

                if (nl < 0) {
                    res.add(new SemanticError("Function id " + id + " has not been declared"));
                }
            }

        } else {
            for (Node e : explist) {
                res.addAll(e.checkSemantics(env));
            }
            for (String s : idlist) {
                int nl2 = env.getNestingLevel();
                if (env.checkDeclaration(s, nl2) == null) {
                    nl2--;

                    //HashMap<String, STentry> hm2;
                    while (nl2 >= 0) {
                        //hm2 = env.symTable.get(nl2);
                        if (env.checkDeclaration(s, nl2) == null) {
                            nl2--;
                        } else {
                            break;
                        }

                        if (nl2 < 0) {
                            res.add(new SemanticError("Variable id " + s + " has not been declared"));
                        }
                    }
                }
            }
        }
        return res;
    }

}
