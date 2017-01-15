package poc.tableaux;

import poc.afirmativa.Afirmativa;
import poc.afirmativa.Expansao;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableauParalelo implements Tableau, ControladorParalelo {
    private List<Ramo> _ramos = new ArrayList<Ramo>();
    private Logger _logger = Logger.getLogger("tableau");
    private ExecutorService _executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private AtomicInteger _numExecutores = new AtomicInteger();
    private Semaphore _semaforo = new Semaphore(0);

    @Override
    public void expandir() {
        List<Expansor> expansores = new ArrayList<Expansor>();
        for (int i = 0; i < _ramos.size(); i++) {
            Expansor e = new Expansor(i, _ramos.get(i), this);
            expansores.add(e);
            _numExecutores.incrementAndGet();
        }
        for (Expansor e : expansores) {
            synchronized (_executor) {
                _executor.execute(e);
            }
        }
        try {
            _semaforo.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException ignored) {
//        }
        System.out.println("Shutdown " + _numExecutores.get());
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
//        System.out.println("Novo: " + novo);
        Expansor e;
        synchronized (_ramos) {
            int tamanho = _ramos.size();
            adicionarRamo(novo);
            e = new Expansor(tamanho, novo, this);
        }
        if (_executor.isShutdown()) {
            System.out.println("ERRO");
        }
        try {
            synchronized (_executor) {
                _executor.execute(e);
            }
            _numExecutores.incrementAndGet();
        } catch (RejectedExecutionException ex) {
            ex.printStackTrace();
            System.out.println("num: " + _numExecutores.get());
        }
    }

    @Override
    public void ramoFechado(int id) {
//        System.out.println("Fechou: " + _ramos.get(id).toStringPuro());
        synchronized (_ramos) {
            _ramos.set(id, null);
        }
        if (_numExecutores.decrementAndGet() == 0) {
            System.out.println("release 1");
            _semaforo.release();
        }
        if (_numExecutores.get() < 0) {
            System.out.println("Erro");
        }
    }

    @Override
    public void ramoAberto(int id) {
//        System.out.println("Sobrou aberto: " + _ramos.get(id).toStringPuro());
        if (_numExecutores.decrementAndGet() == 0) {
            System.out.println("release 2");
            _semaforo.release();
        }
        if (_numExecutores.get() < 0) {
            System.out.println("Erro");
        }
    }
}