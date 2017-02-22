package poc.minimize;

import java.util.Collection;
import java.util.Set;

public interface TabelaVerdade {
    Set<Variavel> getVariaveis();
    boolean getValor(Collection<Atribuicao> atribuicoes);
}
