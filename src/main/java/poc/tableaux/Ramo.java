package poc.tableaux;

import java.util.*;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Expansao;
import poc.afirmativa.Localizacao;
import poc.afirmativa.Referencia;
import poc.puzzle.Puzzle;

/**
 * Um ramo do tableaux.
 * 
 * @author Thiago
 */
public final class Ramo implements Iterable<Afirmativa> {
    private List<Afirmativa> _envelopes = new ArrayList<Afirmativa>();
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
        for (Afirmativa e : ramo._envelopes) {
            _envelopes.add(e);
        }
    }

    public void adicionarEnvelope(Afirmativa envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException();
        }
        if (_envelopes.contains(envelope)) {
            return;
        }
        if (_suposicao.contradiz(envelope)) {
            _fechado = true;
        }
        _envelopes.add(envelope);
        _suposicao.suporTambem(envelope);
    }

    /**
     * Procure linearmente pelo primeiro envelope expansível neste ramo.
     * @return <code>null</code> se não houver envelope expansível no 
     * ramo.
     */
    public Afirmativa obterPrimeiroEnvelopeExpansivel() {
        for (int i = _indicePossivelEnvelopeExpansivel; i < _envelopes.size(); i++) {
            Afirmativa envelope = _envelopes.get(i);
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
        for (Afirmativa envelope : _envelopes) {
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
        for (Afirmativa env : _envelopes) {
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

    private boolean contem(Afirmativa env) {
        return _envelopes.contains(env);
    }

    @Override
    public String toString() {
        List<Afirmativa> copia = new ArrayList<Afirmativa>(_envelopes);
        Collections.sort(copia, new Comparator<Afirmativa>() {
            // TODO Acertar esta bagunça aqui
            @Override
            public int compare(Afirmativa o1, Afirmativa o2) {
                if (o1.eEssencial() && !o2.eEssencial()) {
                    return -1;
                }
                if (!o1.eEssencial() && o2.eEssencial()) {
                    return 1;
                }
                if (!o1.eEssencial() && !o2.eEssencial()) {
                    return o1.hashCode() - o2.hashCode();
                }
                if (o1 instanceof Referencia && o2 instanceof Localizacao) {
                    return -1;
                }
                if (o2 instanceof Referencia && o1 instanceof Localizacao) {
                    return 1;
                }
                if (o1 instanceof Referencia && o2 instanceof Referencia) {
                    return ((Referencia)o1).getIndice() - ((Referencia)o2).getIndice();
                }
                int comparacaoLugar = ((Localizacao)o1).getLugar() - ((Localizacao)o2).getLugar();
                if (comparacaoLugar == 0) {
                    return ((Localizacao)o1).getObjeto().compareTo(((Localizacao)o2).getObjeto());
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

    public void removerEnvelope(Afirmativa envelopeASerExpandido) {
        _envelopes.remove(envelopeASerExpandido);
    }

    @Override
    public Iterator<Afirmativa> iterator() {
        return Collections.unmodifiableList(_envelopes).iterator();
    }
}
