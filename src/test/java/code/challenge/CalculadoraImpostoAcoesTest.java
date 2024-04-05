package code.challenge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class CalculadoraImpostoAcoesTest {

    private CalculadoraImpostoAcoes calculadora;

    @BeforeEach
    public void setUp() {
        CalculadoraImpostoAcoes.operacoesAcoes.clear();
    }

    @Test
    @DisplayName("Deve calcular impostos")
    public void testCalcularImpostos() {
        JSONArray operacoes = new JSONArray();
        operacoes.put(new JSONObject().put("operation", "buy").put("unit-cost", 10).put("quantity", 100));
        operacoes.put(new JSONObject().put("operation", "sell").put("unit-cost", 15).put("quantity", 50));

        JSONArray impostos = CalculadoraImpostoAcoes.calcularImpostos(operacoes);

        // Ajuste a expectativa para 2, pois há uma operação de compra e uma de venda
        assertEquals(2, impostos.length());

        // Verifica se o imposto da operação de venda é 0
        JSONObject impostoVenda = impostos.getJSONObject(1);
        assertEquals(0, impostoVenda.getDouble("tax"));
    }

    @Test
    @DisplayName("Deve calcular preço médio ponderado")
    public void testCalcularPrecoMedioPonderado() {
        CalculadoraImpostoAcoes.operacoesAcoes.add(new OperacaoAcoes(10, 100));
        CalculadoraImpostoAcoes.operacoesAcoes.add(new OperacaoAcoes(15, 50));

        double precoMedioPonderado = CalculadoraImpostoAcoes.calcularPrecoMedioPonderado();

        assertEquals(11.67, precoMedioPonderado, 0.01);
    }
    @Test
    @DisplayName("Deve ler a entrada")
    public void testLerEntrada() {
        CalculadoraImpostoAcoes calculadora = new CalculadoraImpostoAcoes();

        String entrada = "[{\"tipo\":\"venda\",\"data\":\"2022-01-01\",\"quantidade\":100,\"preco\":10}," +
                "{\"tipo\":\"compra\",\"data\":\"2022-01-02\",\"quantidade\":50,\"preco\":5}]";

        List<OperacaoAcoes> operacoes = calculadora.lerEntrada(entrada);

        assertEquals(2, operacoes.size());
//        assertEquals("venda", operacoes.get(0).getTipo());
//        assertEquals("2022-01-01", operacoes.get(0).getData());
        assertEquals(100, operacoes.get(0).getQuantidade());
        assertEquals(10, operacoes.get(0).getPrecoUnitario(), 0.001);

//        assertEquals("compra", operacoes.get(1).getTipo());
//        assertEquals("2022-01-02", operacoes.get(1).getData());
        assertEquals(50, operacoes.get(1).getQuantidade());
        assertEquals(5, operacoes.get(1).getPrecoUnitario(), 0.001);
    }
}