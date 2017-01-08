package poc.afirmativa;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import poc.puzzle.Puzzle;

/**
 * Usada pelo parser. Substitui uma referência a uma afirmativa se ela não
 * estiver disponível. Feita para resolver o problema de recursão mútua entre
 * afirmativas.
 */
public class Referencia implements Afirmativa {
	/** Puzzle a que esta afirmativa está relacionada. */
	private Puzzle _puzzle;
	/** A que afirmativa do Puzzle esta se refere. */
	private int _indice;

	/**
	 * Cria uma nova referência a uma afirmativa.
	 * 
	 * @param indice Deve estar entre 1 e puzzle.getNumPortas().
	 */
	public Referencia(int indice) {
	    if (indice < 1) {
	        throw new IllegalArgumentException();
	    }
		_indice = indice;
	}

	public int getIndice() {
		return _indice;
	}


    @Override
    public boolean eExpansivel() {
        return true;
    }

    /**
	 * Uma referência para uma afirmativa é expandida para o conteúdo da mesma.
	 */
	public Set<Afirmativa> expandir() {
		Afirmativa resultado = _puzzle.getPorta(_indice);
		Set<Afirmativa> expansao = new HashSet<Afirmativa>();
		expansao.add(resultado);
		return expansao;
	}

	/**
	 * Indica o tipo de expansão que o método expandir() fará.
	 */
	public Expansao getTipoExpansao() {
		return Expansao.DELTA;
	}

	@Override
    public Afirmativa resolverNegacoesDuplas() {
        return this;
    }

    public String toString() {
		return "a(" + _indice + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _indice;
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
		Referencia other = (Referencia) obj;
		if (_indice != other._indice)
			return false;
		if (_puzzle == null) {
			if (other._puzzle != null)
				return false;
		} else if (!_puzzle.equals(other._puzzle))
			return false;
		return true;
	}

    @Override
    public void associarAPuzzle(Puzzle puzzle) {
        if (puzzle == null) {
            throw new IllegalArgumentException("Puzzle nulo");
        }
        _puzzle = puzzle;
        if (_indice > _puzzle.getNumPortas()) {
            throw new IllegalArgumentException("Número de porta " + _indice 
                    + " não permitido para o puzzle " + puzzle);
        }
    }

    @Override
    public boolean eEssencial() {
        return true;
    }

    @Override
    public Collection<Negacao> explicitarObjetosQueNaoEstaoAqui() {
        return Collections.emptyList();
    }
}