package poc;

import java.util.*;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Disjuncao;
import poc.afirmativa.Negacao;
import poc.afirmativa.Referencia;
import poc.puzzle.Puzzle;

/**
 * Representa um tableaux sem�ntico para um determinado problema.
 */
public class Solucionador extends Tableaux {

    /** Puzzle a ser resolvido. */
    private Puzzle _puzzle;

    /**
     * Cria um tableaux para resolver determinado puzzle.
     */
    public Solucionador(Puzzle puzzle) {
        _puzzle = puzzle;
        Ramo ramoInicial = new Ramo();
        ramoInicial.adicionarEnvelope(new Envelope(_puzzle.getAxioma()));
        adicionarRamo(ramoInicial);
    }

    /**
     * Obt�m a solu��o do puzzle, se for coerente.
     */
    public Ramo getSolucao() {
        if (eExpansivel()) {
            throw new IllegalStateException(
                    "Solu��es s� podem ser encontradas ap�s "
                            + "a expans�o completa do tableaux.");
        }
        fechar();
        // Realiza mais algumas infer�ncias
        List<Ramo> ramos = new ArrayList<Ramo>();
        for (int i = 0; i < getNumRamos(); i++) {
            Ramo ramo = getRamo(i).getEssenciais();
            ramo = ramo.deduzirPresenca(_puzzle);
            if (ramo == null) {
                continue;
            }
            ramo = ramo.deduzirAusencia(_puzzle);
            if (ramo == null) {
                continue;
            }
            ramos.add(ramo);
        }
        // Calculamos a interse��o dos ramos restantes
        if (!ramos.isEmpty()) {
            Ramo verdades = new Ramo(ramos.get(0));
            for (int i = 0; i < ramos.size(); i++) {
                Ramo ramo = ramos.get(i).getEssenciais();
                verdades = verdades.calcularIntersecao(ramo);
            }
            show("Solu��o : " + verdades);
            return verdades;
        } else {
            return new Ramo();
        }
    }

    @Override
    /**
     * Ap�s expandir tudo, a solu��o pode n�o conter a indica��o
     * se uma determinada porta est� dizendo a verdade ou n�o.
     * Inserimos ent�o, em cada ramo, a implic�ncia
     * (conte�do da afirmativa da porta) -> (porta diz a verdade).
     * Com isso instru�mos o tableaux a dizer que a afirmativa da 
     * porta � verdadeira se ele for capaz de deduzir isso.
     * Funciona porque a expans�o de uma porta mant�m sua afirmativa
     * no ramo, que � nosso objetivo
     */
    public void expandir() {
        super.expandir();
        List<Afirmativa> deducoes = new ArrayList<Afirmativa>();
        for (int iPorta = 1; iPorta <= _puzzle.getNumPortas(); iPorta++) {
            Afirmativa afPorta = _puzzle.getPorta(iPorta);
            Disjuncao portaEstaCertaOuErrada = new Disjuncao(
                    new Negacao(afPorta), new Referencia(iPorta));
            portaEstaCertaOuErrada.associarAPuzzle(_puzzle);
            deducoes.add(portaEstaCertaOuErrada);
        }
        for (Afirmativa af : deducoes) {
            for (Ramo r : getRamos()) {
                r.adicionarEnvelope(new Envelope(af));
            }
        }
        super.expandir();
    }
}