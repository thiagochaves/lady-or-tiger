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
    private boolean _negada;

	/** Constrói uma afirmativa que é a conjunção de duas. */
	public Conjuncao(Afirmativa umaAfirmativa,
			Afirmativa outraAfirmativa) {
		_afirmativaUm = umaAfirmativa;
		_afirmativaDois = outraAfirmativa;
	}

    @Override
    public boolean estaNegada() {
        return _negada;
    }

    @Override
    public Conjuncao negar() {
        Conjuncao negada = new Conjuncao(_afirmativaUm, _afirmativaDois);
        negada._negada = !_negada;
        return negada;
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
        if (!estaNegada()) {
            expansao.add(_afirmativaUm);
            expansao.add(_afirmativaDois);
        } else {
            expansao.add(_afirmativaUm.negar());
            expansao.add(_afirmativaDois.negar());
        }
		return expansao;
	}

	/**
	 * Indica o tipo de expansão que o método expandir() fará.
	 */
	public Expansao getTipoExpansao() {
	    if (!estaNegada()) {
	        return Expansao.ALFA;
	    } else {
	        return Expansao.BETA;
	    }
	}

	/**
	 * Representação textual.
	 */
	public String toString() {
		String base = "(" + _afirmativaUm + " ^ " + _afirmativaDois + ")";
		if (estaNegada()) {
		    return "¬" + base;
		} else {
		    return base;
		}
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
    public Collection<Localizacao> explicitarObjetosQueNaoEstaoAqui() {
        return Collections.emptyList();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((_afirmativaDois == null) ? 0 : _afirmativaDois.hashCode());
        result = prime * result
                + ((_afirmativaUm == null) ? 0 : _afirmativaUm.hashCode());
        result = prime * result + (_negada ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Conjuncao other = (Conjuncao) obj;
        if (_afirmativaDois == null) {
            if (other._afirmativaDois != null) {
                return false;
            }
        } else if (!_afirmativaDois.equals(other._afirmativaDois)) {
            return false;
        }
        if (_afirmativaUm == null) {
            if (other._afirmativaUm != null) {
                return false;
            }
        } else if (!_afirmativaUm.equals(other._afirmativaUm)) {
            return false;
        }
        return _negada == other._negada;
    }
}