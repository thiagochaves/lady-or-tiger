package poc.puzzle;

/**
 * Indica que o arquivo informado � inv�lido, contendo uma representa��o errada
 * de um puzzle.
 * 
 * @author Thiago
 */
public class ExcecaoArquivoInvalido extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExcecaoArquivoInvalido() {
		super();
	}

	public ExcecaoArquivoInvalido(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcecaoArquivoInvalido(String message) {
		super(message);
	}

	public ExcecaoArquivoInvalido(Throwable cause) {
		super(cause);
	}
}
