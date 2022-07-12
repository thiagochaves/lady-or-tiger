package poc.afirmativa;

/**
 * Tipo de expans�o que a afirmativa realiza. 
 * @author Thiago
 */
public enum Expansao {
    /** Expans�o proibida, pois n�o h� o que expandir. */
    IDENTIDADE,
    /** Expans�o do tipo alfa (conjun��o). */
    ALFA,
    /** Expans�o do tipo beta (disjun��o). */
    BETA,
    /** Expans�o devido � nega��o de uma nega��o. */
    GAMA,
    /** Expans�o de uma refer�ncia para seu conte�do. */
    DELTA
}