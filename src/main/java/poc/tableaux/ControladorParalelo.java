package poc.tableaux;

public interface ControladorParalelo {
    void ramoNovo(Ramo novo);

    void ramoFechado(int id);

    void ramoAberto(int id);
}
