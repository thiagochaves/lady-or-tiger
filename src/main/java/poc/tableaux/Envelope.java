package poc.tableaux;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Expansao;
import poc.afirmativa.Localizacao;

/**
 * Wrapper para afirmativas. Usado pela classe Tableaux.
 */
public class Envelope {
	/** A afirmativa envelopada. */
	private Afirmativa _afirmativa;
	/** Indica se este envelope já foi expandido */
	private boolean _expandido = false;

	public Envelope(Afirmativa afirmativa) {
		_afirmativa = afirmativa;
	}
	
	/**
	 * Cria uma cópia precisa do envelope informado.
	 * @param original Não pode ser null.
	 */
	public static Envelope criarCopia(Envelope original) {
	    Envelope copia = new Envelope(original._afirmativa);
	    copia._expandido = original._expandido;
	    return copia;
	}

	/**
	 * Indica se este envelope ainda pode ser expandido.
	 */
	public boolean eExpansivel() {
		return !_expandido && _afirmativa.eExpansivel();
	}

	/**
     * Expande a afirmativa. Retorna os componentes da mesma. AVISO: Só pode ser
     * executada uma vez.
     * 
     * @return Se a expansão for do tipo gama, retorna um elemento apenas. Se
     *         for do tipo alfa ou beta retorna dois elementos.
     */
    public Set<Afirmativa> expandir() {
        if (!eExpansivel()) {
            return Collections.emptySet();
        }
    	_expandido = true;
    	return _afirmativa.expandir();
    }

    /**
	 * Obtém a afirmativa envelopada por este objeto.
	 */
	public Afirmativa getAfirmativa() {
		return _afirmativa;
	}

	/**
     * Representaçao textual.
     */
    public String toString() {
    	return _afirmativa.toString();
    }

    /**
	 * Indica o tipo de expansão que o método expandir() fará.
	 */
	public Expansao getTipoExpansao() {
		return _afirmativa.getTipoExpansao();
	}

	public boolean eEssencial() {
	    return _afirmativa.eEssencial();
	}
	
	public Collection<Localizacao> explicitarObjetosQueNaoEstaoAqui() {
	    return _afirmativa.explicitarObjetosQueNaoEstaoAqui();
	}

    @Override
    /*
      A expansibilidade não é considerada.
     */
    public boolean equals(Object obj) {
    	if (this == obj)
    		return true;
    	if (obj == null)
    		return false;
    	if (getClass() != obj.getClass())
    		return false;
    	Envelope other = (Envelope) obj;
    	if (_afirmativa == null) {
    		if (other._afirmativa != null)
    			return false;
    	} else if (!_afirmativa.equals(other._afirmativa))
    		return false;
    	return true;
    }

    @Override
    public int hashCode() {
    	final int prime = 31;
    	int result = 1;
    	result = prime * result
    			+ ((_afirmativa == null) ? 0 : _afirmativa.hashCode());
    	return result;
    }
}