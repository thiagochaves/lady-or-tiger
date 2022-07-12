package poc;

import java.util.*;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Expansao;

/**
 * Representa um tableaux sem�ntico gen�rico.
 */
public class Tableaux {
    /** Lista de ramos da �rvore. */
    private List<Ramo> _ramos = new ArrayList<Ramo>();
    /** Indica se as mensagens de depura��o devem ser mostradas. */
    private static boolean cdepuracao = false;

    /**
     * Construtor para as subclasses.
     */
    protected Tableaux() {
    }

    /**
     * Cria um tableaux para resolver alguma implic�ncia.
     */
    public Tableaux(Ramo axiomas) {
        _ramos.add(axiomas);
    }

    /**
     * Define se as mensagens de depura��o devem ser mostradas ou n�o.
     */
    static void setDepuracao(boolean depuracao) {
        cdepuracao = depuracao;
    }

    /**
     * Expande o tableaux ao m�ximo de forma estrita.
     */
    public void expandir() {
        show("Tableaux original : ", this);
        while (eExpansivel()) {
            expandirUmNivel();
            show("Expans�o do tableaux : ", this);
            fechar();
        }
        fechar();
        show("Tableaux sem ramos fechados : ", this);
    }
    
    /**
     * Adiciona um ramo ao tableaux.
     * @param ramo N�o pode ser <code>null</code>.
     */
    protected void adicionarRamo(Ramo ramo) {
        if (ramo == null) {
            throw new IllegalArgumentException("Ramo nulo");
        }
        _ramos.add(ramo);
    }
    
    /**
     * Remove o ramo no �ndice informado.
     */
    private void removerRamo(int indice) {
        if (indice < 0 || indice >= getNumRamos()) {
            throw new IllegalArgumentException("�ndice inv�lido " + indice);
        }
        _ramos.remove(indice);
    }

    /**
     * Obt�m um ramo do tableaux.
     */
    protected Ramo getRamo(int indice) {
        assert _ramos != null : "N�o h� ramos";
        return _ramos.get(indice);
    }

    /**
     * Obt�m a �rvore toda.
     */
    protected List<Ramo> getRamos() {
        return Collections.unmodifiableList(_ramos);
    }
    
    /**
     * @return O n�mero de ramos neste tableaux.
     */
    protected int getNumRamos() {
        return _ramos.size();
    }

    /**
     * Aplica uma regra de expans�o ao tableaux.
     */
    private void expandirUmNivel() {
        // 1. Para cada ramo ATUAL
        final int numRamos = _ramos.size();
        for (int i = 0; i < numRamos; i++) {
            Ramo ramoAtual = getRamo(i);
            Envelope envelopeASerExpandido = ramoAtual.obterPrimeiroEnvelopeExpansivel();
            if (envelopeASerExpandido == null) {
                continue;
            }
            // 1.2 Expanda-o
            Expansao tipoExpansao = envelopeASerExpandido.getTipoExpansao();
            Set<Afirmativa> expansao = envelopeASerExpandido.expandir();
            // No caso de conjun��es e disjun��es, mantemos apenas os
            // elementos separados, arrancando fora a express�o de 
            // conjun��o ou disjun��o
            if (tipoExpansao == Expansao.ALFA || 
                    tipoExpansao == Expansao.BETA ||
                    tipoExpansao == Expansao.GAMA) {
                ramoAtual.removerEnvelope(envelopeASerExpandido);
            }
            switch (tipoExpansao) {
                case ALFA:
                case GAMA:
                case DELTA:
                    // Nestes tipos de expans�es os elementos s�o adicionados ao
                    // ramo atual
                    adicionarAfirmativasRamo(expansao, i);
                    break;
                case BETA:
                    // Neste tipo de expans�o � criado um novo ramo
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
            ramoAtual.adicionarEnvelope(new Envelope(a));
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
        ramoAtual.adicionarEnvelope(new Envelope(it.next()));
        novoRamo.adicionarEnvelope(new Envelope(it.next()));
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
     * Indica se mais alguma expans�o pode ser feita ou n�o. Em caso negativo o
     * tableaux j� atingiu a atomicidade m�xima.
     */
    public boolean eExpansivel() {
        final int numRamos = getRamos().size();
        for (int i = 0; i < numRamos; i++) {
            Ramo ramoAtual = getRamo(i);
            Envelope env = ramoAtual.obterPrimeiroEnvelopeExpansivel();
            if (env != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna uma representa��o textual do tableaux.
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
     * Mostra uma mensagem de depura��o se assim for pedido.
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