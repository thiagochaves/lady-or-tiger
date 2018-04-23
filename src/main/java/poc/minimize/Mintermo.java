package poc.minimize;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class Mintermo {
    private List<Variavel> _variaveis;
    private Map<Variavel, Valor> _valores = Maps.newHashMap();

    private Mintermo() {}

    public static Mintermo criar(List<Variavel> todas, List<Variavel> verdades) {
        Preconditions.checkNotNull(todas);
        Preconditions.checkNotNull(verdades);
        Preconditions.checkArgument(todas.containsAll(verdades));
        Mintermo novo = new Mintermo();
        novo._variaveis = todas;
        for (Variavel v : todas) {
            if (verdades.contains(v)) {
                novo._valores.put(v, Valor.UM);
            } else {
                novo._valores.put(v, Valor.ZERO);
            }
        }
        return novo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Variavel v : _variaveis) {
            sb.append(v.getNome());
            sb.append(exibicao(_valores.get(v)));
        }
        return sb.toString();
    }

    private String exibicao(Valor valor) {
        switch (valor) {
            case ZERO: return "0";
            case UM: return "1";
            case AMBOS: return "-";
            case QUALQUER: return "X";
        }
        return "";
    }

    public static void main(String[] args) {
        Variavel a = new Variavel("a");
        Variavel b = new Variavel("b");
        Mintermo m = Mintermo.criar(Lists.newArrayList(a, b), Lists.newArrayList(a));
        System.out.println(m);
    }
}
