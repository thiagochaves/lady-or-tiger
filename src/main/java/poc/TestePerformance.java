package poc;

import java.io.File;

import poc.puzzle.LeitorPuzzle;
import poc.puzzle.Puzzle;

public class TestePerformance {

    private static final String DIRETORIO = System.getProperty("user.dir");
    
    private static double testarConfiguracaoDoArquivo(File caminho) {
        LeitorPuzzle leitor = LeitorPuzzle.lerDoArquivo(caminho);
        Puzzle puzzle = leitor.puzzle();
        long t1 = System.currentTimeMillis();
        Solucionador tableaux = new Solucionador(puzzle);
        tableaux.expandir();
        tableaux.getSolucao();
        long t2 = System.currentTimeMillis();
        return (t2 - t1) / 1000.0;
    }

    public static void main(String[] args) {
        File caminho = new File(DIRETORIO, "lady11.txt");
        double total = 0.0;
        for (int i = 0; i < 100; i++) {
            total += testarConfiguracaoDoArquivo(caminho);
        }
        double media = total / 100;
        System.out.println("Tempo médio: " + media + "s");
    }

}
