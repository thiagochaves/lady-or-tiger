package poc;

import java.io.File;
import java.util.logging.*;

import poc.puzzle.LeitorPuzzle;
import poc.puzzle.Puzzle;
import poc.tableaux.Ramo;
import poc.tableaux.TableauSerial;

public class TesteSimples {

    private static final String DIRETORIO = System.getProperty("user.dir") + File.separator + "puzzles";
    
    private static void testarConfiguracaoDoArquivo(File caminho) {
        LeitorPuzzle leitor = LeitorPuzzle.lerDoArquivo(caminho);
        Puzzle puzzle = leitor.puzzle();
        Solucionador tableaux = new Solucionador(puzzle);
        Ramo solucao = tableaux.getSolucao();
        System.out.println(solucao);
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("tableau");
        logger.setLevel(Level.FINE);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        logger.addHandler(handler);
        File caminho = new File(DIRETORIO, "lady12.txt");
        testarConfiguracaoDoArquivo(caminho);
    }

}
