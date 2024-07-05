package code.challenge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraImpostoAcoesTest {

    @Test
    @DisplayName("Deve processar operações e calcular impostos")
    void testProcessarOperacoes() {
        JSONArray operacoes = new JSONArray();
        operacoes.put(new JSONObject().put("operation", "buy").put("unit-cost", BigDecimal.valueOf(10)).put("quantity", 100));
        operacoes.put(new JSONObject().put("operation", "sell").put("unit-cost", BigDecimal.valueOf(15)).put("quantity", 50));

        JSONArray impostos = CalculadoraImpostoAcoes.processarOperacoes(operacoes);

        assertEquals(2, impostos.length());

        // Verifica se o imposto da operação de venda é zero
        JSONObject impostoVenda = impostos.getJSONObject(1);
        assertEquals(BigDecimal.ZERO, impostoVenda.getBigDecimal("tax"));
    }

    @Test
    @DisplayName("Caso de teste: Input 1 (Case3)")
    void testCase3() {
        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}," +
                "{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}," +
                "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}]");

        JSONArray impostos = CalculadoraImpostoAcoes.processarOperacoes(operacoes);

        assertEquals(3, impostos.length());
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(0).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(1).getBigDecimal("tax"));
        assertEquals(BigDecimal.valueOf(1000), impostos.getJSONObject(2).getBigDecimal("tax"));
    }

    @Test
    @DisplayName("Caso de teste: Input 2 (Case4)")
    void testCase4() {
        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}," +
                "{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000}," +
                "{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}]");

        JSONArray impostos = CalculadoraImpostoAcoes.processarOperacoes(operacoes);

        assertEquals(3, impostos.length());
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(0).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(1).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(2).getBigDecimal("tax"));
    }

    @Test
    @DisplayName("Caso de teste: Input (Case6)")
    void testCase6() {
        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}," +
                "{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000}," +
                "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000}," +
                "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000}," +
                "{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000}]");

        JSONArray impostos = CalculadoraImpostoAcoes.processarOperacoes(operacoes);

        assertEquals(5, impostos.length());
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(0).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(1).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(2).getBigDecimal("tax"));
        assertEquals(BigDecimal.ZERO, impostos.getJSONObject(3).getBigDecimal("tax"));
        assertEquals(BigDecimal.valueOf(3000), impostos.getJSONObject(4).getBigDecimal("tax"));
    }
}