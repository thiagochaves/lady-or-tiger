package poc.afirmativa;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import poc.puzzle.Puzzle;

/**
 * Usada pelo parser. Substitui uma referência a uma afirmativa se ela não estiver disponível. Feita
 * para resolver o problema de recursão mútua entre afirmativas.
 */
public class Referencia implements Afirmativa {
    /** Puzzle a que esta afirmativa está relacionada. */
    private Puzzle _puzzle;
    /** A que afirmativa do Puzzle esta se refere. */
    private final int _indice;
    private boolean _negada;

    /**
     * Cria uma nova referência a uma afirmativa.
     * 
     * @param indice
     *            Deve estar entre 1 e puzzle.getNumPortas().
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
        return false;
    }

    public Set<Afirmativa> expandir() {
        throw new UnsupportedOperationException(
                "Afirmativas atômicas não podem ser expandidas.");
    }

    public Expansao getTipoExpansao() {
        return Expansao.IDENTIDADE;
    }

    public String toString() {
        String base = "a(" + _indice + ")";
        if (estaNegada()) {
            return "¬" + base;
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
            return other._puzzle == null;
        } else
            return _puzzle.equals(other._puzzle);
    }
}