package poc.tableaux;

import java.util.List;

public interface Tableau {
    /**
     * Expande o tableaux ao máximo de forma estrita.
     */
    void expandir();

    /**
     * Adiciona um ramo ao tableaux.
     * @param ramo Não pode ser <code>null</code>.
     */
    void adicionarRamo(Ramo ramo);

    /**
     * Obtém a árvore toda.
     */
    List<Ramo> getRamos();
}
