package poc;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import poc.afirmativa.Localizacao;
import poc.puzzle.ParserExpressao;

public class SuposicaoTest {

    @Test
    public void testContradiz() {
        Set<String> objetos = new HashSet<String>();
        objetos.add("m");
        objetos.add("t");
        Suposicao s = parse("¬a(1), a(2), ¬em(t,2), em(t,1), em(m,2)", 3, objetos);
        Localizacao ladyIsNotBehindDoorOne = new Localizacao("m", 1).negar();
        assertFalse(s.contradiz(ladyIsNotBehindDoorOne));
    }

    @Test
    public void testContradizLasy12() {
        Set<String> objetos = new HashSet<String>();
        objetos.add("m");
        objetos.add("t");
        objetos.add("n");
        Suposicao s = parse("[a(1), ¬a(2), ¬a(3), ¬a(4), ¬a(5), a(6), a(7), ¬a(8), ¬a(9), ¬em(m, 1), ¬em(m, 2), ¬em(n, 2), ¬em(m, 3), ¬em(m, 4), ¬em(m, 5), ¬em(m, 6), ¬em(m, 7), ¬em(m, 8), ¬em(t, 8)]", 9, objetos);
        Localizacao ladyIsBehindDoorNine = new Localizacao("m", 9);
        assertFalse(s.contradiz(ladyIsBehindDoorNine));
    }

    private static Suposicao parse(String ramo, int numPortas, Set<String> objetos) {
        ramo = ramo.replace("[", "");
        ramo = ramo.replace("]", "");
        Suposicao s = Suposicao.criar(numPortas, objetos);
        ParserExpressao pe = new ParserExpressao();
        for (String afirmativa : ramo.split("\\)\\s?,\\s*")) {
            s.suporTambem(pe.parse(afirmativa + ")"));
        }
        return s;
    }
}
