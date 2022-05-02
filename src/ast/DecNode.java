package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class DecNode implements Node{

    /**
     * A list containing the types of the declared variables (can be 'int' or 'bool')
     * It contains at least one value
     * tyepList[i] corresponds to the type of declaration of id idList[i]
     */
    private ArrayList<Node> typeList;

    /**
     * A list containing the ids of the newly declared variables
     * It contains at least one id
     */
    private ArrayList<String> idList;

    /**
     * Class constructor using the first declaration (it is always present) id and type
     * @param t a node containing the type of the first declaration
     * @param i a String containing the id of the first declaration
     * @return an object of type DecNode
     */
    public DecNode(Node t, String i){
        typeList = new ArrayList<Node>();
        idList = new ArrayList<String>();
        addDeclaration(t,i);
    }

    /**
     * Add declaration to the node
     * It adds the type to the typeList and the id to the idList
     * tyepList[i] corresponds to the type of declaration of id idList[i]
     * @param t a node containing the type of the declaration
     * @param i a String containing the id of the declaration
     * @return void
     */
    public void addDeclaration(Node t, String i){
        typeList.add(t);
        idList.add(i);
    }

    public ArrayList<Node> getTypeList() {
        return this.typeList;
    }

    /**
     * Override of the toPrint method
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
    @Override
    public String toPrint(String s){

        // String containing information about the declarations in the form type + id
        String d = "";

        for (int i = 0; i < idList.size(); i++) {
            d += typeList.get(i).toPrint(s + " ");
            d += idList.get(i);
        }

    	return s+"Declaration(s):\t" + d;
    }

    /**
     * Override of the checkSemantics function
     * Check for possible semantics errors when introducing new declarations
     * It checks if the ids have already been used in this scope and if so it provides an error
     * @param env the environment in which the check takes place (it contains the symTable)
     * @return a list of semantic errors (can be empty)
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env){

        // Create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();  
        
        // For every id try introducing it to the symTable and, if already declared, give an error
        for(int i = 0; i < typeList.size(); i++){

            // Introducing "entry"
            // If addEntry returns null, it means that another declaration with the same id
            // has been found in the same scope, and therefore an "already declared id" is provided
            if( env.addEntry(typeList.get(i), idList.get(i)) != null)
                res.add(new SemanticError("Error when declaring variable of id " + idList.get(i) +"\n" +
                                          "Id already used for declaration in the same scope"));
        }

        return res;
    }

    @Override
    public Node typeCheck() {
        return null;
    }
}
