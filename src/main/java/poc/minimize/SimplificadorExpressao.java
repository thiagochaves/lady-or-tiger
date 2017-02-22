package poc.minimize;

import static com.google.common.base.Preconditions.*;

public class SimplificadorExpressao {
    private TabelaVerdade _tabela;

    public SimplificadorExpressao(TabelaVerdade tabela) {
        _tabela = checkNotNull(tabela);
    }
}
