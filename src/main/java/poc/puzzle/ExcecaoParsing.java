package poc.puzzle;

/**
 * Um erro durante o parsing de uma expressão.
 * 
 * @author Thiago
 */
public class ExcecaoParsing extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExcecaoParsing() {
	}

	public ExcecaoParsing(String message) {
		super(message);
	}

	public ExcecaoParsing(Throwable cause) {
		super(cause);
	}

	public ExcecaoParsing(String message, Throwable cause) {
		super(message, cause);
	}
}
