package poc.minimize;

import java.util.Set;

import static com.google.common.base.Preconditions.*;

class SimplificadorExpressao {

    private TabelaVerdade _tabela;

    private SimplificadorExpressao(TabelaVerdade tabela) {
        _tabela = checkNotNull(tabela);
    }

    public static SimplificadorExpressao criar(TabelaVerdade tabela) {
        return new SimplificadorExpressao(tabela);
    }

    public Set<Mintermo> simplificar() {
        return null;
    }
}
