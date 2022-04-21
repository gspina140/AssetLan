package util;

public class SemanticError {
	
	/**
	 * A string containing the error message
	 */
	public final String msg;
	
	/**
	 * Class constructor
	 * @param String the error message
	 * @return an object of type SemanticError with error string msg
	 */
	public SemanticError(String msg) {
		this.msg = msg;
	}
	
	/**
	 * Override of the function to cast from SemanticError to String
	 * @param void
	 * @return a string containing the error message
	 */
	@Override
	public String toString() {		
		return msg;
	}	
}