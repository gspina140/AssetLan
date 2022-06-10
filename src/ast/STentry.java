package ast;


public class STentry {
 
	/**
	 * The nesting level of the entry
	 */
	private int nl;

	/**
	 * The type of the entry
	 */
	private Node type;

	/**
	 * The offset of the entry (for code generation, not used yet - 2022/04/21)
	 */
	private int offset;
	
	/**
	 * Class constructor taking as parameters the nesting level and an offset
	 * @param n nesting level of the entry
	 * @param os offset for code generation
	 * @return an object of type STentry
	 */
	public STentry (int n, int os) {
		nl	   = n;
		offset = os;
	} 
	
	/**
	 * Class constructor taking as parameters the nesting level, the type of entry and an offset
	 * @param n nesting level of the entry
	 * @param t type of the entry
	 * @param os offset for code generation
	 * @return an object of type STentry
	 */
	public STentry (int n, Node t, int os) {
		nl     = n;
		type   = t;
		offset = os;
	}

    public STentry(STentry e){
        this.nl = e.getNestinglevel();
        if(e.getType() instanceof IntTypeNode)
            this.type = new IntTypeNode();
        else if(e.getType() instanceof BoolTypeNode)
            this.type = new BoolTypeNode();
        else if(e.getType() instanceof AssetTypeNode) 
            this.type = new AssetTypeNode(e.getType());
        else if(e.getType() instanceof ArrowTypeNode)
            this.type = new ArrowTypeNode((ArrowTypeNode)e.getType());
        this.offset = e.getOffset();
    }
	
	/**
	 * Specify the type of the entry
	 * @param t the type of the entry
	 * @return void
	 */
	public void addType (Node t) {
		type = t;
	}
	
	/**
	 * Get the type of the entry
	 * @return a node containing the type of the entry
	 */
	public Node getType () {
		return type;
	}

	/**
	 * Get the offset for code generation (not used yet - 2022/04/21))
	 * @return the offset for code generation
	 */
	public int getOffset () {
		return offset;
	}
	
	/**
	 * Get the nesting level the entry is associated with
	 * @return the nesting level
	 */
	public int getNestinglevel () {
		return nl;
	}
	
	/**
     * Method to print a message containing information about the node
     * Useful for printing errors
     * @param s a string to use as the head of the message
     * @return the string containing the message
     */
	public String toPrint(String s) {
		return s + "STentry: nestlev " + Integer.toString(nl) + "\n"+
			   s + "STentry: type\n" + 
			   type.toPrint(s + "  ") + 
			   s + "STentry: offset " + Integer.toString(offset) + "\n";
	}
}  