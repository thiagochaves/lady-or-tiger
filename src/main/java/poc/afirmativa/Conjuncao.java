package poc.afirmativa;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import poc.puzzle.Puzzle;

/**
 * Representa a conjunção de duas afirmativas.
 */
public class Conjuncao implements Afirmativa {
	/** Primeira afirmativa. */
	private final Afirmativa _afirmativaUm;
	/** Segunda afirmativa. */
	private final Afirmativa _afirmativaDois;

	/** Constrói uma afirmativa que é a conjunção de duas. */
	public Conjuncao(Afirmativa umaAfirmativa,
			Afirmativa outraAfirmativa) {
		_afirmativaUm = umaAfirmativa;
		_afirmativaDois = outraAfirmativa;
	}

    @Override
    public boolean eExpansivel() {
        return true;
    }

	/**
	 * Os componentes são as subafirmativas.
	 */
	public Set<Afirmativa> expandir() {
	    Set<Afirmativa> expansao = new HashSet<Afirmativa>();
	    expansao.add(_afirmativaUm);
	    expansao.add(_afirmativaDois);
		return expansao;
	}

	/**
	 * Indica o tipo de expansão que o método expandir() fará.
	 */
	public Expansao getTipoExpansao() {
		return Expansao.ALFA;
	}

	@Override
    public Afirmativa resolverNegacoesDuplas() {
        return this;
    }

    /**
	 * Representação textual.
	 */
	public String toString() {
		return "(" + _afirmativaUm + " ^ " + _afirmativaDois + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_afirmativaDois == null) ? 0 : _afirmativaDois.hashCode());
		result = prime * result
				+ ((_afirmativaUm == null) ? 0 : _afirmativaUm.hashCode());
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
		Conjuncao other = (Conjuncao) obj;
		if (_afirmativaDois == null) {
			if (other._afirmativaDois != null)
				return false;
		} else if (!_afirmativaDois.equals(other._afirmativaDois))
			return false;
		if (_afirmativaUm == null) {
			if (other._afirmativaUm != null)
				return false;
		} else if (!_afirmativaUm.equals(other._afirmativaUm))
			return false;
		return true;
	}

    @Override
    public void associarAPuzzle(Puzzle puzzle) {
        _afirmativaUm.associarAPuzzle(puzzle);
        _afirmativaDois.associarAPuzzle(puzzle);
    }

    @Override
    public boolean eEssencial() {
        return false;
    }

    @Override
    public Collection<Negacao> explicitarObjetosQueNaoEstaoAqui() {
        return Collections.emptyList();
    }
}