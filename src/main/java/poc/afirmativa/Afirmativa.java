package poc.afirmativa;

import java.util.Collection;
import java.util.Set;

import poc.puzzle.Puzzle;

/**
 * � uma afirmativa l�gica.
 */
public interface Afirmativa {
    
    /**
     * Indica se a afirmativa pode ser expandida.
     */
    boolean eExpansivel();
    
    /**
	 * Expande a afirmativa. Retorna os componentes da mesma.
	 * 
	 * @return Se a expans�o for do tipo gama, retorna um elemento apenas. Se
	 *         for do tipo alfa ou beta retorna dois elementos.
	 */
	Set<Afirmativa> expandir();

	/**
	 * Indica o tipo de expans�o que o m�todo expandir() far�.
	 */
	Expansao getTipoExpansao();
	
	/**
	 * Se a afirmativa � uma nega��o dupla, elimina as nega��es.
	 * O resultado nunca ser� uma nega��o dupla
	 */
	Afirmativa resolverNegacoesDuplas();
	
	/**
	 * Liga a afirmativa ao puzzle informado.
	 * Permite a refer�ncia cruzada �s afirmativas das portas.
	 * @param puzzle
	 */
	void associarAPuzzle(Puzzle puzzle);
	
	boolean eEssencial();

    /**
     * Afirmativas que explicitam todos os objetos que n�o est�o neste lugar.
     * Vazio se n�o se aplicar.
     */
    Collection<Negacao> explicitarObjetosQueNaoEstaoAqui();
}