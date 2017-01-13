package poc;

import org.junit.Before;
import org.junit.Test;
import poc.afirmativa.Afirmativa;
import poc.afirmativa.Referencia;
import poc.puzzle.ParserExpressao;
import poc.puzzle.Puzzle;
import poc.tableaux.Ramo;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SolucionadorPresencaTest {
    private List<Afirmativa> axioma;
    private List<Afirmativa> portas;
    private Set<String> objetos;
    private Puzzle puzzle;
    private Solucionador solucionador;

    @Before
    public void setUp() {
        objetos = new HashSet<String>();
        portas = new ArrayList<Afirmativa>();
        axioma = new ArrayList<Afirmativa>();
        puzzle = null;
    }

    @Test
    public void testarDeducaoPresenca() {
        configurarAxioma("!em(m,1)");
        configurarPortas(1);
        configurarObjetos("m", "t");
        Ramo ramo = configurarRamo();
        Ramo novo = solucionador.deduzirPresenca(ramo);
        assertTrue(novo.toString().contains("em(t, 1)"));
    }

    private Ramo configurarRamo() {
        puzzle = new Puzzle(new Referencia(1), portas, objetos);
        solucionador = new Solucionador(puzzle);
        Ramo ramo = new Ramo(puzzle);
        for (Afirmativa af : axioma) {
            ramo.adicionarAfirmativa(af);
        }
        return ramo;
    }

    private void configurarObjetos(String... nomes) {
        Collections.addAll(objetos, nomes);
    }

    private void configurarPortas(int numPortas) {
        for (int i = 0; i < numPortas; i++) {
            portas.add(new ParserExpressao().parse("a(1)"));
        }
    }

    private void configurarAxioma(String... textoAxioma) {
        for (String texto : textoAxioma) {
            axioma.add(new ParserExpressao().parse(texto));
        }
    }

    @Test
    public void testarDeducaoPresenca4Itens() {
        configurarAxioma("!em(m,1)", "!em(n,1)", "!em(o,1)");
        configurarPortas(1);
        configurarObjetos("m", "n", "o", "p");
        Ramo ramo = configurarRamo();
        Ramo novo = solucionador.deduzirPresenca(ramo);
        assertTrue(novo.toString(), novo.toString().contains("em(p, 1)"));
    }

    @Test
    public void testarDeducaoPresencaItemPresente() {
        configurarAxioma("!em(m,1)", "em(n,1)", "!em(o,1)");
        configurarPortas(1);
        configurarObjetos("m", "n", "o", "p");
        Ramo ramo = configurarRamo();
        Ramo novo = solucionador.deduzirPresenca(ramo);
        assertFalse(novo.toString(), novo.toString().contains("em(p, 1)"));
    }

    @Test
    public void testarDeducaoPresencaNada() {
        configurarAxioma("!em(m,1)", "!em(n,1)", "!em(o,1)", "!em(p,1)");
        configurarPortas(1);
        configurarObjetos("m", "n", "o", "p");
        Ramo ramo = configurarRamo();
        Ramo novo = solucionador.deduzirPresenca(ramo);
        assertNull(novo);
    }
}
