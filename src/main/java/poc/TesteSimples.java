package poc;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import poc.puzzle.LeitorPuzzle;
import poc.puzzle.Puzzle;
import poc.tableaux.Ramo;
import poc.tableaux.TableauSerial;

public class TesteSimples {

    private static final String DIRETORIO = System.getProperty("user.dir");
    
    private static void testarConfiguracaoDoArquivo(File caminho) {
        LeitorPuzzle leitor = LeitorPuzzle.lerDoArquivo(caminho);
        Puzzle puzzle = leitor.puzzle();
        Solucionador tableaux = new Solucionador(puzzle);
        Ramo solucao = tableaux.getSolucao();
        System.out.println(solucao);
    }

    public static void main(String[] args) {
        Logger.getLogger("tableau").setLevel(Level.ALL);
        File caminho = new File(DIRETORIO, "lady1.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

}
