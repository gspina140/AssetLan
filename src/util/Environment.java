package util;

import java.util.ArrayList;
import java.util.HashMap;

import ast.Node;
import ast.STentry;

public class Environment {
	
	/**
	 * Symbolic table, implemented as a list of hash-tables
	 */
	private ArrayList<HashMap<String,STentry>>  symTable = new ArrayList<HashMap<String,STentry>>();

	/**
	 * The current nesting level; outher nesting level is 0 (first position of ArrayList) instead of 1 (slides)
	 */
	private int nestingLevel = -1;

	/**
	 * The current offset (to this day unused, 2022/04/21 - it will be used for code generation)
	 */
	private int offset = 0;

	/**
	 * Enter a new scope (increase nesting level and add a new hash-table to the list)
	 * @param void
	 * @return void
	 */
	public void enterScope() {
		nestingLevel++;
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
	}

	/**
	 * Get the scope of nesting level nl
	 * @param int a nesting level
	 * @return the scope of nesting level nl (object of type HashMap<String, STentry>)
	 */
	public HashMap<String, STentry> getScope(int nl) {
		return symTable.get(nl);
	}

	/**
	 * Get the current scope (e.g., the head of the list of hash-tables)
	 * @param void
	 * @return the current scope (object of type HashMap<String, STentry>)
	 */
	public HashMap<String, STentry> getCurrentScope() {
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
		if (type != null)
			entry = new STentry(nestingLevel, type, offset--);
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
	 * @param String the id of the symbol to be searched
	 * @param int the nesting level in which we are looking for the symbol
	 * @return returns the symbol entry if found (object of type STentry), null otherwise
	 */
	public STentry checkDeclaration(String id, int nl) {
		HashMap<String, STentry> hm = getScope(nl);
		return hm.get(id);
	}
}