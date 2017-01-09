package poc.puzzle;

import java.util.*;

import poc.afirmativa.Afirmativa;

/**
 * Representa um problema.
 */
public class Puzzle {

	/** Restrição do problema que é sempre verdadeira. */
	private Afirmativa _axioma;
	/** Afirmativas das portas. */
	private List<Afirmativa> _portas;
	/** Objetos que podem se encontrar atrás das portas. */
	private Set<String> _objetos;

    /**
     * Cria um novo puzzle.
     * 
     * @param axioma
     *            Uma restrição que é assumida como verdadeira.
     * @param portas
     *            As afirmativas das portas (na ordem).
     * @param objetosValidos
     *            Os objetos que podem estar atrás das portas.
     * @throws ExcecaoParsing Falha ao interpretar as restrições.
     */
	public Puzzle(Afirmativa axioma, List<Afirmativa> portas, Set<String> objetosValidos) {
        _objetos = new HashSet<String>(objetosValidos);
        _axioma = axioma;
        _portas = new ArrayList<Afirmativa>();
        _portas.addAll(portas);
        associarAfirmativasAEstePuzzle();
    }

    private void associarAfirmativasAEstePuzzle() {
        _axioma.associarAPuzzle(this);
        for (Afirmativa p : _portas) {
            p.associarAPuzzle(this);
        }
    }
	
	/**
	 * Obtém o axioma.
	 */
	public Afirmativa getAxioma() {
		return _axioma;
	}

	/**
	 * Obtém o número de portas.
	 */
	public int getNumPortas() {
		return _portas.size();
	}

	/**
	 * Obtém a afirmativa de uma porta. Obs: A primeira porta tem índice 1.
	 */
	public Afirmativa getPorta(int indice) {
		return _portas.get(indice - 1);
	}

	/**
	 * Descobre se o parâmetro representa um objeto válido.
	 */
	public boolean eObjeto(String objeto) {
		return _objetos.contains(objeto);
	}

	/**
	 * Obtém um conjunto com os objetos válidos.
	 */
	public Set<String> getObjetos() {
		return Collections.unmodifiableSet(_objetos);
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((_objetos == null) ? 0 : _objetos.hashCode());
        result = prime * result + ((_portas == null) ? 0 : _portas.hashCode());
        result = prime * result
                + ((_axioma == null) ? 0 : _axioma.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Puzzle other = (Puzzle) obj;
        if (_objetos == null) {
            if (other._objetos != null)
                return false;
        } else if (!_objetos.equals(other._objetos))
            return false;
        if (_portas == null) {
            if (other._portas != null)
                return false;
        } else if (!_portas.equals(other._portas))
            return false;
        if (_axioma == null) {
            if (other._axioma != null)
                return false;
        } else if (!_axioma.equals(other._axioma))
            return false;
        return true;
    }
}