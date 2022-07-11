package poc;

/**
 * Um erro de implementação. Significa que um acontecimento não previsto ocorreu.
 * 
 * @author Thiago
 */
public class ExcecaoImplementacao extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExcecaoImplementacao(String message) {
        super(message);
    }

    public ExcecaoImplementacao(String message, Throwable cause) {
        super(message, cause);
    }
}
