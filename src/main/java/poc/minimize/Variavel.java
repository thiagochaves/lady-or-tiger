package poc.minimize;

import static com.google.common.base.Preconditions.checkNotNull;

public class Variavel {
    private final String _nome;

    public Variavel(String nome) {
        _nome = checkNotNull(nome);
    }

    public String getNome() {
        return _nome;
    }

    @Override
    public String toString() {
        return getNome();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variavel variavel = (Variavel) o;

        return _nome.equals(variavel._nome);
    }

    @Override
    public int hashCode() {
        return _nome.hashCode();
    }
}
