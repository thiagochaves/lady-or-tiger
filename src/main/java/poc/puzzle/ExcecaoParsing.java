package poc.puzzle;

/**
 * Um erro durante o parsing de uma expressão.
 * 
 * @author Thiago
 */
class ExcecaoParsing extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExcecaoParsing(String message) {
		super(message);
	}

	public ExcecaoParsing(String message, Throwable cause) {
		super(message, cause);
	}
}
