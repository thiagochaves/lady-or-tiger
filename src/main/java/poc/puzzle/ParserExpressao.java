package poc.puzzle;

import poc.afirmativa.*;

public class ParserExpressao {
    
    public ParserExpressao() {
    }

    /**
     * Faz o parsing de uma sentença usando um algoritmo descendente recursivo. A
     * gramática é a seguinte: 
     * Exp = Term && Exp | Term || Exp | Term -> Exp | Term <-> Exp | Term
     * Term = !Exp | (Exp) | a(N) | em(N, N)
     * @throws ExcecaoParsing Caso o texto não seja válido.
     */
    public Afirmativa parse(String texto) throws ExcecaoParsing {
        StringBuffer frase = new StringBuffer(texto.trim());
        return exp(frase);
    }

    /**
     * Faz o parsing de uma expressão (afirmativa). O buffer enviado como
     * parâmetro é modificado.
     * @throws ExcecaoParsing Caso a expressão não seja válida.
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
        } else if (operador == '>') {
            Afirmativa termo2 = exp(texto);
            return new Implicacao(termo1, termo2);
        } else if (operador == '=') {
            Afirmativa termo2 = exp(texto);
            return new Bicondicional(termo1, termo2);
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
     * Faz o parsing de uma sub-expressão. O buffer enviado como parâmetro é
     * modificado.
     * @throws ExcecaoParsing Expressão inválida.
     */
    private Afirmativa term(StringBuffer texto) throws ExcecaoParsing {
        char inicio = next(texto);
        // ! Exp
        if (inicio == '!' || inicio == '¬') {
            Afirmativa expressao = exp(texto);
            return expressao.negar();
        }
        // ( Exp )
        else if (inicio == '(') {
            Afirmativa expressao = exp(texto);
            char fim = next(texto);
            if (fim != ')') {
            	throw new ExcecaoParsing("Expressão dentro de parênteses não foi fechada.");
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
     * Obtém o próximo caracter da seqüência (ignorando espaços), removendo-o do
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
