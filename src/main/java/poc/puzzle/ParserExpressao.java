package poc.puzzle;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Conjuncao;
import poc.afirmativa.Disjuncao;
import poc.afirmativa.Negacao;
import poc.afirmativa.Referencia;
import poc.afirmativa.Localizacao;

class ParserExpressao {
    
    public ParserExpressao() {
    }

    /**
     * Faz o parsing de uma senten�a usando um algoritmo descendente recursivo. A
     * gram�tica � a seguinte: 
     * Exp = Term && Exp | Term || Exp | Term -> Exp | Term 
     * Term = !Exp | (Exp) | a(N) | em(N, N)
     * @throws ExcecaoParsing Caso o texto n�o seja v�lido.
     */
    public Afirmativa parse(String texto) throws ExcecaoParsing {
        StringBuffer frase = new StringBuffer(texto.trim());
        return exp(frase);
    }

    /**
     * Faz o parsing de uma express�o (afirmativa). O buffer enviado como
     * par�metro � modificado.
     * @throws ExcecaoParsing Caso a express�o n�o seja v�lida.
     */
    private Afirmativa exp(StringBuffer texto) throws ExcecaoParsing {
        Afirmativa termo1 = term(texto);
        char operador = next(texto);
        // Exp ^ Term
        if (operador == '^') {
            Afirmativa termo2 = exp(texto);
            return new Conjuncao(termo1, termo2);
        }
        // Exp v Term
        else if (operador == 'v') {
            Afirmativa termo2 = exp(texto);
            return new Disjuncao(termo1, termo2);
        }
        else if (operador == '>') {
            Afirmativa termo2 = exp(texto);
            return new Disjuncao(new Negacao(termo1), termo2);
        }
        // Term
        else {
            if (operador != 0) {
                texto.insert(0, operador);
            }
            return termo1;
        }
    }

    /**
     * Faz o parsing de uma sub-express�o. O buffer enviado como par�metro �
     * modificado.
     * @throws ExcecaoParsing Express�o inv�lida.
     */
    private Afirmativa term(StringBuffer texto) throws ExcecaoParsing {
        char inicio = next(texto);
        // ! Exp
        if (inicio == '!') {
            Afirmativa expressao = exp(texto);
            return new Negacao(expressao);
        }
        // ( Exp )
        else if (inicio == '(') {
            Afirmativa expressao = exp(texto);
            char fim = next(texto);
            if (fim != ')') {
            	throw new ExcecaoParsing("Express�o dentro de par�nteses n�o foi fechada.");
            }
            return expressao;
        }
        // a ( N )
        else if (inicio == 'a') {
            next(texto);
            StringBuilder numero = new StringBuilder();
            char digito = next(texto);
            while (digito != ')') {
                numero.append(digito);
                digito = next(texto);
            }
            return new Referencia(Integer.parseInt(numero.toString()));
        }
        // em ( M, N )
        else if (inicio == 'e') {
            next(texto);
            next(texto);
            String objeto = "";
            char caractere = next(texto);
            while (caractere != ',') {
                objeto += caractere;
                caractere = next(texto);
            }
            String numero = "";
            char digito = next(texto);
            while (digito != ')') {
                numero += "" + digito;
                digito = next(texto);
            }
            int lugar = Integer.parseInt(numero);
            return new Localizacao(objeto.trim(), lugar);
        }
        throw new ExcecaoParsing("ARGH! : inicio = " + inicio + 
                ", texto = " + texto);
    }

    /**
     * Obt�m o pr�ximo caracter da seq��ncia (ignorando espa�os), removendo-o do
     * buffer.
     */
    private char next(final StringBuffer texto) {
        if (texto.length() == 0) {
            return (char) 0;
        }
        char resultado = texto.charAt(0);
        while (resultado == ' ') {
            texto.deleteCharAt(0);
            resultado = texto.charAt(0);
        }
        texto.deleteCharAt(0);
        return resultado;
    }

}
