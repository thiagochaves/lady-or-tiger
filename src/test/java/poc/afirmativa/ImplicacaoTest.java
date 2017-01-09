package poc.afirmativa;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import poc.puzzle.Puzzle;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Thiago on 09/01/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImplicacaoTest {

    private Implicacao imp;
    @Mock
    private Puzzle _puzzle;
    @Mock
    private Afirmativa _antecedente;
    @Mock
    private Afirmativa _consequente;
    @Mock
    private Afirmativa _antecedenteNegada;
    @Mock
    private Afirmativa _consequenteNegada;

    @Before
    public void setUp() {
        when(_antecedente.negar()).thenReturn(_antecedenteNegada);
        when(_consequente.negar()).thenReturn(_consequenteNegada);
        imp = new Implicacao(_antecedente, _consequente);
    }

    @Test
    public void testarNegacao() {
        assertFalse(imp.estaNegada());
        Implicacao negada = imp.negar();
        assertTrue(negada.estaNegada());
    }

    @Test
    public void testarExpansibilidade() {
        assertTrue(imp.eExpansivel());
        assertEquals(Expansao.BETA, imp.getTipoExpansao());
        Implicacao negada = imp.negar();
        assertEquals(Expansao.ALFA, negada.getTipoExpansao());
    }

    @Test
    public void testarEssencial() {
        assertFalse(imp.eEssencial());
    }

    @Test
    public void testarObjetosAusentes() {
        assertTrue(imp.explicitarObjetosQueNaoEstaoAqui().isEmpty());
    }

    @Test
    public void testarExpansao() {
        Set<Afirmativa> expansao = imp.expandir();
        assertEquals(2, expansao.size());
        assertTrue(expansao.contains(_antecedenteNegada));
        assertTrue(expansao.contains(_consequente));
    }

    @Test
    public void testarExpansaoNegada() {
        // !(p->q)
        // !(!pvq)
        // (p^!q)
        Set<Afirmativa> expansao = imp.negar().expandir();
        assertEquals(2, expansao.size());
        assertTrue(expansao.contains(_antecedente));
        assertTrue(expansao.contains(_consequenteNegada));
    }
}
