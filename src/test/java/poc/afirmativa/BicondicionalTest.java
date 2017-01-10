package poc.afirmativa;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import poc.puzzle.Puzzle;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Thiago on 10/01/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class BicondicionalTest {

    private Bicondicional imp;
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
        imp = new Bicondicional(_antecedente, _consequente);
    }

    @Test
    public void testarNegacao() {
        assertFalse(imp.estaNegada());
        Bicondicional negada = imp.negar();
        assertTrue(negada.estaNegada());
    }

    @Test
    public void testarExpansibilidade() {
        assertTrue(imp.eExpansivel());
        assertEquals(Expansao.BETA, imp.getTipoExpansao());
        Bicondicional negada = imp.negar();
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
        boolean positivoEncontrado = false;
        boolean negativoEncontrado = false;
        for (Afirmativa atual : expansao) {
            assertEquals(Expansao.ALFA, atual.getTipoExpansao());
            Set<Afirmativa> segundaExpansao = atual.expandir();
            if (segundaExpansao.contains(_antecedente)) {
                assertTrue(segundaExpansao.contains(_consequente));
                positivoEncontrado = true;
            } else if (segundaExpansao.contains(_antecedenteNegada)) {
                assertTrue(segundaExpansao.contains(_consequenteNegada));
                negativoEncontrado = true;
            } else {
                fail("Segunda expansão inválida.");
            }
        }
        assertTrue(positivoEncontrado);
        assertTrue(negativoEncontrado);
    }

    @Test
    public void testarExpansaoNegada() {
        // !(p<->q)
        // !((p^q)v(!p^!q))
        // (!(p^q)^!(!p^!q))
        Set<Afirmativa> expansao = imp.negar().expandir();
        assertEquals(2, expansao.size());
        boolean positivoEncontrado = false;
        boolean negativoEncontrado = false;
        for (Afirmativa atual : expansao) {
            assertEquals(Expansao.BETA, atual.getTipoExpansao());
            assertFalse(atual.estaNegada());
            Set<Afirmativa> segundaExpansao = atual.expandir();
            if (segundaExpansao.contains(_antecedente)) {
                assertTrue(segundaExpansao.contains(_consequente));
                positivoEncontrado = true;
            } else if (segundaExpansao.contains(_antecedenteNegada)) {
                assertTrue(segundaExpansao.contains(_consequenteNegada));
                negativoEncontrado = true;
            } else {
                fail("Segunda expansão inválida.");
            }
        }
        assertTrue(positivoEncontrado);
        assertTrue(negativoEncontrado);
    }
}
