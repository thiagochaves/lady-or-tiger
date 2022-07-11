package poc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import poc.afirmativa.*;
import poc.puzzle.Puzzle;
import poc.tableaux.Ramo;
import poc.tableaux.Tableau;
import poc.tableaux.TableauParalelo;

/**
 * Representa um tableaux semântico para um determinado problema.
 */
class Solucionador {

    /** Puzzle a ser resolvido. */
    private final Puzzle _puzzle;
    private final Tableau _tableau = new TableauParalelo();
    private final Logger _log = Logger.getLogger("tableau");

    /**
     * Cria um tableaux para resolver determinado puzzle.
     */
    public Solucionador(Puzzle puzzle) {
        _log.finest("Iniciando solucionador");
        _puzzle = puzzle;
        Ramo ramoInicial = new Ramo(puzzle);
        ramoInicial.adicionarAfirmativa(_puzzle.getAxioma());
        _tableau.adicionarRamo(ramoInicial);
    }

    /**
     * Obtém a solução do puzzle, se for coerente.
     */
    public Ramo getSolucao() {
        _log.fine("Iniciando busca por solução");
        configurarRelacaoPortasAfirmativas();
        _tableau.expandir();
        // Realiza mais algumas inferências
        List<Ramo> ramos = new ArrayList<Ramo>();
        for (Ramo r : _tableau.getRamos()) {
            // TODO Não é preciso obter uma cópia com os essenciais, porque os ramos só estarão com
            // afirmativas
            // essenciais neste ponto.
            Ramo ramo = r.getEssenciais();
            ramo = deduzirPresenca(ramo);
            if (ramo == null) {
                continue;
            }
            ramo = deduzirAusencia(ramo);
            if (ramo == null) {
                continue;
            }
            ramos.add(ramo);
        }
        _log.log(Level.FINE, "Tableau expandido totalmente: {0}", _tableau.toString());
        // Calculamos a interseção dos ramos restantes
        if (!ramos.isEmpty()) {
            return calcularIntersecao(ramos);
        } else {
            return new Ramo(_puzzle);
        }
    }

    /**
     * Se já foi descoberto o que há atrás de uma porta, então afirmaremos que os outros objetos não
     * podem estar atrás de tal porta.
     */
    private Ramo deduzirAusencia(Ramo ramo) {
        Ramo saida = new Ramo(ramo);
        for (Afirmativa e : ramo) {
            for (Localizacao n : e.explicitarObjetosQueNaoEstaoAqui()) {
                saida.adicionarAfirmativa(n);
            }
        }
        return saida;
    }

    /**
     * Dado que uma porta precisa esconder um e apenas um objeto este método analisa e infere o que
     * pode estar atrás das portas. O resultado completo (junto com dados iniciais) é retornado. Ex:
     * Se houver !em(m,1) e os únicos objetos válidos sejam m e t, então é inferido que em(t,1).
     * OBS: Sempre deve existir um objeto atrás de uma porta. Se isso não acontecer o método retorna
     * null.
     */
    @SuppressWarnings("unchecked")
    Ramo deduzirPresenca(Ramo ramo) {
        // 1- Criar um container para cada porta
        ArrayList<String>[] portas = new ArrayList[_puzzle.getNumPortas()];
        for (int i = 0; i < _puzzle.getNumPortas(); i++) {
            portas[i] = new ArrayList<String>();
        }
        // 2- Passar por todas as verdades, se ela for uma negação de uma
        // localização então então inclua seu objeto no container respectivo
        for (Afirmativa atual : ramo) {
            if (!(atual.estaNegada())) {
                continue;
            }
            if (atual instanceof Localizacao) {
                Localizacao localizacao = (Localizacao) atual;
                String objeto = localizacao.getObjeto();
                int lugar = localizacao.getLugar();
                if (!portas[lugar - 1].contains(objeto)) {
                    portas[lugar - 1].add(objeto);
                }
            }
        }
        // 3- Para cada porta determine os objetos que poderiam estar lá e
        // se houver apenas uma possibilidade adicione-a à verdade
        Ramo saida = new Ramo(ramo);
        for (int j = 0; j < portas.length; j++) {
            ArrayList<String> portaAtual = portas[j];
            HashSet<String> objetos = new HashSet<String>(_puzzle.getObjetos());
            objetos.removeAll(portaAtual);
            // O método Ramo.impossivelTerAlgoNoLugar() já elimina estas situações durante a
            // expansão
            if (objetos.size() == 0) {
                // Está dizendo que não há objetos atrás da porta, o que não pode acontecer
                return null;
            }
            if (objetos.size() == 1) {
                Localizacao localizacaoInferida =
                        new Localizacao(objetos.iterator().next(), j + 1);
                localizacaoInferida.associarAPuzzle(_puzzle);
                saida.adicionarAfirmativa(localizacaoInferida);
            }
        }
        return saida;
    }

    private Ramo calcularIntersecao(List<Ramo> ramos) {
        Ramo verdades = new Ramo(ramos.get(0).getEssenciais());
        for (Ramo r : ramos) {
            Ramo ramo = r.getEssenciais();
            verdades = verdades.calcularIntersecao(ramo);
        }
        return verdades;
    }

    private void configurarRelacaoPortasAfirmativas() {
        List<Afirmativa> deducoes =
                criarAfirmativasSeESomenteSePortaVerdadeiraEntaoConteudoPortaVerdadeiro();
        adicionarAfirmativasATodosOsRamos(deducoes);
    }

    private List<Afirmativa>
            criarAfirmativasSeESomenteSePortaVerdadeiraEntaoConteudoPortaVerdadeiro() {
        List<Afirmativa> deducoes = new ArrayList<Afirmativa>();
        for (int iPorta = 1; iPorta <= _puzzle.getNumPortas(); iPorta++) {
            Afirmativa afPorta = _puzzle.getPorta(iPorta);
            Bicondicional portaEstaCertaOuErrada =
                    new Bicondicional(afPorta, new Referencia(iPorta));
            portaEstaCertaOuErrada.associarAPuzzle(_puzzle);
            deducoes.add(portaEstaCertaOuErrada);
        }
        return deducoes;
    }

    private void adicionarAfirmativasATodosOsRamos(List<Afirmativa> deducoes) {
        for (Afirmativa af : deducoes) {
            for (Ramo r : _tableau.getRamos()) {
                r.adicionarAfirmativa(af);
            }
        }
    }
}