package poc.minimize;

import java.util.Collection;
import java.util.Set;

interface TabelaVerdade {
    Set<Variavel> getVariaveis();
    boolean getValor(Collection<Atribuicao> atribuicoes);
}
