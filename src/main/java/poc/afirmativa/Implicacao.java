package poc.afirmativa;

import poc.puzzle.Puzzle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementa uma implicação lógica, para facilitar a leitura do processamento.
 * antecedente -> consequente
 *
 * Created by Thiago on 09/01/17.
 */
public class Implicacao implements Afirmativa {
    private boolean _negada;
    private Afirmativa _antecedente;
    private Afirmativa _consequente;
    private Puzzle _puzzle;

    public Implicacao(Afirmativa antecedente, Afirmativa consequente) {
        _antecedente = antecedente;
        _consequente = consequente;
    }

    @Override
    public boolean estaNegada() {
        return _negada;
    }

    @Override
    public Implicacao negar() {
        Implicacao negada = new Implicacao(_antecedente, _consequente);
        negada._puzzle = _puzzle;
        negada._negada = true;
        return negada;
    }

    @Override
    public boolean eExpansivel() {
        return true;
    }

    @Override
    public Set<Afirmativa> expandir() {
        Set<Afirmativa> expansao = new HashSet<Afirmativa>();
        if (_negada) {
            expansao.add(_antecedente);
            expansao.add(_consequente.negar());
        } else {
            expansao.add(_antecedente.negar());
            expansao.add(_consequente);
        }
        return expansao;
    }

    @Override
    public Expansao getTipoExpansao() {
        if (estaNegada()) {
            return Expansao.ALFA;
        } else {
            return Expansao.BETA;
        }
    }

    @Override
    public void associarAPuzzle(Puzzle puzzle) {
        _puzzle = puzzle;
        _antecedente.associarAPuzzle(puzzle);
        _consequente.associarAPuzzle(puzzle);
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
    public String toString() {
        String base = "(" + _antecedente + " -> " + _consequente + ")";
        if (estaNegada()) {
            return "¬" + base;
        } else {
            return base;
        }
    }
}
