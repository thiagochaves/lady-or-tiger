package poc;

import java.util.ArrayList;
import java.util.List;

import poc.afirmativa.*;
import poc.puzzle.Puzzle;

/**
 * Representa um tableaux semântico para um determinado problema.
 */
public class Solucionador extends Tableaux {

    /** Puzzle a ser resolvido. */
    private Puzzle _puzzle;

    /**
     * Cria um tableaux para resolver determinado puzzle.
     */
    public Solucionador(Puzzle puzzle) {
        _puzzle = puzzle;
        Ramo ramoInicial = new Ramo(puzzle);
        ramoInicial.adicionarEnvelope(new Envelope(_puzzle.getAxioma()));
        adicionarRamo(ramoInicial);
    }

    /**
     * Obtém a solução do puzzle, se for coerente.
     */
    public Ramo getSolucao() {
        if (eExpansivel()) {
            throw new IllegalStateException(
                    "Soluções só podem ser encontradas após "
                            + "a expansão completa do tableaux.");
        }
        fechar();
        // Realiza mais algumas inferências
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
        // Calculamos a interseção dos ramos restantes
        if (!ramos.isEmpty()) {
            return calcularIntersecao(ramos);
        } else {
            return new Ramo(_puzzle);
        }
    }

    private Ramo calcularIntersecao(List<Ramo> ramos) {
        show("Calculando interseção:");
        Ramo verdades = new Ramo(ramos.get(0).getEssenciais());
        show("Encontrado : ", verdades);
        for (int i = 0; i < ramos.size(); i++) {
            Ramo ramo = ramos.get(i).getEssenciais();
            show("Encontrado : ", ramo);
            verdades = verdades.calcularIntersecao(ramo);
            show("Interseção parcial : ", verdades);
        }
        show("Solução : ", verdades);
        return verdades;
    }

    @Override
    /**
     * A solução pode não conter a indicação
     * se uma determinada porta está dizendo a verdade ou não.
     * Inserimos então, em cada ramo, a implicância
     * (conteúdo da afirmativa da porta) <-> (porta diz a verdade).
     * Com isso instruímos o tableaux a dizer que a afirmativa da 
     * porta é verdadeira se ele for capaz de deduzir isso.
     * Funciona porque a expansão de uma porta mantém sua afirmativa
     * no ramo, que é nosso objetivo
     */
    public void expandir() {
        List<Afirmativa> deducoes = new ArrayList<Afirmativa>();
        for (int iPorta = 1; iPorta <= _puzzle.getNumPortas(); iPorta++) {
            Afirmativa afPorta = _puzzle.getPorta(iPorta);
            Bicondicional portaEstaCertaOuErrada = new Bicondicional(afPorta, new Referencia(iPorta));
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