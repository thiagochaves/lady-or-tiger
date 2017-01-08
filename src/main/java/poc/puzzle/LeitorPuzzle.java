package poc.puzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import poc.afirmativa.Afirmativa;
import poc.util.CommentLineBufferedReader;

/**
 * Lê um puzzle armazenado no formato padrão.
 */
public class LeitorPuzzle {
    
    private Puzzle _puzzle;
    private String _solucao;
    
    private LeitorPuzzle() {}

    /**
     * Obtém as informações para configurar um puzzle de um arquivo.
     * @param caminho Arquivo com a especificação de um puzzle no formato esperado.
     * @return nunca null.
     */
    public static LeitorPuzzle lerDoArquivo(File caminho) {
        try {
            return tentarLerDoArquivo(caminho);
        } catch (IOException e) {
            String mensagem = "Falha ao ler um puzzle em " + caminho.getAbsolutePath();
            throw new ExcecaoArquivoInvalido(mensagem, e);
        }
    }

    private static LeitorPuzzle tentarLerDoArquivo(File caminho) throws IOException {
        BufferedReader leitor = new CommentLineBufferedReader(new FileReader(caminho));
        int numPortas = lerNumeroDePortas(leitor);
        Set<String> objetos = lerObjetos(leitor);
        String textoRestricao = lerAxioma(leitor);
        List<String> textoPortas = lerAfirmativasDasPortas(leitor, numPortas);
        String resultado = leitor.readLine();
        leitor.close();
        
        ParserExpressao parser = new ParserExpressao();
        if (textoRestricao.trim().isEmpty()) {
            textoRestricao = gerarRestricaoBasica(numPortas);
        } 
        Afirmativa restricao = parser.parse(textoRestricao);
        List<Afirmativa> portas = new ArrayList<Afirmativa>();
        for (String p : textoPortas) {
            Afirmativa afirmativaPorta = parser.parse(p);
            portas.add(afirmativaPorta);
        }
        
        Puzzle puzzle = new Puzzle(restricao, portas, objetos);
        
        LeitorPuzzle lp = new LeitorPuzzle();
        lp._puzzle = puzzle;
        lp._solucao = resultado;
        return lp;
    }

    private static String gerarRestricaoBasica(int numPortas) {
        if (numPortas == 1) {
            return "a(1)v(!a(1))";
        }
        String saidaPortasMenosUm = gerarRestricaoBasica(numPortas - 1);
        String saida = "((" + saidaPortasMenosUm + ") ^ a(" + numPortas
                + ")) v ((" + saidaPortasMenosUm + ") ^ (!a(" + numPortas
                + ")))";
        return saida;
    }

    private static int lerNumeroDePortas(BufferedReader leitor)
            throws IOException {
        String linhaPortas = leitor.readLine();
        if (linhaPortas == null) {
            throw new ExcecaoArquivoInvalido("O arquivo é inválido. " 
                    + "O número de portas não foi informado.");
        }
        int numPortas = Integer.parseInt(linhaPortas);
        return numPortas;
    }
    
    private static Set<String> lerObjetos(BufferedReader leitor)
            throws IOException {
        String listaObjetos = leitor.readLine();
        if (listaObjetos == null) {
            throw new ExcecaoArquivoInvalido("O arquivo é inválido. " 
                    + "Os objetos existentes não foram informados.");
        }
        StringTokenizer tokenizer = new StringTokenizer(listaObjetos, ", ");
        Set<String> objetos = new HashSet<String>();
        while (tokenizer.hasMoreTokens()) {
            objetos.add(tokenizer.nextToken());
        }
        return objetos;
    }

    private static String lerAxioma(BufferedReader leitor) throws IOException {
        String restricao = leitor.readLine();
        if (restricao == null) {
            throw new ExcecaoArquivoInvalido("O arquivo é inválido. " 
                    + "As restrições não foram informadas.");
        }
        return restricao;
    }

    private static List<String> lerAfirmativasDasPortas(BufferedReader leitor,
            int numPortas) throws IOException {
        List<String> portas = new ArrayList<String>();
        for (int i = 0; i < numPortas; i++) {
            portas.add(leitor.readLine());
        }
        return portas;
    }

    public Puzzle puzzle() {
        return _puzzle;
    }

    public String solucao() {
        return _solucao;
    }
}
