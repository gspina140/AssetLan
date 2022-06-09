package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;

import ast.AssetTypeNode;
import ast.IntTypeNode;
import ast.Node;
import ast.STentry;

public class Environment {
	
	/**
	 * Symbolic table, implemented as a list of hash-tables
	 */
	private ArrayList<HashMap<String,STentry>>  symTable;

	/**
	 * The current nesting level; outher nesting level is 0 (first position of ArrayList) instead of 1 (slides)
	 */
	private int nestingLevel = -1;

	/**
	 * The current offset (to this day unused, 2022/04/21 - it will be used for code generation)
	 */
	private int offset = 0;

	public Environment() {
		this.symTable = new ArrayList<HashMap<String,STentry>>();
	}
	 
	public Environment(Environment env) {
        this.symTable = new ArrayList<HashMap<String,STentry>>();

        for(HashMap<String,STentry> hm : env.getSymTable()) {
            HashMap<String,STentry> newhm = new HashMap<String,STentry>();
            for (Map.Entry<String, STentry> set : hm.entrySet()) {
                STentry aux = new STentry(set.getValue()) ;
                newhm.put(set.getKey(), aux);
            }
            this.symTable.add(newhm);
        }
        this.nestingLevel = env.getNestingLevel();
//		this.symTable = new ArrayList<HashMap<String,STentry>>(env.getSymTable());
 //       Collections.copy(this.symTable, env.getSymTable());
	}

	public ArrayList<HashMap<String,STentry>> getSymTable() {
		return this.symTable;
	}

	/**
	 * Enter a new scope (increase nesting level and add a new hash-table to the list)
	 * @param void
	 * @return void
	 */
	public void enterScope() {
		nestingLevel++;
        offset = 0;
		HashMap<String, STentry> hm = new HashMap<String, STentry>();
		symTable.add(hm);
	}

	/**
	 * Exit a scope (decrease nesting level and remove the last hash-table in the list)
	 * @param void
	 * @return void
	 */
	public void exitScope() {
		symTable.remove(nestingLevel--);
        //offset (backtrack)
        
        int x = 0;
        //int lastDeclared=0;
        for (Map.Entry<String, STentry> set : symTable.get(nestingLevel).entrySet()) {
            if(x >= set.getValue().getOffset()){
                x=set.getValue().getOffset();
                if(set.getValue().getType() instanceof IntTypeNode || set.getValue().getType() instanceof AssetTypeNode)
                    x-=4;
                else
                    x-=1;
            }
        }

        offset = x;
	}

	/**
	 * Get the scope of nesting level nl
	 * @param nl a nesting level
	 * @return the scope of nesting level nl (object of type HashMap<String, STentry>)
	 */
	private HashMap<String, STentry> getScope(int nl) {
		return symTable.get(nl);
	}

	/**
	 * Get the current scope (e.g., the head of the list of hash-tables)
	 * @param void
	 * @return the current scope (object of type HashMap<String, STentry>)
	 */
	private HashMap<String, STentry> getCurrentScope() {
		return getScope(getNestingLevel());
	}

	/**
	 * Add an entry to current scope
	 * @param Node the type of the symbol (can be null)
	 * @param String the id of the symbol
	 * @return returns the new entry if succeded (object of type STentry), null otherwise
	 */
	public STentry addEntry(Node type, String id) {

		STentry entry = null;
		if (type != null){
            if(type instanceof IntTypeNode || type instanceof AssetTypeNode){
			    entry = new STentry(nestingLevel, type, offset);
                offset-=4;
            }else
			    entry = new STentry(nestingLevel, type, offset--);
        }
		else
			entry = new STentry(nestingLevel, offset--);

		HashMap<String, STentry> hm = getCurrentScope();

		return hm.put(id, entry);
	}

	/**
	 * Get current nesting level
	 * @param void
	 * @return current nesting level
	 */
	public int getNestingLevel() {
		return nestingLevel;
	}

	/**
	 * Checks if the symbol has been declared in this scope (useful for declaration look-up)
	 * @param id the id of the symbol to be searched
	 * @param nl the nesting level in which we are looking for the symbol
	 * @return returns the symbol entry if found (object of type STentry), null otherwise
	 */
	private STentry checkDeclaration(String id, int nl) {
		HashMap<String, STentry> hm = getScope(nl);
		return hm.get(id);
	}

	/**
	 * Look-up for the declaration of a given id in the current scope or in an enclosing one
	 * Useful when we found the use of an id to check if it has been declared or not
	 * In a program, every di must be declared before use in the current scope or an enclosing one
	 * @param id the id to be searched
	 * @return the STentry if the id has been found, null otherwise
	 */
	public STentry lookup(String id) {

		// Get current nesting level
        int nl = getNestingLevel();

        // Look-up for the id
		while(nl >= 0){
			STentry tmp = checkDeclaration(id, nl);
            if(tmp != null){
                // Id found
                return tmp;
            } else {
                // Could not find the id in the current scope
                // Try searching in the enclosing scopes
				// (iteratively decrease the nesting level and check them)
                nl--;
            }
        }

        // If this point is reached, it means that the id has not been found
		return null;
	}

}