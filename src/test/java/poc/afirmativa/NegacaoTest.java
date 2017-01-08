package poc.afirmativa;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class NegacaoTest {

    @Test(expected = NullPointerException.class)
    public void testNegacaoDeNulo() {
        new Negacao(null);
    }

    @Test
    public void testEExpansivelLocalizacao() {
        Localizacao loc = criarLocalizacao();
        Negacao neg = new Negacao(loc);
        assertFalse(neg.eExpansivel());
    }

    private Localizacao criarLocalizacao() {
        Localizacao loc = mock(Localizacao.class);
        when(loc.getTipoExpansao()).thenReturn(Expansao.ATOMICA);
        return loc;
    }

    @Test
    public void testEExpansivelReferencia() {
        Referencia ref = criarReferencia();
        Negacao neg = new Negacao(ref);
        assertTrue(neg.eExpansivel());
    }

    private Referencia criarReferencia() {
        Referencia ref = mock(Referencia.class);
        when(ref.getTipoExpansao()).thenReturn(Expansao.DELTA);
        return ref;
    }

    @Test
    public void testEExpansivelConjuncao() {
        Conjuncao con = criarConjuncao();
        Negacao neg = new Negacao(con);
        assertTrue(neg.eExpansivel());
    }

    private Conjuncao criarConjuncao() {
        Conjuncao con = mock(Conjuncao.class);
        when(con.getTipoExpansao()).thenReturn(Expansao.ALFA);
        return con;
    }

    @Test
    public void testEExpansivelDisjuncao() {
        Disjuncao disj = criarDisjuncao();
        Negacao neg = new Negacao(disj);
        assertTrue(neg.eExpansivel());
    }

    private Disjuncao criarDisjuncao() {
        Disjuncao disj = mock(Disjuncao.class);
        when(disj.getTipoExpansao()).thenReturn(Expansao.BETA);
        return disj;
    }

    @Test
    public void testEExpansivelNegacao() {
        Negacao negacaoOriginal = criarNegacaoSimples();
        Negacao neg = new Negacao(negacaoOriginal);
        assertTrue(neg.eExpansivel());
    }

    private Negacao criarNegacaoSimples() {
        Negacao negacaoOriginal = mock(Negacao.class);
        when(negacaoOriginal.getTipoExpansao()).thenReturn(Expansao.ATOMICA);
        return negacaoOriginal;
    }

    @Test
    public void testExpandir() {
        fail("Not yet implemented");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExpandirLocalizacao() {
        Localizacao loc = criarLocalizacao();
        Negacao neg = new Negacao(loc);
        neg.expandir();
    }

    @Test
    public void testExpandirReferencia() {
        Referencia ref = criarReferencia();
        Set<Afirmativa> retornado = new HashSet<Afirmativa>();
        Afirmativa af = criarLocalizacao();
        retornado.add(af);
        when(ref.expandir()).thenReturn(retornado);
        Negacao neg = new Negacao(ref);

        Set<Afirmativa> expansao = neg.expandir();

        assertEquals(1, expansao.size());
        Afirmativa aposExpansao = expansao.iterator().next();
        assertTrue(aposExpansao instanceof Negacao);
        assertEquals(af, ((Negacao)aposExpansao).getLocalizacao());
    }

    @Test
    public void testExpandirConjuncao() {
        Conjuncao con = criarConjuncao();
        Negacao neg = new Negacao(con);
        assertEquals(Expansao.BETA, neg.getTipoExpansao());
    }

    @Test
    public void testExpandirDisjuncao() {
        Disjuncao disj = criarDisjuncao();
        Negacao neg = new Negacao(disj);
        assertEquals(Expansao.ALFA, neg.getTipoExpansao());
    }

    @Test
    public void testExpandirNegacao() {
        Negacao negacaoOriginal = criarNegacaoSimples();
        Negacao neg = new Negacao(negacaoOriginal);
        assertEquals(Expansao.GAMA, neg.getTipoExpansao());
    }

    @Test
    public void testGetTipoExpansaoLocalizacao() {
        Localizacao loc = criarLocalizacao();
        Negacao neg = new Negacao(loc);
        assertEquals(Expansao.ATOMICA, neg.getTipoExpansao());
    }

    @Test
    public void testGetTipoExpansaoReferencia() {
        Referencia ref = criarReferencia();
        Negacao neg = new Negacao(ref);
        assertEquals(Expansao.DELTA, neg.getTipoExpansao());
    }

    @Test
    public void testGetTipoExpansaoConjuncao() {
        Conjuncao con = criarConjuncao();
        Negacao neg = new Negacao(con);
        assertEquals(Expansao.BETA, neg.getTipoExpansao());
    }

    @Test
    public void testGetTipoExpansaoDisjuncao() {
        Disjuncao disj = criarDisjuncao();
        Negacao neg = new Negacao(disj);
        assertEquals(Expansao.ALFA, neg.getTipoExpansao());
    }

    @Test
    public void testGetTipoExpansaoNegacao() {
        Negacao negacaoOriginal = criarNegacaoSimples();
        Negacao neg = new Negacao(negacaoOriginal);
        assertEquals(Expansao.GAMA, neg.getTipoExpansao());
    }

    @Test
    public void testResolverNegacoesDuplas() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetLocalizacao() {
        fail("Not yet implemented");
    }

    @Test
    public void testAssociarAPuzzle() {
        fail("Not yet implemented");
    }

    @Test
    public void testEEssencial() {
        fail("Not yet implemented");
    }

    @Test
    public void testExplicitarObjetosQueNaoEstaoAqui() {
        fail("Not yet implemented");
    }

}
