package poc;

import java.io.File;

import poc.puzzle.LeitorPuzzle;
import poc.puzzle.Puzzle;

public class TesteSimples {

    private static final String DIRETORIO = System.getProperty("user.dir");
    
    private static void testarConfiguracaoDoArquivo(File caminho) {
        Tableaux.setDepuracao(false);
        LeitorPuzzle leitor = LeitorPuzzle.lerDoArquivo(caminho);
        Puzzle puzzle = leitor.puzzle();
        Solucionador tableaux = new Solucionador(puzzle);
        tableaux.expandir();
        Ramo solucao = tableaux.getSolucao();
        System.out.println(solucao);
    }

    public static void main(String[] args) {
        File caminho = new File(DIRETORIO, "lady12.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

}
