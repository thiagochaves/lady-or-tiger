package poc.afirmativa;

import poc.puzzle.Puzzle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementa uma implicação lógica bicondicional.
 * antecedente <-> consequente
 *
 * Created by Thiago on 10/01/17.
 */
public class Bicondicional implements Afirmativa {
    private boolean _negada;
    private final Afirmativa _antecedente;
    private final Afirmativa _consequente;
    private Puzzle _puzzle;

    public Bicondicional(Afirmativa antecedente, Afirmativa consequente) {
        _antecedente = antecedente;
        _consequente = consequente;
    }

    @Override
    public boolean estaNegada() {
        return _negada;
    }

    @Override
    public Bicondicional negar() {
        Bicondicional negada = new Bicondicional(_antecedente, _consequente);
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
            // !(p ^ q) ^ !(!p ^ !q)
            // (!p v !q) ^ (p v q)
            Disjuncao positiva = new Disjuncao(_antecedente, _consequente);
            Disjuncao negativa = new Disjuncao(_antecedente.negar(), _consequente.negar());
            expansao.add(positiva);
            expansao.add(negativa);
        } else {
            // (p ^ q) v (!p ^ !q)
            Conjuncao positiva = new Conjuncao(_antecedente, _consequente);
            Conjuncao negativa = new Conjuncao(_antecedente.negar(), _consequente.negar());
            expansao.add(positiva);
            expansao.add(negativa);
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
        String base = "(" + _antecedente + " <=> " + _consequente + ")";
        if (estaNegada()) {
            return "¬" + base;
        } else {
            return base;
        }
    }
}
