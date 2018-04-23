package poc.minimize;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TabelaVerdadeContagem implements TabelaVerdade {

    private int _contagem;
    private List<Variavel> _variaveis;

    private TabelaVerdadeContagem() {}

    public static TabelaVerdadeContagem criarContagemIgual(int contagem, Set<Variavel> variaveis) {
        Preconditions.checkNotNull(variaveis);
        Preconditions.checkArgument(contagem >= 0);
        TabelaVerdadeContagem nova = new TabelaVerdadeContagem();
        nova._contagem = contagem;
        nova._variaveis = Collections.unmodifiableList(Lists.newArrayList(variaveis));
        return nova;
    }

    @Override
    public Set<Variavel> getVariaveis() {
        return Sets.newHashSet(_variaveis);
    }

    @Override
    public boolean getValor(Collection<Variavel> verdadeiras) {
        long pertinentes = verdadeiras.stream().filter(v -> _variaveis.contains(v)).count();
        return pertinentes == _contagem;
    }
}
