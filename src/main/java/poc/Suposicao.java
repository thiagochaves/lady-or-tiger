package poc;

import java.util.HashMap;
import java.util.Set;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Negacao;

public class Suposicao {
    
    private boolean[] _suposicoesFeitas;
    private boolean[] _afirmativas;
    private int _numLocais;
    private int _numObjetos;
    private HashMap<String, Integer> _indicesDeObjetos = new HashMap<String, Integer>();
    
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
    
    public boolean contradiz(Afirmativa a) {
        if (!a.eEssencial()) {
            throw new IllegalArgumentException();
        }
        return negacaoJaSuposta(a) || jaTemAlgoNoMesmoLocal(a);
    }
    
    private boolean negacaoJaSuposta(Afirmativa a) {
        int indice = obterIndice(a);
        if (a instanceof Negacao) {
            return _suposicoesFeitas[indice] && _afirmativas[indice];
        } else {
            return _suposicoesFeitas[indice] && !_afirmativas[indice];
        }
    }

    private int obterIndice(Afirmativa a) {
        return 0;
        // TODO
    }

    private boolean jaTemAlgoNoMesmoLocal(Afirmativa a) {
        // TODO Auto-generated method stub
        return false;
    }

    public void suporTambem(Afirmativa a) {
        // TODO
    }
}
