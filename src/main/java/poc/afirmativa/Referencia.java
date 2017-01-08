package poc.afirmativa;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import poc.puzzle.Puzzle;

/**
 * Usada pelo parser. Substitui uma refer�ncia a uma afirmativa se ela n�o
 * estiver dispon�vel. Feita para resolver o problema de recurs�o m�tua entre
 * afirmativas.
 */
public class Referencia implements Afirmativa {
	/** Puzzle a que esta afirmativa est� relacionada. */
	private Puzzle _puzzle;
	/** A que afirmativa do Puzzle esta se refere. */
	private int _indice;
    private boolean _negada;

	/**
	 * Cria uma nova refer�ncia a uma afirmativa.
	 * 
	 * @param indice Deve estar entre 1 e puzzle.getNumPortas().
	 */
	public Referencia(int indice) {
		_indice = indice;
	}

	public int getIndice() {
		return _indice;
	}


    @Override
    public boolean estaNegada() {
        return _negada;
    }

    @Override
    public Afirmativa negar() {
        Referencia negada = new Referencia(_indice);
        negada._puzzle = _puzzle;
        negada._negada = !_negada;
        return negada;
    }

    @Override
    public boolean eExpansivel() {
        return true;
    }

    /**
	 * Uma refer�ncia para uma afirmativa � expandida para o conte�do da mesma.
	 */
	public Set<Afirmativa> expandir() {
		Afirmativa resultado = _puzzle.getPorta(_indice);
		Set<Afirmativa> expansao = new HashSet<Afirmativa>();
        if (!estaNegada()) {
            expansao.add(resultado);
        } else {
            expansao.add(resultado.negar());
        }
		return expansao;
	}

	/**
	 * Indica o tipo de expans�o que o m�todo expandir() far�.
	 */
	public Expansao getTipoExpansao() {
		return Expansao.DELTA;
	}

	public String toString() {
		String base = "a(" + _indice + ")";
        if (estaNegada()) {
            return "�" + base;
        } else {
            return base;
        }
	}

	@Override
    public void associarAPuzzle(Puzzle puzzle) {
        if (puzzle == null) {
            throw new IllegalArgumentException("Puzzle nulo");
        }
        _puzzle = puzzle;
    }

    @Override
    public boolean eEssencial() {
        return true;
    }

    @Override
    public Collection<Localizacao> explicitarObjetosQueNaoEstaoAqui() {
        return Collections.emptyList();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + _indice;
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
        Referencia other = (Referencia) obj;
        if (_indice != other._indice) {
            return false;
        }
        if (_negada != other._negada) {
            return false;
        }
        if (_puzzle == null) {
            if (other._puzzle != null) {
                return false;
            }
        } else if (!_puzzle.equals(other._puzzle)) {
            return false;
        }
        return true;
    }
}