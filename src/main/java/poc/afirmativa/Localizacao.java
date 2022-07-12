package poc.afirmativa;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import poc.puzzle.Puzzle;

/**
 * Suposi��o em torno da localiza��o de um objeto.
 */
public class Localizacao implements Afirmativa {
	private String _objeto;
	private int _lugar;
	private Puzzle _puzzle;
	
	/**
	 * 
	 * @param umObjeto
	 *            N�o pode ser <code>null</code>.
	 * @param umLugar
	 *            Inteiro positivo.
	 */
	public Localizacao(String umObjeto, int umLugar) {
		if (umObjeto == null) {
			throw new NullPointerException("Objeto nulo informado.");
		}
		if (umLugar <= 0) {
			throw new IllegalArgumentException(
					"N�mero de porta fora do intervalo:" + umLugar);
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
    public boolean eExpansivel() {
        return false;
    }

    /**
	 * Uma afirmativa at�mica como Localizacao n�o pode ser expandida.
	 * 
	 * @throws UnsupportedOperationException
	 *             Sempre.
	 */
	public Set<Afirmativa> expandir() {
		throw new UnsupportedOperationException(
				"Afirmativas at�micas n�o podem ser " + "expandidas.");
	}

	/**
	 * Indica o tipo de expans�o que o m�todo expandir() far�.
	 */
	public Expansao getTipoExpansao() {
		return Expansao.ATOMICA;
	}

	@Override
    public Afirmativa resolverNegacoesDuplas() {
        return this;
    }

    public String toString() {
		return "em(" + _objeto + ", " + _lugar + ")";
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
     * Afirmativas que explicitam todos os objetos que n�o est�o neste lugar.
     * Vazio se n�o se aplicar.
     */
    public Collection<Negacao> explicitarObjetosQueNaoEstaoAqui() {
        assert _puzzle != null;
        Set<String> objetos = new HashSet<String>(_puzzle.getObjetos());
        objetos.remove(_objeto);
        Set<Negacao> saida = new HashSet<Negacao>();
        for (String o : objetos) {
            Negacao negacao = new Negacao(new Localizacao(o, _lugar));
            negacao.associarAPuzzle(_puzzle);
            saida.add(negacao);
        }
        return saida;
    }

    @Override
    public boolean equals(Object obj) {
    	if (this == obj)
    		return true;
    	if (obj == null)
    		return false;
    	if (getClass() != obj.getClass())
    		return false;
    	Localizacao other = (Localizacao) obj;
    	if (_lugar != other._lugar)
    		return false;
    	if (_objeto == null) {
    		if (other._objeto != null)
    			return false;
    	} else if (!_objeto.equals(other._objeto))
    		return false;
    	return true;
    }

    @Override
    public int hashCode() {
    	final int prime = 31;
    	int result = 1;
    	result = prime * result + _lugar;
    	result = prime * result + ((_objeto == null) ? 0 : _objeto.hashCode());
    	return result;
    }
}