package poc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Leitor de texto que ignora as linhas que se iniciam com o caracter #,
 * considerando-as coment�rios. Para leitura use apenas o m�todo
 * {@link #readLine()}.
 * 
 * @author Thiago
 */
public class CommentLineBufferedReader extends BufferedReader {
    public CommentLineBufferedReader(Reader in) {
        super(in);
    }

    /**
     * L� uma linha do arquivo, mas ignorando aquelas que s�o coment�rios (cujo
     * primeiro caracter � #).
     */
    public String readLine() throws IOException {
        String linha = "#";
        while (linha != null && !linha.isEmpty() && linha.charAt(0) == '#') {
            linha = super.readLine();
        }
        return linha;
    }
}