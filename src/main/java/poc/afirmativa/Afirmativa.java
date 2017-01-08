package poc.afirmativa;

import java.util.Collection;
import java.util.Set;

import poc.puzzle.Puzzle;

/**
 * É uma afirmativa lógica.
 */
public interface Afirmativa {
    
    /**
     * Indica se a afirmativa pode ser expandida.
     */
    boolean eExpansivel();
    
    /**
	 * Expande a afirmativa. Retorna os componentes da mesma.
	 * 
	 * @return Se a expansão for do tipo gama, retorna um elemento apenas. Se
	 *         for do tipo alfa ou beta retorna dois elementos.
	 */
	Set<Afirmativa> expandir();

	/**
	 * Indica o tipo de expansão que o método expandir() fará.
	 */
	Expansao getTipoExpansao();
	
	/**
	 * Se a afirmativa é uma negação dupla, elimina as negações.
	 * O resultado nunca será uma negação dupla
	 */
	Afirmativa resolverNegacoesDuplas();
	
	/**
	 * Liga a afirmativa ao puzzle informado.
	 * Permite a referência cruzada às afirmativas das portas.
	 * @param puzzle
	 */
	void associarAPuzzle(Puzzle puzzle);
	
	boolean eEssencial();

    /**
     * Afirmativas que explicitam todos os objetos que não estão neste lugar.
     * Vazio se não se aplicar.
     */
    Collection<Negacao> explicitarObjetosQueNaoEstaoAqui();
}