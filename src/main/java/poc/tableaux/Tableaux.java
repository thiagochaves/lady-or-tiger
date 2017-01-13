package poc.tableaux;

import java.util.*;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Expansao;

/**
 * Representa um tableaux semântico genérico.
 */
public class Tableaux {
    /** Lista de ramos da árvore. */
    private List<Ramo> _ramos = new ArrayList<Ramo>();
    /** Indica se as mensagens de depuração devem ser mostradas. */
    private static boolean cdepuracao = false;

    public Tableaux() {
    }

    /**
     * Define se as mensagens de depuração devem ser mostradas ou não.
     */
    public static void setDepuracao(boolean depuracao) {
        cdepuracao = depuracao;
    }

    /**
     * Expande o tableaux ao máximo de forma estrita.
     */
    public void expandir() {
        show("Tableaux original : ", this);
        while (eExpansivel()) {
            expandirUmNivel();
            show("Expansão do tableaux : ", this);
            fechar();
        }
        fechar();
        show("Tableaux sem ramos fechados : ", this);
    }
    
    /**
     * Adiciona um ramo ao tableaux.
     * @param ramo Não pode ser <code>null</code>.
     */
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
    protected Ramo getRamo(int indice) {
        assert _ramos != null : "Não há ramos";
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
    protected int getNumRamos() {
        return _ramos.size();
    }

    /**
     * Aplica uma regra de expansão ao tableaux.
     */
    private void expandirUmNivel() {
        // 1. Para cada ramo ATUAL
        final int numRamos = _ramos.size();
        for (int i = 0; i < numRamos; i++) {
            Ramo ramoAtual = getRamo(i);
            Afirmativa envelopeASerExpandido = ramoAtual.obterPrimeiroEnvelopeExpansivel();
            if (envelopeASerExpandido == null) {
                continue;
            }
            // 1.2 Expanda-o
            Expansao tipoExpansao = envelopeASerExpandido.getTipoExpansao();
            Set<Afirmativa> expansao = envelopeASerExpandido.expandir();
            ramoAtual.removerEnvelope(envelopeASerExpandido);
            switch (tipoExpansao) {
                case ALFA:
                case GAMA:
                    // Nestes tipos de expansões os elementos são adicionados ao
                    // ramo atual
                    adicionarAfirmativasRamo(expansao, i);
                    break;
                case BETA:
                    // Neste tipo de expansão é criado um novo ramo
                    bifurcarRamo(expansao, i);
            }
        }
    }

    /**
     * Adiciona uma lista de afirmativas a um ramo.
     */
    private void adicionarAfirmativasRamo(Set<Afirmativa> afirmativas,
            int indiceRamo) {
        Ramo ramoAtual = getRamo(indiceRamo);
        for(Afirmativa a : afirmativas) {
            ramoAtual.adicionarEnvelope(a);
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
        ramoAtual.adicionarEnvelope(it.next());
        novoRamo.adicionarEnvelope(it.next());
        adicionarRamo(novoRamo);
    }

    /**
     * Fecha os ramos que podem ser fechados, removendo-os.
     */
    public void fechar() {
        final int numRamos = getRamos().size();
        for (int i = numRamos - 1; i >= 0; i--) {
            Ramo ramoAtual = getRamo(i);
            if (ramoAtual.podeSerFechado()) {
                removerRamo(i);
            }
        }
    }

    /**
     * Indica se mais alguma expansão pode ser feita ou não. Em caso negativo o
     * tableaux já atingiu a atomicidade máxima.
     */
    public boolean eExpansivel() {
        final int numRamos = getRamos().size();
        for (int i = 0; i < numRamos; i++) {
            Ramo ramoAtual = getRamo(i);
            Afirmativa env = ramoAtual.obterPrimeiroEnvelopeExpansivel();
            if (env != null) {
                return true;
            }
        }
        return false;
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
    protected void show(String mensagem, Object... objetosAExibir) {
        if (cdepuracao) {
            if (objetosAExibir.length > 0) {
                System.out.println(mensagem + Arrays.asList(objetosAExibir));
            } else {
                System.out.println(mensagem);
            }
        }
    }
}