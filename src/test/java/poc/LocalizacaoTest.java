package poc;

import static org.junit.Assert.*;

import org.junit.Test;

import poc.afirmativa.Expansao;
import poc.afirmativa.Localizacao;

public class LocalizacaoTest {

    @Test
    public void testLocalizacao() {
        new Localizacao("a", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLocalizacaoObjetoNulo() {
        new Localizacao(null, 1);
    }

    @Test
    public void testLocalizacaoPortaInvalida() {
        try {
            new Localizacao("a", -1);
            fail("Porta negativa aceita.");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            new Localizacao("a", 0);
            fail("Porta zero aceita.");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExpandir() {
        Localizacao loc = new Localizacao("a", 1);
        loc.expandir();
    }

    @Test
    public void testGetTipoExpansao() {
        Localizacao loc = new Localizacao("a", 1);
        assertEquals(Expansao.IDENTIDADE, loc.getTipoExpansao());
    }
}
