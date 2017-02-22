package poc.minimize;

import static com.google.common.base.Preconditions.*;

public class Atribuicao {
    private Variavel _x;
    private boolean _valor;

    public Atribuicao(Variavel x, boolean valor) {
        _x = checkNotNull(x);
        _valor = valor;
    }

    public Variavel getVariavel() {
        return _x;
    }

    public boolean getValor() {
        return _valor;
    }

    @Override
    public String toString() {
        return "[" + _x + "/" + ((_valor) ? "1" : "0") + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Atribuicao that = (Atribuicao) o;

        if (_valor != that._valor) return false;
        return _x.equals(that._x);
    }

    @Override
    public int hashCode() {
        int result = _x.hashCode();
        result = 31 * result + (_valor ? 1 : 0);
        return result;
    }
}
