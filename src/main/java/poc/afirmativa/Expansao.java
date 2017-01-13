package poc.afirmativa;

/**
 * Tipo de expansão que a afirmativa realiza. 
 * @author Thiago
 */
public enum Expansao {
    /** Expansão proibida, pois não há o que expandir. */
    IDENTIDADE,
    /** Expansão do tipo alfa (conjunção). */
    ALFA,
    /** Expansão do tipo beta (disjunção). */
    BETA,
    /** Expansão devido à negação de uma negação. */
    GAMA
}