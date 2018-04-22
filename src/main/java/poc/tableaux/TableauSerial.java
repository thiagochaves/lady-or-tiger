package poc.tableaux;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Expansao;

/**
 * Representa um tableaux semântico genérico.
 */
public class TableauSerial implements Tableau {
    /** Lista de ramos da árvore. */
    private final List<Ramo> _ramos = new ArrayList<Ramo>();
    private final Logger _logger = Logger.getLogger("tableau");

    public TableauSerial() {
    }

    @Override
    public void expandir() {
        show("TableauSerial original : ", this);
        while (expandirUmNivel()) {
            show("Expansão do tableaux : ", this);
            fechar();
        }
        show("TableauSerial sem ramos fechados : ", this);
    }
    
    @Override
    public void adicionarRamo(Ramo ramo) {
        if (ramo == null) {
            throw new IllegalArgumentException("Ramo nulo");
        }
        _ramos.add(ramo);
    }
    
    /**
     * Remove o ramo no índice informado.
     */
    private void removerRamo(int indice) {
        if (indice < 0 || indice >= getNumRamos()) {
            throw new IllegalArgumentException("Índice inválido " + indice);
        }
        _ramos.remove(indice);
    }

    /**
     * Obtém um ramo do tableaux.
     */
    private Ramo getRamo(int indice) {
        return _ramos.get(indice);
    }

    /**
     * Obtém a árvore toda.
     */
    public List<Ramo> getRamos() {
        return Collections.unmodifiableList(_ramos);
    }
    
    /**
     * @return O número de ramos neste tableaux.
     */
    private int getNumRamos() {
        return _ramos.size();
    }

    /**
     * Aplica uma regra de expansão ao tableaux.
     */
    private boolean expandirUmNivel() {
        boolean expandiu = false;
        // 1. Para cada ramo ATUAL
        final int numRamos = _ramos.size();
        for (int i = 0; i < numRamos; i++) {
            Ramo ramoAtual = getRamo(i);
            Afirmativa afirmativaAExpandir = null;
            for (Afirmativa a : ramoAtual) {
                if (a.eExpansivel()) {
                    afirmativaAExpandir = a;
                    break;
                }
            }
            if (afirmativaAExpandir == null) {
                continue;
            }
            // 1.2 Expanda-o
            Expansao tipoExpansao = afirmativaAExpandir.getTipoExpansao();
            Set<Afirmativa> expansao = afirmativaAExpandir.expandir();
            ramoAtual.removerAfirmativa(afirmativaAExpandir);
            switch (tipoExpansao) {
                case ALFA:
                case GAMA:
                    // Nestes tipos de expansões os elementos são adicionados ao
                    // ramo atual
                    adicionarAfirmativasRamo(expansao, i);
                    expandiu = true;
                    break;
                case BETA:
                    // Neste tipo de expansão é criado um novo ramo
                    bifurcarRamo(expansao, i);
                    expandiu = true;
            }
        }
        return expandiu;
    }

    /**
     * Adiciona uma lista de afirmativas a um ramo.
     */
    private void adicionarAfirmativasRamo(Set<Afirmativa> afirmativas,
            int indiceRamo) {
        Ramo ramoAtual = getRamo(indiceRamo);
        for(Afirmativa a : afirmativas) {
            ramoAtual.adicionarAfirmativa(a);
        }
    }

    /**
     * Bifurca um ramo, dando-lhe dois filhos.
     * 
     * @param afirmativas
     *            Lista de 2 afirmativas.
     * @param indiceRamo
     *            Ramo a ser bifurcado.
     */
    private void bifurcarRamo(Set<Afirmativa> afirmativas, int indiceRamo) {
        assert afirmativas.size() == 2 : afirmativas.toString();
        Ramo ramoAtual = getRamo(indiceRamo);
        Ramo novoRamo = new Ramo(ramoAtual);
        Iterator<Afirmativa> it = afirmativas.iterator();
        ramoAtual.adicionarAfirmativa(it.next());
        novoRamo.adicionarAfirmativa(it.next());
        adicionarRamo(novoRamo);
    }

    private void fechar() {
        final int numRamos = getRamos().size();
        for (int i = numRamos - 1; i >= 0; i--) {
            Ramo ramoAtual = getRamo(i);
            if (ramoAtual.podeSerFechado()) {
                removerRamo(i);
            }
        }
    }

    /**
     * Retorna uma representação textual do tableaux.
     */
    public String toString() {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < getRamos().size(); i++) {
            if (i != 0) {
                resultado.append("\n");
            }
            resultado.append(getRamos().get(i));
        }
        return resultado.toString();
    }

    /**
     * Mostra uma mensagem de depuração se assim for pedido.
     */
    private void show(String mensagem, Object... objetosAExibir) {
        _logger.log(Level.FINE, mensagem, objetosAExibir);
    }
}