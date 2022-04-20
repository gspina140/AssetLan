package util;

import java.util.ArrayList;
import java.util.HashMap;

import ast.STentry;
import ast.Node;

public class Environment {
	
	private ArrayList<HashMap<String,STentry>>  symTable = new ArrayList<HashMap<String,STentry>>();
	private int nestingLevel = -1;
	private int offset = 0;
	//livello ambiente con dichiarazioni piu' esterno � 0 (prima posizione ArrayList) invece che 1 (slides)
	//il "fronte" della lista di tabelle � symTable.get(nestingLevel)

	public void enterScope() {
		nestingLevel++;
		HashMap<String, STentry> hm = new HashMap<String, STentry>();
		symTable.add(hm);
	}

	public void exitScope() {
		symTable.remove(nestingLevel--);
	}

	public HashMap<String, STentry> getScope(int nl) {
		return symTable.get(nl);
	}

	public HashMap<String, STentry> getCurrentScope() {
		return getScope(nestingLevel);
	}

	public STentry addEntry(Node type, String id) {

		STentry entry = null;
		if (type != null)
			entry = new STentry(nestingLevel, type, offset--);
		else
			entry = new STentry(nestingLevel, offset--);

		HashMap<String, STentry> hm = getCurrentScope();

		return hm.put(id, entry);
	}

	public int getNestingLevel() {
		return nestingLevel;
	}

	public STentry checkDeclaration(String id, int nl) {
		HashMap<String, STentry> hm = getScope(nl);
		return hm.get(id);
	}
}