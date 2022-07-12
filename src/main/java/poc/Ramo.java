package poc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import poc.afirmativa.Expansao;
import poc.afirmativa.Localizacao;
import poc.afirmativa.Referencia;
import poc.puzzle.Puzzle;

/**
 * Um ramo do tableaux.
 * 
 * @author Thiago
 */
public final class Ramo {
    private List<Envelope> _envelopes = new ArrayList<Envelope>();
    private int _indicePossivelEnvelopeExpansivel;
    private Suposicao _suposicao;
    private boolean _fechado;

    private Ramo(Suposicao s) {
        _suposicao = Suposicao.criarNovaDeMesmaEstrutura(s);
    }
    
    public Ramo(Puzzle puzzle) {
        _suposicao = Suposicao.criar(puzzle.getNumPortas(), puzzle.getObjetos());
    }
    
    public Ramo(List<Envelope> envelopes, Puzzle puzzle) {
        if (envelopes == null) {
            throw new IllegalArgumentException();
        }
        _envelopes.addAll(envelopes);
        _suposicao = Suposicao.criar(puzzle.getNumPortas(), puzzle.getObjetos());
    }
    
    /**
     * Cria um novo ramo fazendo uma c�pia do conte�do do ramo informado.
     * @param ramo N�o pode ser <code>null</code>.
     */
    public Ramo(Ramo ramo) {
        _indicePossivelEnvelopeExpansivel = ramo._indicePossivelEnvelopeExpansivel;
        _suposicao = Suposicao.copiar(ramo._suposicao);
        _fechado = ramo._fechado;
        for (Envelope e : ramo._envelopes) {
            _envelopes.add(Envelope.criarCopia(e));
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
        if (_suposicao.contradiz(envelope.getAfirmativa())) {
            _fechado = true;
        }
        _envelopes.add(envelope);
        _suposicao.suporTambem(envelope.getAfirmativa());
    }
    
    public Envelope getEnvelope(int indice) {
        if (indice < 0 || indice >= getNumEnvelopes()) {
            throw new IllegalArgumentException("�ndice inv�lido " + indice);
        }
        return _envelopes.get(indice);
    }
    
    /**
     * Procure linearmente pelo primeiro envelope expans�vel neste ramo.
     * @return <code>null</code> se n�o houver envelope expans�vel no 
     * ramo.
     */
    public Envelope obterPrimeiroEnvelopeExpansivel() {
        for (int i = _indicePossivelEnvelopeExpansivel; i < _envelopes.size(); i++) {
            Envelope envelope = _envelopes.get(i);
            if (envelope.eExpansivel()) {
                _indicePossivelEnvelopeExpansivel = i;
                return envelope;
            }
        }
        _indicePossivelEnvelopeExpansivel = _envelopes.size();
        return null;
    }
    
    /**
     * Indica se o ramo pode ser fechado, ou seja, possui uma 
     * contradi��o em seu conte�do.
     * @return
     */
    public boolean podeSerFechado() {
        return _fechado;
    }
    
    /**
     * Retorna um ramo que cont�m todos os envelopes deste ramo que
     * envolvem uma afirmativa essencial (localiza��o ou afirmativa 
     * de porta).
     */
    public Ramo getEssenciais() {
        Ramo saida = new Ramo(_suposicao);
        for (Envelope envelope : _envelopes) {
            if (envelope.eEssencial()) {
                saida.adicionarEnvelope(envelope);
            }
        }
        return saida;
    }

    /**
     * Dado que uma porta precisa esconder um e apenas um objeto este m�todo
     * analisa e infere o que pode estar atr�s das portas. O resultado completo
     * (junto com dados iniciais) � retornado. Ex: Se houver !em(m,1) e os
     * �nicos objetos v�lidos sejam m e t, ent�o � inferido que em(t,1). OBS:
     * Sempre deve existir um objeto atr�s de uma porta. Se isso n�o acontecer o
     * m�todo retorna null.
     */
    @SuppressWarnings("unchecked")
	public Ramo deduzirPresenca(Puzzle puzzle) {
        // 1- Criar um container para cada porta
        ArrayList<String>[] portas = new ArrayList[puzzle.getNumPortas()];
        for (int i = 0; i < puzzle.getNumPortas(); i++) {
            portas[i] = new ArrayList<String>();
        }
        // 2- Passar por todas as verdades, se ela for uma nega��o de uma
        // localiza��o ent�o ent�o inclua seu objeto no container respectivo
        for (Envelope atual : _envelopes) {
            if (!(atual.getAfirmativa().estaNegada())) {
                continue;
            }
            if (atual.getTipoExpansao() != Expansao.IDENTIDADE) {
                continue;
            }
            Localizacao localizacao = (Localizacao) atual.getAfirmativa();
            String objeto = localizacao.getObjeto();
            int lugar = localizacao.getLugar();
            if (!portas[lugar - 1].contains(objeto)) {
                portas[lugar - 1].add(objeto);
            }
        }
        // 3- Para cada porta determine os objetos que poderiam estar l� e
        // se houver apenas uma possibilidade adicione-a � verdade
        Ramo saida = new Ramo(this);
        for (int j = 0; j < portas.length; j++) {
            ArrayList<String> portaAtual = portas[j];
            HashSet<String> objetos = new HashSet<String>();
            objetos.addAll(puzzle.getObjetos());
            objetos.removeAll(portaAtual);
            if (objetos.size() == 0) {
                // Est� dizendo que n�o h� objetos atr�s da porta, o que n�o
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
     * Calcula a interse��o do ramo atual com o informado. 
     * @throws NullPointerException Se o ramo for <code>null</code>.
     */
    public Ramo calcularIntersecao(Ramo outroRamo) {
        if (outroRamo == null) {
            throw new IllegalArgumentException();
        }
        Ramo intersecao = new Ramo(_suposicao);
        for (Envelope env : _envelopes) {
            // Trata o caso em que h� repeti��es
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
        List<Envelope> copia = new ArrayList<Envelope>(_envelopes);
        Collections.sort(copia, new Comparator<Envelope>() {
            // TODO Acertar esta bagun�a aqui
            @Override
            public int compare(Envelope o1, Envelope o2) {
                if (o1.eEssencial() && !o2.eEssencial()) {
                    return -1;
                }
                if (!o1.eEssencial() && o2.eEssencial()) {
                    return 1;
                }
                if (!o1.eEssencial() && !o2.eEssencial()) {
                    return o1.hashCode() - o2.hashCode();
                }
                if (o1.getTipoExpansao() == Expansao.DELTA && o2.getTipoExpansao() == Expansao.IDENTIDADE) {
                    return -1;
                }
                if (o2.getTipoExpansao() == Expansao.DELTA && o1.getTipoExpansao() == Expansao.IDENTIDADE) {
                    return 1;
                }
                if (o1.getTipoExpansao() == Expansao.DELTA && o2.getTipoExpansao() == Expansao.DELTA) {
                    return ((Referencia)o1.getAfirmativa()).getIndice() - ((Referencia)o2.getAfirmativa()).getIndice();
                }
                int comparacaoLugar = ((Localizacao)o1.getAfirmativa()).getLugar() - ((Localizacao)o2.getAfirmativa()).getLugar();
                if (comparacaoLugar == 0) {
                    return ((Localizacao)o1.getAfirmativa()).getObjeto().compareTo(((Localizacao)o2.getAfirmativa()).getObjeto());
                }
                return comparacaoLugar;
            }
        });
        return copia.toString();
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
     * Se j� foi descoberto o que h� atr�s de uma porta, ent�o afirmaremos
     * que os outros objetos n�o podem estar atr�s de tal porta.
     */
    public Ramo deduzirAusencia(Puzzle puzzle) {
        Ramo saida = new Ramo(this);
        for (Envelope e : _envelopes) {
            for (Localizacao n : e.explicitarObjetosQueNaoEstaoAqui()) {
                saida.adicionarEnvelope(new Envelope(n));
            }
        }
        return saida;
    }
}
