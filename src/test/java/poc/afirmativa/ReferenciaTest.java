package poc.afirmativa;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import poc.puzzle.Puzzle;

public class ReferenciaTest {
    
    private Puzzle puzzle;
    private Referencia ref;
    
    @Before
    public void setUp() {
        puzzle = mock(Puzzle.class);
        when(puzzle.getNumPortas()).thenReturn(3);
        ref = new Referencia(1);
        ref.associarAPuzzle(puzzle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenciaInvalida() {
        new Referencia(0);
    }

    @Test
    public void testEExpansivel() {
        assertTrue(ref.eExpansivel());
    }

    @Test
    public void testExpandir() {
        Afirmativa a = mock(Afirmativa.class);
        when(a.getTipoExpansao()).thenReturn(Expansao.ATOMICA);
        when(a.eExpansivel()).thenReturn(false);
        when(puzzle.getPorta(1)).thenReturn(a);
        
        Set<Afirmativa> expansao = ref.expandir();
        assertEquals(1, expansao.size());
        assertEquals(a, expansao.iterator().next());
    }

    @Test
    public void testGetTipoExpansao() {
        assertEquals(Expansao.DELTA, ref.getTipoExpansao());
    }

    @Test
    public void testResolverNegacoesDuplas() {
        assertEquals(ref, ref.resolverNegacoesDuplas());
    }

    @Test
    public void testEEssencial() {
        assertTrue(ref.eEssencial());
    }

    @Test
    public void testExplicitarObjetosQueNaoEstaoAqui() {
        assertTrue(ref.explicitarObjetosQueNaoEstaoAqui().isEmpty());
    }

}
