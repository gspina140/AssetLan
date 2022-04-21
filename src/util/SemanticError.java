package util;

public class SemanticError {
	
	/**
	 * A string containing the error message
	 */
	public final String msg;
	
	/**
	 * Class constructor
	 * @param String msg the error message
	 * @return SemanticError an object of type SemanticError with error string msg
	 */
	public SemanticError(String msg) {
		this.msg = msg;
	}
	
	/**
	 * Override of the function to cast from SemanticError to String
	 * @param void
	 * @return String a string containing the error message
	 */
	@Override
	public String toString() {		
		return msg;
	}	
}