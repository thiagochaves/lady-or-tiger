package poc.tableaux;

import java.util.*;

import poc.afirmativa.Expansao;
import poc.afirmativa.Localizacao;
import poc.afirmativa.Referencia;
import poc.puzzle.Puzzle;

/**
 * Um ramo do tableaux.
 * 
 * @author Thiago
 */
public final class Ramo implements Iterable<Envelope> {
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

    /**
     * Cria um novo ramo fazendo uma cópia do conteúdo do ramo informado.
     * @param ramo Não pode ser <code>null</code>.
     */
    public Ramo(Ramo ramo) {
        _indicePossivelEnvelopeExpansivel = ramo._indicePossivelEnvelopeExpansivel;
        _suposicao = Suposicao.copiar(ramo._suposicao);
        _fechado = ramo._fechado;
        for (Envelope e : ramo._envelopes) {
            _envelopes.add(Envelope.criarCopia(e));
        }
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

    /**
     * Procure linearmente pelo primeiro envelope expansível neste ramo.
     * @return <code>null</code> se não houver envelope expansível no 
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
     * contradição em seu conteúdo.
     */
    public boolean podeSerFechado() {
        return _fechado;
    }
    
    /**
     * Retorna um ramo que contém todos os envelopes deste ramo que
     * envolvem uma afirmativa essencial (localização ou afirmativa 
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
     * Calcula a interseção do ramo atual com o informado. 
     * @throws NullPointerException Se o ramo for <code>null</code>.
     */
    public Ramo calcularIntersecao(Ramo outroRamo) {
        if (outroRamo == null) {
            throw new IllegalArgumentException();
        }
        Ramo intersecao = new Ramo(_suposicao);
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
        List<Envelope> copia = new ArrayList<Envelope>(_envelopes);
        Collections.sort(copia, new Comparator<Envelope>() {
            // TODO Acertar esta bagunça aqui
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
                if (o1.getAfirmativa() instanceof Referencia && o2.getAfirmativa() instanceof Localizacao) {
                    return -1;
                }
                if (o2.getAfirmativa() instanceof Referencia && o1.getAfirmativa() instanceof Localizacao) {
                    return 1;
                }
                if (o1.getAfirmativa() instanceof Referencia && o2.getAfirmativa() instanceof Referencia) {
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

    @Override
    public Iterator<Envelope> iterator() {
        return Collections.unmodifiableList(_envelopes).iterator();
    }
}
