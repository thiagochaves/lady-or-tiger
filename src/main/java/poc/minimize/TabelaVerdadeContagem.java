package poc.minimize;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;

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

    @Override
    public Set<Mintermo> getMintermos() {
        Set<Mintermo> todos = getMintermos(_variaveis.get(0), _variaveis.subList(1, _variaveis.size()));
        for (Iterator<Mintermo> i = todos.iterator(); i.hasNext();) {
            Mintermo termo = i.next();
            if (termo.getVariaveis().size() != _contagem) {
                i.remove();
            }
        }
        return todos;
    }

    private Set<Mintermo> getMintermos(Variavel variavel, List<Variavel> variaveis) {
        if (variaveis.isEmpty()) {
            Mintermo com = Mintermo.criar(_variaveis, Lists.newArrayList(variavel));
            Mintermo sem = Mintermo.criar(_variaveis, Lists.newArrayList());
            return Sets.newHashSet(com, sem);
        }
        Set<Mintermo> seguintes = getMintermos(variaveis.get(0), variaveis.subList(1, variaveis.size()));
        Set<Mintermo> saida = Sets.newHashSet();
        saida.addAll(seguintes);
        seguintes.forEach(termo -> saida.add(termo.adicionarVariavel(variavel)));
        return saida;
    }
}
