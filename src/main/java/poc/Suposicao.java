package poc;

import java.util.HashMap;
import java.util.Set;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Localizacao;
import poc.afirmativa.Referencia;

/**
 * Conjunto de suposi��es acerca de afirmativas essenciais realizadas em um ramo do tableaux.
 * O objetivo desta classe � responder rapidamente se existe uma contradi��o entre as suposi��es
 * e determinada afirmativa.
 * 
 * Usar os m�todos {@link #criar(int, Set)}, {@link #criarNovaDeMesmaEstrutura(Suposicao)} e 
 * {@link #copiar(Suposicao)} para criar objetos deste tipo.
 */
public class Suposicao {
    // Se para um �ndice i � verdadeira, ent�o _afirmativas[i] pode ser usado, c.c, n�o.
    private boolean[] _suposicoesFeitas;
    // Cada posi��o indica se sup�e-se que determinada afirmativa � verdadeira ou n�o.
    // Algumas posi��es representam afirmativas de portas (ex: a(2)) e outras
    // representam afirmativas de localiza��o (ex: em(m,1)).
    private boolean[] _afirmativas;
    private int _numLocais;
    private int _numObjetos;
    // A ordem em que os objetos devem ser levados em considera��o
    private HashMap<String, Integer> _indicesDeObjetos = new HashMap<String, Integer>();
    
    private Suposicao() {
    }
    
    private Suposicao(int numPortas, Set<String> objetos) {
        _numLocais = numPortas;
        _numObjetos = objetos.size();
        int tamanho = _numLocais * (1 + _numObjetos);
        _suposicoesFeitas = new boolean[tamanho];
        _afirmativas = new boolean[tamanho];
        int indice = 0;
        for (String o : objetos) {
            _indicesDeObjetos.put(o, indice++);
        }
    }
    
    public static Suposicao criar(int numPortas, Set<String> objetos) {
        return new Suposicao(numPortas, objetos);
    }
    
    public static Suposicao criarNovaDeMesmaEstrutura(Suposicao original) {
        return new Suposicao(original._numLocais, original._indicesDeObjetos.keySet());
    }

    public static Suposicao copiar(Suposicao original) {
        Suposicao nova = new Suposicao();
        nova._numLocais = original._numLocais;
        nova._numObjetos = original._numObjetos;
        nova._indicesDeObjetos.putAll(original._indicesDeObjetos);
        int tamanho = nova._numLocais * (1 + nova._numObjetos);
        nova._suposicoesFeitas = new boolean[tamanho];
        nova._afirmativas = new boolean[tamanho];
        System.arraycopy(original._afirmativas, 0, nova._afirmativas, 0, 
                original._afirmativas.length);
        System.arraycopy(original._suposicoesFeitas, 0, nova._suposicoesFeitas, 0, 
                original._suposicoesFeitas.length);
        return nova;
    }

    public boolean contradiz(Afirmativa a) {
        if (a.eEssencial()) {
            return negacaoJaSuposta(a) || jaTemAlgoNoMesmoLocal(a);
        } else {
            return false;
        }
    }
    
    private boolean negacaoJaSuposta(Afirmativa a) {
        int indice = obterIndice(a);
        if (a.estaNegada()) {
            return _suposicoesFeitas[indice] && _afirmativas[indice];
        } else {
            return _suposicoesFeitas[indice] && !_afirmativas[indice];
        }
    }

    private int obterIndice(Afirmativa a) {
        if (a instanceof Referencia) {
            return ((Referencia)a).getIndice() - 1;
        } else if (a instanceof Localizacao) {
            Localizacao loc = (Localizacao)a;
            int lugar = loc.getLugar();
            int iObjeto = _indicesDeObjetos.get(loc.getObjeto());
            int indice = _numLocais + (lugar - 1) * _numObjetos + iObjeto;
            assert indice < _afirmativas.length : "Indice " + indice 
                    + " passou do limite de " + _afirmativas.length;
            return indice;
        } else {
            throw new ExcecaoImplementacao("Tipo desconhecido " + a);
        }
    }

    private boolean jaTemAlgoNoMesmoLocal(Afirmativa a) {
        if (a.estaNegada()) {
            return false;
        }
        if (a instanceof Localizacao) {
            Localizacao loc = (Localizacao)a;
            int lugar = loc.getLugar();
            int indiceInicioLocal = _numLocais + (lugar - 1) * _numObjetos;
            int indiceLimiteLocal = indiceInicioLocal + _numObjetos;
            assert indiceLimiteLocal <= _afirmativas.length : "Indice " + indiceLimiteLocal 
                    + " passou do limite de " + _afirmativas.length;
            for (int i = indiceInicioLocal; i < indiceLimiteLocal; i++) {
                if (_suposicoesFeitas[i] && _afirmativas[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    public void suporTambem(Afirmativa a) {
        if (a.eEssencial()) {
            int indice = obterIndice(a);
            _suposicoesFeitas[indice] = true;
            if (!a.estaNegada()) {
                _afirmativas[indice] = true;
            }
        }
    }
    
    @Override
    public String toString() {
        String saida = "";
        for (int i = 0; i < _numLocais; i++) {
            if (_suposicoesFeitas[i]) {
                if (_afirmativas[i]) {
                    saida += "a(" + (i - 1) + ")";
                } else {
                    saida += "�a(" + (i - 1) + ")";
                }
            }
            saida += " ";
        }
        // TODO
        return saida;
    }
}
