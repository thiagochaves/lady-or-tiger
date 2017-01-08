package poc.afirmativa;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import poc.ExcecaoImplementacao;
import poc.puzzle.Puzzle;

/**
 * É a negação de uma afirmativa lógica.
 */
public class Negacao implements Afirmativa {
	/** A afirmativa que é negada. */
	private Afirmativa _afirmativa;

	/** Nega uma afirmativa. */
	public Negacao(Afirmativa umaAfirmativa) {
	    if (umaAfirmativa == null) {
	        throw new NullPointerException();
	    }
		_afirmativa = umaAfirmativa;
	}

    @Override
    public boolean eExpansivel() {
        return getTipoExpansao() != Expansao.ATOMICA;
    }

    /**
	 * Faz a expansão de acordo com o tipo de afirmativa que é negada.
	 */
	public Set<Afirmativa> expandir() {
	    Set<Afirmativa> expansao = new HashSet<Afirmativa>();
		switch (getTipoExpansao()) {
    		case ATOMICA:
    			throw new NoSuchMethodError("Afirmativas atômicas não podem ser "
    					+ "expandidas.");
    		case GAMA:
    		    expansao.add(((Negacao) _afirmativa)._afirmativa);
    			return expansao;
    		case ALFA:
    		case BETA:
            case DELTA:
    			Set<Afirmativa> temp = _afirmativa.expandir();
    			for (Afirmativa a : temp) {
    			    expansao.add(new Negacao(a));
    			}
    			return expansao;
    		default:
    			throw new ExcecaoImplementacao("ESTADO INVÁLIDO!");
		}
	}

	public Expansao getTipoExpansao() {
		if (_afirmativa instanceof Negacao) {
			return Expansao.GAMA;
		}
		switch (_afirmativa.getTipoExpansao()) {
		    case ALFA:
		        return Expansao.BETA;
		    case BETA:
		        return Expansao.ALFA;
		    case DELTA:
		        return Expansao.DELTA;
		    case GAMA:
		        return Expansao.GAMA;
		    case ATOMICA:
		        return Expansao.ATOMICA;
		    default:
		        throw new IllegalArgumentException();
		}
	}

	@Override
    public Afirmativa resolverNegacoesDuplas() {
	    Afirmativa a = this;
	    while (a.getTipoExpansao() == Expansao.GAMA) {
	        Afirmativa segundaNegacao = ((Negacao)a)._afirmativa;
            a = ((Negacao)segundaNegacao)._afirmativa;
	    }
        return a;
    }

    /**
	 * Representação textual.
	 */
	public String toString() {
		return "¬" + _afirmativa;
	}

	/**
	 * Retorna a localização negada por este objeto se for uma localização. Caso
	 * contrário retorna null.
	 */
	public Localizacao getLocalizacao() {
		if (_afirmativa instanceof Localizacao) {
			return (Localizacao) _afirmativa;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_afirmativa == null) ? 0 : _afirmativa.hashCode());
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
		Negacao other = (Negacao) obj;
		if (_afirmativa == null) {
			if (other._afirmativa != null)
				return false;
		} else if (!_afirmativa.equals(other._afirmativa))
			return false;
		return true;
	}

    @Override
    public void associarAPuzzle(Puzzle puzzle) {
        _afirmativa.associarAPuzzle(puzzle);
    }

    @Override
    public boolean eEssencial() {
        return getTipoExpansao() == Expansao.ATOMICA 
                || getTipoExpansao() == Expansao.DELTA;
    }

    @Override
    public Collection<Negacao> explicitarObjetosQueNaoEstaoAqui() {
        return Collections.emptyList();
    }
}