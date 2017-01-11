package poc;

import org.junit.Before;
import org.junit.Test;
import poc.afirmativa.Afirmativa;
import poc.afirmativa.Localizacao;
import poc.afirmativa.Referencia;
import poc.puzzle.ParserExpressao;
import poc.puzzle.Puzzle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Thiago on 11/01/17.
 */
public class RamoTest {
    private List<Afirmativa> axioma;
    private List<Afirmativa> portas;
    private Set<String> objetos;
    private Puzzle puzzle;

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
        Ramo novo = configurarRamo().deduzirPresenca(puzzle);
        assertTrue(novo.toString().contains("em(t, 1)"));
    }

    private Ramo configurarRamo() {
        puzzle = new Puzzle(new Referencia(1), portas, objetos);
        Ramo ramo = new Ramo(puzzle);
        for (Afirmativa af : axioma) {
            ramo.adicionarEnvelope(new Envelope(af));
        }
        return ramo;
    }

    private void configurarObjetos(String... nomes) {
        for (String nome : nomes) {
            objetos.add(nome);
        }
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
        Ramo novo = configurarRamo().deduzirPresenca(puzzle);
        assertTrue(novo.toString(), novo.toString().contains("em(p, 1)"));
    }

    @Test
    public void testarDeducaoPresencaItemPresente() {
        configurarAxioma("!em(m,1)", "em(n,1)", "!em(o,1)");
        configurarPortas(1);
        configurarObjetos("m", "n", "o", "p");
        Ramo novo = configurarRamo().deduzirPresenca(puzzle);
        assertFalse(novo.toString(), novo.toString().contains("em(p, 1)"));
    }

    @Test
    public void testarDeducaoPresencaNada() {
        configurarAxioma("!em(m,1)", "!em(n,1)", "!em(o,1)", "!em(p,1)");
        configurarPortas(1);
        configurarObjetos("m", "n", "o", "p");
        Ramo novo = configurarRamo().deduzirPresenca(puzzle);
        assertNull(novo);
    }
}
