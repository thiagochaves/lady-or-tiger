package poc.puzzle;

/**
 * Indica que o arquivo informado é inválido, contendo uma representação errada de um puzzle.
 * 
 * @author Thiago
 */
class ExcecaoArquivoInvalido extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExcecaoArquivoInvalido(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcecaoArquivoInvalido(String message) {
        super(message);
    }

}
