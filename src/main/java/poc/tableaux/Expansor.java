package poc.tableaux;

import poc.afirmativa.Afirmativa;

import java.util.Iterator;

class Expansor implements Runnable {
    private final Ramo _ramo;
    private final int _id;
    private final ControladorParalelo _controlador;

    public Expansor(int id, Ramo ramo, ControladorParalelo controlador) {
        _id = id;
        _ramo = ramo;
        _controlador = controlador;
    }

    @Override
    public void run() {
        Iterator<Afirmativa> i = _ramo.afirmativasParaExpansao().iterator();
        while (i.hasNext()) {
            Afirmativa a = i.next();
            switch (a.getTipoExpansao()) {
                case IDENTIDADE:
                    continue;
                case ALFA:
                case GAMA:
                    adicionarExpansaoAUmRamo(a);
                    i.remove();
                    break;
                case BETA:
                    i.remove();
                    adicionarExpansaoADoisRamos(a);
                    break;
            }
            if (_ramo.podeSerFechado()) {
                _controlador.ramoFechado(_id);
                return;
            }
        }
        _controlador.ramoAberto();
    }

    private void adicionarExpansaoAUmRamo(Afirmativa a) {
        for (Afirmativa expansao : a.expandir()) {
            _ramo.adicionarAfirmativa(expansao);
        }
    }

    private void adicionarExpansaoADoisRamos(Afirmativa a) {
        Iterator<Afirmativa> expansoes = a.expandir().iterator();
        Ramo duplicado = new Ramo(_ramo);
        _ramo.adicionarAfirmativa(expansoes.next());
        duplicado.adicionarAfirmativa(expansoes.next());
        if (!duplicado.podeSerFechado()) {
            _controlador.ramoNovo(duplicado);
        }
    }
}
