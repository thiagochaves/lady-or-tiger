package poc.afirmativa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import poc.puzzle.Puzzle;

public class LocalizacaoTest {

    private static final String OBJETO = "x";
    private static final int LUGAR = 1;
    Localizacao loc;
    
    @Before
    public void setUp() {
        loc = new Localizacao(OBJETO, LUGAR);
    }

    @Test
    public void testLocalizacao() {
        assertEquals(OBJETO, loc.getObjeto());
        assertEquals(LUGAR, loc.getLugar());
    }

    @Test(expected = NullPointerException.class)
    public void testLocalizacaoSemObjeto() {
        new Localizacao(null, LUGAR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLocalizacaoLugarInvalido() {
        new Localizacao(OBJETO, 0);
    }

    @Test
    public void testEExpansivel() {
        assertFalse(loc.eExpansivel());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExpandir() {
        loc.expandir();
    }

    @Test
    public void testGetTipoExpansao() {
        assertEquals(Expansao.ATOMICA, loc.getTipoExpansao());
    }

    @Test
    public void testResolverNegacoesDuplas() {
        assertEquals(loc, loc.resolverNegacoesDuplas());
    }

    @Test
    public void testEEssencial() {
        assertTrue(loc.eEssencial());
    }

    @Test
    public void testExplicitarObjetosQueNaoEstaoAqui() {
        Puzzle p = mock(Puzzle.class);
        HashSet<String> objetos = new HashSet<String>();
        objetos.add("y");
        objetos.add("z");
        when(p.getObjetos()).thenReturn(objetos);
        loc.associarAPuzzle(p);
        Collection<Negacao> resultado = loc.explicitarObjetosQueNaoEstaoAqui();
        for (Negacao n : resultado) {
            Localizacao negada = n.getLocalizacao();
            assertTrue(objetos.contains(negada.getObjeto()));
            objetos.remove(negada.getObjeto());
        }
        assertTrue(objetos.isEmpty());
    }
}
