package code.challenge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraImpostoAcoesTest {

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

        assertEquals(2, impostos.length());

        // Verifica se o imposto da operação de venda é 0
        JSONObject impostoVenda = impostos.getJSONObject(1);
        assertEquals(BigDecimal.ZERO, impostoVenda.getBigDecimal("tax"));
    }

    @Test
    @DisplayName("Deve calcular preço médio ponderado")
    public void testCalcularPrecoMedioPonderado() {
        List<OperacaoAcoes> operacoesAcoes = List.of(
                new OperacaoAcoes(BigDecimal.valueOf(10), 100),
                new OperacaoAcoes(BigDecimal.valueOf(15), 50)
        );

        BigDecimal precoMedioPonderado = CalculadoraImpostoAcoes.calcularPrecoMedioPonderado(operacoesAcoes);

        assertEquals(BigDecimal.valueOf(11.67), precoMedioPonderado.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    @DisplayName("Deve ler a entrada")
    public void testLerEntrada() {
        String entrada = "[{\"operation\":\"sell\",\"quantity\":100,\"unit-cost\":10}," +
                "{\"operation\":\"buy\",\"quantity\":50,\"unit-cost\":5}]";

        List<OperacaoAcoes> operacoes = CalculadoraImpostoAcoes.lerEntrada(entrada);

        assertEquals(2, operacoes.size());

        assertEquals(0, BigDecimal.valueOf(10.0).compareTo(operacoes.get(0).getPrecoUnitario()));
        assertEquals(100, operacoes.get(0).getQuantidade());

        assertEquals(0, BigDecimal.valueOf(5.0).compareTo(operacoes.get(1).getPrecoUnitario()));
        assertEquals(50, operacoes.get(1).getQuantidade());
    }

    @Test
    @DisplayName("Caso de teste: Input 1 (Case3)")
    public void testCase3() {
        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}," +
                "{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}," +
                "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}]");

        JSONArray impostos = CalculadoraImpostoAcoes.calcularImpostos(operacoes);

        assertEquals(3, impostos.length());
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(0).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(1).getBigDecimal("tax"));
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(impostos.getJSONObject(2).getBigDecimal("tax")));
    }

    @Test
    @DisplayName("Caso de teste: Input 2 (Case4)")
    public void testCase4() {
        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}," +
                "{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000}," +
                "{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}]");

        JSONArray impostos = CalculadoraImpostoAcoes.calcularImpostos(operacoes);

        assertEquals(3, impostos.length());
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(0).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(1).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(2).getBigDecimal("tax"));
    }

    @Test
    @DisplayName("Caso de teste: Input (Case6)")
    public void testCase6() {
        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}," +
                "{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000}," +
                "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000}," +
                "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000}," +
                "{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000}]");

        JSONArray impostos = CalculadoraImpostoAcoes.calcularImpostos(operacoes);

        assertEquals(5, impostos.length());
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(0).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(1).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(2).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(3).getBigDecimal("tax"));
        assertEquals(0, BigDecimal.valueOf(3000).compareTo(impostos.getJSONObject(4).getBigDecimal("tax")));
    }
}