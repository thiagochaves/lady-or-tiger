package poc.tableaux;

import poc.afirmativa.Afirmativa;

import java.util.Iterator;

class Expansor implements Runnable {
    private Ramo _ramo;
    private int _id;
    private ControladorParalelo _controlador;

    public Expansor(int id, Ramo ramo, ControladorParalelo controlador) {
        _id = id;
        _ramo = ramo;
        _controlador = controlador;
    }

    @Override
    public void run() {
        Iterator<Afirmativa> i = _ramo.expansiveis().iterator();
        while (i.hasNext()) {
            Afirmativa a = i.next();
            switch (a.getTipoExpansao()) {
                case IDENTIDADE:
                    continue;
                case ALFA:
                case GAMA:
                    for (Afirmativa nova : a.expandir()) {
                        _ramo.adicionarAfirmativa(nova);
                    }
                    i.remove();
                    break;
                case BETA:
                    Iterator<Afirmativa> iterator = a.expandir().iterator();
                    i.remove();
                    Ramo duplicado = new Ramo(_ramo);
                    _ramo.adicionarAfirmativa(iterator.next());
                    duplicado.adicionarAfirmativa(iterator.next());
                    if (duplicado.podeSerFechado()) {
                        break;
                    }
                    _controlador.ramoNovo(duplicado);
                    break;
            }
            if (_ramo.podeSerFechado()) {
                _controlador.ramoFechado(_id);
                return;
            }
        }
        _controlador.ramoAberto(_id);
    }
}
