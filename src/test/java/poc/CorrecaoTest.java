package poc;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import poc.puzzle.LeitorPuzzle;
import poc.puzzle.Puzzle;
import poc.tableaux.Ramo;

public class CorrecaoTest {

    private static final String DIRETORIO = System.getProperty("user.dir");
    
    @Test
    public void test01() {
        File caminho = new File(DIRETORIO, "lady1.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    private void testarConfiguracaoDoArquivo(File caminho) {
        LeitorPuzzle leitor = LeitorPuzzle.lerDoArquivo(caminho);
        Puzzle puzzle = leitor.puzzle();
        Solucionador tableaux = new Solucionador(puzzle);
        Ramo solucao = tableaux.getSolucao();
        assertEquals(leitor.solucao(), solucao.toString());
    }

    @Test
    public void test02() {
        File caminho = new File(DIRETORIO, "lady2.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test03() {
        File caminho = new File(DIRETORIO, "lady3.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test04() {
        File caminho = new File(DIRETORIO, "lady4.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test05() {
        File caminho = new File(DIRETORIO, "lady5.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test06() {
        File caminho = new File(DIRETORIO, "lady6.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test07() {
        File caminho = new File(DIRETORIO, "lady7.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test08() {
        File caminho = new File(DIRETORIO, "lady8.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test09() {
        File caminho = new File(DIRETORIO, "lady9.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test10() {
        File caminho = new File(DIRETORIO, "lady10.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test11() {
        File caminho = new File(DIRETORIO, "lady11.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

    @Test
    public void test12() {
        File caminho = new File(DIRETORIO, "lady12.txt");
        testarConfiguracaoDoArquivo(caminho);
    }
}
