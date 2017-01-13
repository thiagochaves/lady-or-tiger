package poc.afirmativa;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import poc.puzzle.Puzzle;

/**
 * Suposição em torno da localização de um objeto.
 */
public class Localizacao implements Afirmativa {
	private String _objeto;
	private int _lugar;
	private Puzzle _puzzle;
	private boolean _negada;
	
	/**
	 * 
	 * @param umObjeto
	 *            Não pode ser <code>null</code>.
	 * @param umLugar
	 *            Inteiro positivo.
	 */
	public Localizacao(String umObjeto, int umLugar) {
		if (umObjeto == null) {
			throw new IllegalArgumentException("Objeto nulo informado.");
		}
		if (umLugar <= 0) {
			throw new IllegalArgumentException(
					"Número de porta fora do intervalo:" + umLugar);
		}
		_objeto = umObjeto;
		_lugar = umLugar;
	}

	public String getObjeto() {
		return _objeto;
	}

	public int getLugar() {
		return _lugar;
	}


    @Override
    public boolean estaNegada() {
        return _negada;
    }

    @Override
    public Localizacao negar() {
        Localizacao negada = new Localizacao(_objeto, _lugar);
        negada._puzzle = _puzzle;
        negada._negada = !_negada;
        return negada;
    }

    @Override
    public boolean eExpansivel() {
        return false;
    }

    /**
	 * Uma afirmativa atômica como Localizacao não pode ser expandida.
	 * 
	 * @throws UnsupportedOperationException
	 *             Sempre.
	 */
	public Set<Afirmativa> expandir() {
		throw new UnsupportedOperationException(
				"Afirmativas atômicas não podem ser expandidas.");
	}

	/**
	 * Indica o tipo de expansão que o método expandir() fará.
	 */
	public Expansao getTipoExpansao() {
		return Expansao.IDENTIDADE;
	}

	public String toString() {
		String base = "em(" + _objeto + ", " + _lugar + ")";
        if (estaNegada()) {
            return "¬" + base;
        } else {
            return base;
        }
	}

	@Override
    public void associarAPuzzle(Puzzle puzzle) {
	    _puzzle = puzzle;
    }

    @Override
    public boolean eEssencial() {
        return true;
    }
    
    /**
     * Afirmativas que explicitam todos os objetos que não estão neste lugar.
     * Vazio se não se aplicar.
     */
    public Collection<Localizacao> explicitarObjetosQueNaoEstaoAqui() {
        assert _puzzle != null;
        if (estaNegada()) {
            return Collections.emptyList();
        }
        Set<String> objetos = new HashSet<String>(_puzzle.getObjetos());
        objetos.remove(_objeto);
        Set<Localizacao> saida = new HashSet<Localizacao>();
        for (String o : objetos) {
            Localizacao negacao = new Localizacao(o, _lugar).negar();
            negacao.associarAPuzzle(_puzzle);
            saida.add(negacao);
        }
        return saida;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Localizacao other = (Localizacao) obj;
        if (_lugar != other._lugar) {
            return false;
        }
        if (_negada != other._negada) {
            return false;
        }
        if (_objeto == null) {
            if (other._objeto != null) {
                return false;
            }
        } else if (!_objeto.equals(other._objeto)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + _lugar;
        result = prime * result + (_negada ? 1231 : 1237);
        result = prime * result + ((_objeto == null) ? 0 : _objeto.hashCode());
        return result;
    }
}