package poc.tableaux;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class TableauParalelo implements Tableau, ControladorParalelo {
    private final List<Ramo> _ramos = new ArrayList<Ramo>();
    private Logger _logger = Logger.getLogger("tableau");
    private final ExecutorService _executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final AtomicInteger _numExecutores = new AtomicInteger();
    private final Semaphore _semaforo = new Semaphore(0);

    @Override
    public void expandir() {
        List<Expansor> expansores = criarExpansores();
        _numExecutores.compareAndSet(0, expansores.size());
        iniciaExpansaoParalela(expansores);
        aguardarFimExpansao();
        finalizarExecutores();
    }

    private List<Expansor> criarExpansores() {
        List<Expansor> expansores = new ArrayList<Expansor>();
        for (int i = 0; i < _ramos.size(); i++) {
            Expansor e = new Expansor(i, _ramos.get(i), this);
            expansores.add(e);
        }
        return expansores;
    }

    private void iniciaExpansaoParalela(List<Expansor> expansores) {
        for (Expansor e : expansores) {
            synchronized (_executor) {
                _executor.execute(e);
            }
        }
    }

    private void aguardarFimExpansao() {
        try {
            _semaforo.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void finalizarExecutores() {
        _executor.shutdown();
        try {
            _executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        _executor.shutdownNow();
    }

    @Override
    public void adicionarRamo(Ramo ramo) {
        if (ramo == null) {
            throw new IllegalArgumentException("Ramo nulo");
        }
        synchronized (_ramos) {
            _ramos.add(ramo);
        }
    }

    /**
     * Obtém a árvore toda.
     */
    public List<Ramo> getRamos() {
        List<Ramo> validos = new ArrayList<Ramo>();
        for (Ramo r : _ramos) {
            if (r != null) {
                validos.add(r);
            }
        }
        return validos;
    }

    /**
     * Retorna uma representação textual do tableaux.
     */
    public String toString() {
        synchronized (_ramos) {
            StringBuilder resultado = new StringBuilder();
            for (int i = 0; i < getRamos().size(); i++) {
                if (i != 0) {
                    resultado.append("\n");
                }
                resultado.append(getRamos().get(i));
            }
            return resultado.toString();
        }
    }

    @Override
    public void ramoNovo(Ramo novo) {
        Expansor e = adicionarRamoCriarExpansor(novo);
        executarNovoExpansor(e);
    }

    private Expansor adicionarRamoCriarExpansor(Ramo novo) {
        int tamanho;
        synchronized (_ramos) {
            tamanho = _ramos.size();
            adicionarRamo(novo);
        }
        return new Expansor(tamanho, novo, this);
    }

    private void executarNovoExpansor(Expansor e) {
        try {
            _numExecutores.incrementAndGet();
            synchronized (_executor) {
                _executor.execute(e);
            }
        } catch (RejectedExecutionException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void ramoFechado(int id) {
        removerRamoLogicamente(id);
        finalizarExpansaoSeForOUltimoExpansor();
    }

    private void removerRamoLogicamente(int id) {
        synchronized (_ramos) {
            _ramos.set(id, null);
        }
    }

    private void finalizarExpansaoSeForOUltimoExpansor() {
        if (_numExecutores.decrementAndGet() == 0) {
            _semaforo.release();
        }
    }

    @Override
    public void ramoAberto() {
        finalizarExpansaoSeForOUltimoExpansor();
    }
}