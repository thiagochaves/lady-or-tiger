package poc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import poc.afirmativa.Expansao;
import poc.afirmativa.Localizacao;
import poc.afirmativa.Negacao;
import poc.puzzle.Puzzle;

/**
 * Um ramo do tableaux.
 * 
 * @author Thiago
 */
public final class Ramo {
    private List<Envelope> _envelopes = new ArrayList<Envelope>();

    public Ramo() {
    }
    
    public Ramo(List<Envelope> envelopes) {
        if (envelopes == null) {
            throw new IllegalArgumentException();
        }
        _envelopes.addAll(envelopes);
    }
    
    /**
     * Cria um novo ramo fazendo uma cópia do conteúdo do ramo informado.
     * @param ramo Não pode ser <code>null</code>.
     */
    public Ramo(Ramo ramo) {
        for (Envelope e : ramo._envelopes) {
			adicionarEnvelope(Envelope.criarCopia(e));
        }
    }

    public int getNumEnvelopes() {
        return _envelopes.size();
    }
    
    public void adicionarEnvelope(Envelope envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException();
        }
        if (_envelopes.contains(envelope)) {
            return;
        }
        _envelopes.add(envelope);
    }
    
    public Envelope getEnvelope(int indice) {
        if (indice < 0 || indice >= getNumEnvelopes()) {
            throw new IllegalArgumentException("Índice inválido " + indice);
        }
        return _envelopes.get(indice);
    }
    
    /**
     * Procure linearmente pelo primeiro envelope expansível neste ramo.
     * @return <code>null</code> se não houver envelope expansível no 
     * ramo.
     */
    public Envelope obterPrimeiroEnvelopeExpansivel() {
        for (Envelope envelope : _envelopes) {
            if (envelope.eExpansivel()) {
                return envelope;
            }
        }
        return null;
    }
    
    /**
     * Indica se o ramo pode ser fechado, ou seja, possui uma 
     * contradição em seu conteúdo.
     * @return
     */
    public boolean podeSerFechado() {
        for (int j = 0; j < getNumEnvelopes() - 1; j++) {
            Envelope envelopeAtual = getEnvelope(j);
            for (int k = j + 1; k < getNumEnvelopes(); k++) {
                Envelope envelopeTeste = getEnvelope(k);
                if (envelopeAtual.eOposto(envelopeTeste)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Retorna um ramo que contém todos os envelopes deste ramo que
     * envolvem uma afirmativa essencial (localização ou afirmativa 
     * de porta).
     */
    public Ramo getEssenciais() {
        Ramo saida = new Ramo();
        for (Envelope envelope : _envelopes) {
            if (envelope.eEssencial()) {
                saida.adicionarEnvelope(envelope);
            }
        }
        return saida;
    }

    /**
     * Dado que uma porta precisa esconder um e apenas um objeto este método
     * analisa e infere o que pode estar atrás das portas. O resultado completo
     * (junto com dados iniciais) é retornado. Ex: Se houver !em(m,1) e os
     * únicos objetos válidos sejam m e t, então é inferido que em(t,1). OBS:
     * Sempre deve existir um objeto atrás de uma porta. Se isso não acontecer o
     * método retorna null.
     */
    @SuppressWarnings("unchecked")
	public Ramo deduzirPresenca(Puzzle puzzle) {
        // 1- Criar um container para cada porta
        ArrayList<String>[] portas = new ArrayList[puzzle.getNumPortas()];
        for (int i = 0; i < puzzle.getNumPortas(); i++) {
            portas[i] = new ArrayList<String>();
        }
        // 2- Passar por todas as verdades, se ela for uma negação de uma
        // localização então então inclua seu objeto no container respectivo
        for (Envelope atual : _envelopes) {
            if (!(atual.getAfirmativa() instanceof Negacao)) {
                continue;
            }
            if (atual.getTipoExpansao() != Expansao.ATOMICA) {
                continue;
            }
            Negacao negacao = (Negacao) atual.getAfirmativa();
            Localizacao localizacao = negacao.getLocalizacao();
            String objeto = localizacao.getObjeto();
            int lugar = localizacao.getLugar();
            if (!portas[lugar - 1].contains(objeto)) {
                portas[lugar - 1].add(objeto);
            }
        }
        // 3- Para cada porta determine os objetos que poderiam estar lá e
        // se houver apenas uma possibilidade adicione-a à verdade
        Ramo saida = new Ramo(this);
        for (int j = 0; j < portas.length; j++) {
            ArrayList<String> portaAtual = portas[j];
            HashSet<String> objetos = new HashSet<String>();
            objetos.addAll(puzzle.getObjetos());
            objetos.removeAll(portaAtual);
            if (objetos.size() == 0) {
                // Está dizendo que não há objetos atrás da porta, o que não
                // pode
                // acontecer
                return null;
            }
            if (objetos.size() == 1) {
                Localizacao localizacaoInferida = new Localizacao(
                        (String) objetos.iterator().next(), j + 1);
                localizacaoInferida.associarAPuzzle(puzzle);
                Envelope envelope = new Envelope(localizacaoInferida);
                saida.adicionarEnvelope(envelope);
            }
        }
        return saida;
    }
    
    /**
     * Calcula a interseção do ramo atual com o informado. 
     * @throws NullPointerException Se o ramo for <code>null</code>.
     */
    public Ramo calcularIntersecao(Ramo outroRamo) {
        if (outroRamo == null) {
            throw new IllegalArgumentException();
        }
        Ramo intersecao = new Ramo();
        for (Envelope env : _envelopes) {
            // Trata o caso em que há repetições
            if (intersecao.contem(env)) {
                continue;
            }
            if (outroRamo.contem(env)) {
                intersecao.adicionarEnvelope(env);
            }
        }
        return intersecao;
    }

    private boolean contem(Envelope env) {
        return _envelopes.contains(env);
    }

    @Override
    public String toString() {
        return "" + _envelopes;
    }
    
    public String toCanonicalString() {
        List<String> texto = new ArrayList<String>();
        for (Envelope e : _envelopes) {
            texto.add(e.toString());
        }
        Collections.sort(texto);
        return "" + texto;
    }

    @Override
    public int hashCode() {
        // TODO
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ramo other = (Ramo) obj;
        if (_envelopes == null) {
            if (other._envelopes != null)
                return false;
        } else if (!_envelopes.containsAll(other._envelopes)) {
            return false;
        } else if (!other._envelopes.containsAll(_envelopes)) {
            return false;
        }
        return true;
    }

    public void removerEnvelope(Envelope envelopeASerExpandido) {
        _envelopes.remove(envelopeASerExpandido);
    }

    public void adicionarEnvelopes(Ramo ramo) {
        if (ramo == null) {
            throw new IllegalArgumentException();
        }
        for (Envelope e : ramo._envelopes) {
            adicionarEnvelope(e);
        }
    }

    /**
     * Se já foi descoberto o que há atrás de uma porta, então afirmaremos
     * que os outros objetos não podem estar atrás de tal porta.
     */
    public Ramo deduzirAusencia(Puzzle puzzle) {
        Ramo saida = new Ramo(this);
        for (Envelope e : _envelopes) {
            for (Negacao n : e.explicitarObjetosQueNaoEstaoAqui()) {
                saida.adicionarEnvelope(new Envelope(n));
            }
        }
        return saida;
    }
}
