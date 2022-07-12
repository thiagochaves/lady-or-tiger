package poc;

/**
 * Um erro de implementa��o. Significa que um acontecimento n�o previsto
 * ocorreu.
 * 
 * @author Thiago
 */
public class ExcecaoImplementacao extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExcecaoImplementacao() {
	}

	public ExcecaoImplementacao(String message) {
		super(message);
	}

	public ExcecaoImplementacao(Throwable cause) {
		super(cause);
	}

	public ExcecaoImplementacao(String message, Throwable cause) {
		super(message, cause);
	}
}
