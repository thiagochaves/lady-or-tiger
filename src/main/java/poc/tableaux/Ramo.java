package poc.tableaux;

import java.util.*;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Localizacao;
import poc.afirmativa.Referencia;
import poc.puzzle.Puzzle;

/**
 * Um ramo do tableaux.
 * 
 * @author Thiago
 */
@SuppressWarnings("ConstantConditions")
public final class Ramo implements Iterable<Afirmativa> {
    private final List<Afirmativa> _afirmativas = new ArrayList<Afirmativa>();
    private final Suposicao _suposicao;
    private boolean _fechado;

    private Ramo(Suposicao s) {
        _suposicao = Suposicao.criarNovaDeMesmaEstrutura(s);
    }

    public Ramo(Puzzle puzzle) {
        _suposicao = Suposicao.criar(puzzle.getNumPortas(), puzzle.getObjetos());
    }

    /**
     * Cria um novo ramo fazendo uma cópia do conteúdo do ramo informado.
     * 
     * @param ramo
     *            Não pode ser <code>null</code>.
     */
    public Ramo(Ramo ramo) {
        _suposicao = Suposicao.copiar(ramo._suposicao);
        _fechado = ramo._fechado;
        _afirmativas.addAll(ramo._afirmativas);
    }

    public void adicionarAfirmativa(Afirmativa afirmativa) {
        if (afirmativa == null) {
            throw new IllegalArgumentException();
        }
        if (_afirmativas.contains(afirmativa)) {
            return;
        }
        if (_suposicao.contradiz(afirmativa)) {
            _fechado = true;
        }
        _afirmativas.add(afirmativa);
        _suposicao.suporTambem(afirmativa);
    }

    /**
     * Indica se o ramo pode ser fechado, ou seja, possui uma contradição em seu conteúdo.
     */
    public boolean podeSerFechado() {
        return _fechado;
    }

    /**
     * Retorna um ramo que contém todas as afirmativas essenciais deste ramo (localização ou
     * afirmativa de porta).
     */
    public Ramo getEssenciais() {
        Ramo saida = new Ramo(_suposicao);
        for (Afirmativa afirmativa : _afirmativas) {
            if (afirmativa.eEssencial()) {
                saida.adicionarAfirmativa(afirmativa);
            }
        }
        return saida;
    }

    /**
     * Calcula a interseção do ramo atual com o informado.
     * 
     * @throws NullPointerException
     *             Se o ramo for <code>null</code>.
     */
    public Ramo calcularIntersecao(Ramo outroRamo) {
        if (outroRamo == null) {
            throw new IllegalArgumentException();
        }
        Ramo intersecao = new Ramo(_suposicao);
        for (Afirmativa afirmativa : _afirmativas) {
            // Trata o caso em que há repetições
            if (intersecao.contem(afirmativa)) {
                continue;
            }
            if (outroRamo.contem(afirmativa)) {
                intersecao.adicionarAfirmativa(afirmativa);
            }
        }
        return intersecao;
    }

    private boolean contem(Afirmativa afirmativa) {
        return _afirmativas.contains(afirmativa);
    }

    public String toStringPuro() {
        return _afirmativas.toString();
    }

    @Override
    public String toString() {
        List<Afirmativa> copia = new ArrayList<Afirmativa>(_afirmativas);
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
                    return ((Referencia) o1).getIndice() - ((Referencia) o2).getIndice();
                }
                int comparacaoLugar =
                        ((Localizacao) o1).getLugar() - ((Localizacao) o2).getLugar();
                if (comparacaoLugar == 0) {
                    return ((Localizacao) o1).getObjeto()
                            .compareTo(((Localizacao) o2).getObjeto());
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
        if (!_afirmativas.containsAll(other._afirmativas)) {
            return false;
        } else
            return other._afirmativas.containsAll(_afirmativas);
    }

    public void removerAfirmativa(Afirmativa afirmativa) {
        _afirmativas.remove(afirmativa);
    }

    @Override
    public Iterator<Afirmativa> iterator() {
        return Collections.unmodifiableList(_afirmativas).iterator();
    }

    public Iterable<Afirmativa> afirmativasParaExpansao() {
        return new IteravelAfirmativasParaExpansao();
    }

    private class IteravelAfirmativasParaExpansao implements Iterable<Afirmativa> {
        @Override
        public Iterator<Afirmativa> iterator() {
            return new IteradorAfirmativasParaExpansao();
        }
    }

    private class IteradorAfirmativasParaExpansao implements Iterator<Afirmativa> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < _afirmativas.size();
        }

        @Override
        public Afirmativa next() {
            return _afirmativas.get(i++);
        }

        @Override
        public void remove() {
            int previous = i - 1;
            _afirmativas.remove(previous);
            i--;
        }
    }
}
