package ast;

import java.util.ArrayList;
import java.util.HashMap;

import util.Environment;
import util.SemanticError;

public class FieldNode implements Node {

    /**
     * The type of the field declaration (can be 'int' or 'bool')
     */
    private Node type;

    /**
     * The id of the field declaration
     */
    private String id;

    /**
     * The expression defining the value to assign the field (can be empty)
     */
    private Node exp;

    /**
     * The class constructor; it take as parameters the type, the id and the expression (null if it is not present)
     * @param t the type of the field declaration (can be 'int' or 'bool')
     * @param i yhe id of the field declaration
     * @param e the node containing the expression defining the value to assign the field (can be null)
     */
    public FieldNode(Node t, String i, Node e) {
        type = t;
        id   = i;
        exp  = e;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    @Override
    public String toPrint(String s) {
        return s + "Field\t" + type.toPrint(s + " ") + id + exp.toPrint(s + " ");
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when declaring the field
     * It checks if the id has already been used
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();

        // Introducing "entry"
        // If addEntry returns null, it means that another declaration with the same id
        // has been found in the same scope, and therefore an "already declared id" is provided
        if (env.addEntry(type, id) != null)
            res.add(new SemanticError("Error when declaring field of id " + idList.get(i) +"\n" +
                                      "Id already used for declaration in the same scope"));

        // If there is an expression, delegate semantic check of expression to relative node
        if(exp != null)
            res.addAll(exp.checkSemantics(env));

        return res;
    }
}