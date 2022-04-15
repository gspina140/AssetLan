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
    }

    public void addId(String id) {
        idlist.add(id);
    }

    public void addExp(Node n) {
        explist.add(n);
    }

    @Override
    public String toPrint(String indent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        HashMap<String, STentry> hm = env.symTable.get(env.nestingLevel);

        if (hm.get(id) == null) {
            int nl = env.nestingLevel - 1;

            while (nl >= 0) {
                hm = env.symTable.get(nl);

                if (hm.get(id) == null) {
                    nl--;
                } else {
                    for (Node e : explist) {
                        res.addAll(e.checkSemantics(env));
                    }
                    for (String s : idlist) {
                        if (hm.get(s) == null) {
                            int nl2 = nl--;

                            HashMap<String, STentry> hm2;
                            while (nl2 >= 0) {
                                hm2 = env.symTable.get(nl2);
                                if (hm2.get(s) == null) {
                                    nl2--;
                                } else {
                                    break;
                                }

                                if (nl2 < 0) {
                                    res.add(new SemanticError("Variable id" + s + "has not been declared"));
                                }
                            }
                        }
                    }
                    break;
                }

                if (nl < 0) {
                    res.add(new SemanticError("Function id" + id + "has not been declared"));
                }
            }

        } else {
            for (Node e : explist) {
                res.addAll(e.checkSemantics(env));
            }
            for (String s : idlist) {
                if (hm.get(s) == null) {
                    int nl2 = env.nestingLevel;

                    HashMap<String, STentry> hm2;
                    while (nl2 >= 0) {
                        hm2 = env.symTable.get(nl2);
                        if (hm2.get(s) == null) {
                            nl2--;
                        } else {
                            break;
                        }

                        if (nl2 < 0) {
                            res.add(new SemanticError("Variable id" + s + "has not been declared"));
                        }
                    }
                }
            }
        }
        return res;
    }

}
