package poc.minimize;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TabelaVerdadeContagemTest {
    private TabelaVerdadeContagem _tabela;
    private Set<Variavel> _variaveis;
    private Variavel _a = new Variavel("a");
    private Variavel _b = new Variavel("b");
    private Variavel _c = new Variavel("c");
    private Variavel _d = new Variavel("d");

    @Before
    public void setUp() {
        _variaveis = Sets.newLinkedHashSet();
        _variaveis.addAll(Lists.newArrayList(_a, _b, _c));
        _tabela = TabelaVerdadeContagem.criarContagemIgual(2, _variaveis);
    }

    @Test
    public void testarContagem() {
        assertTrue(_tabela.getValor(Lists.newArrayList(_a, _c, _d)));
        assertFalse(_tabela.getValor(Lists.newArrayList(_a)));
        assertTrue(_tabela.getValor(Lists.newArrayList(_b, _c)));
    }

    @Test
    public void testarMintermos() {
        Set<Mintermo> termos = _tabela.getMintermos();
        Mintermo ab = Mintermo.criar(Lists.newArrayList(_variaveis), Lists.newArrayList(_a, _b));
        Mintermo ac = Mintermo.criar(Lists.newArrayList(_variaveis), Lists.newArrayList(_a, _c));
        Mintermo bc = Mintermo.criar(Lists.newArrayList(_variaveis), Lists.newArrayList(_b, _c));
        assertEquals(termos, Sets.newHashSet(ab, ac, bc));
    }
}
