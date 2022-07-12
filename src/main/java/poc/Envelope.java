package poc;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Expansao;
import poc.afirmativa.Negacao;
import poc.afirmativa.Localizacao;

/**
 * Wrapper para afirmativas. Usado pela classe Tableaux.
 */
public class Envelope {
	/** A afirmativa envelopada. */
	private Afirmativa _afirmativa;
	/** Indica se este envelope j� foi expandido */
	private boolean _expandido = false;

	public Envelope(Afirmativa afirmativa) {
		_afirmativa = afirmativa;
	}
	
	/**
	 * Cria uma c�pia precisa do envelope informado.
	 * @param original N�o pode ser null.
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
     * Expande a afirmativa. Retorna os componentes da mesma. AVISO: S� pode ser
     * executada uma vez.
     * 
     * @return Se a expans�o for do tipo gama, retorna um elemento apenas. Se
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
	 * Obt�m a afirmativa envelopada por este objeto.
	 */
	public Afirmativa getAfirmativa() {
		return _afirmativa;
	}

	/**
     * Representa�ao textual.
     */
    public String toString() {
    	return _afirmativa.toString();
    }

    /**
	 * Indica o tipo de expans�o que o m�todo expandir() far�.
	 */
	public Expansao getTipoExpansao() {
		return _afirmativa.getTipoExpansao();
	}

	/**
	 * Retorna verdadeiro se o envelope for a nega��o do atual ou se afirma que
	 * h� mais de um objeto em um determinado lugar.
	 */
	public boolean eOposto(Envelope envelope) {
	    // FIXME mover para as afirmativas
		Afirmativa outraAfirmativa = new Negacao(envelope._afirmativa).resolverNegacoesDuplas();
		Afirmativa estaAfirmativa = _afirmativa.resolverNegacoesDuplas();

		if (estaAfirmativa.equals(outraAfirmativa)) {
			return true;
		}

		// Restri��o de unipresen�a
		// Dois ou mais objeto n�o podem estar no mesmo lugar
		// Obs: O significado da restri��o foi alterado. Verifique o relat�rio
		// do POC I para o significado anterior.
		if (_afirmativa instanceof Localizacao
				&& envelope._afirmativa instanceof Localizacao) {
			Localizacao lugar1 = (Localizacao) _afirmativa;
			Localizacao lugar2 = (Localizacao) envelope._afirmativa;
			if ((!lugar1.getObjeto().equals(lugar2.getObjeto()))
					&& (lugar1.getLugar() == lugar2.getLugar())) {
				return true;
			}
		}

		return false;
	}
	
	public boolean eEssencial() {
	    return _afirmativa.eEssencial();
	}
	
	public Collection<Negacao> explicitarObjetosQueNaoEstaoAqui() {
	    return _afirmativa.explicitarObjetosQueNaoEstaoAqui();
	}

    @Override
    /**
     * A expansibilidade n�o � considerada.
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