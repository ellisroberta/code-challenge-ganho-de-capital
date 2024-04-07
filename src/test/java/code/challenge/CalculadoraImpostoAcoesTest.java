package code.challenge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraImpostoAcoesTest {

    private CalculadoraImpostoAcoes calculadora;

    @BeforeEach
    public void setUp() {
        CalculadoraImpostoAcoes.operacoesAcoes.clear();
        calculadora = new CalculadoraImpostoAcoes();
    }

    @Test
    @DisplayName("Deve calcular imposto zero quando não há lucro")
    public void testCalcularImpostoSemLucro() {
        JSONArray operacoes = new JSONArray();
        operacoes.put(new JSONObject().put("operation", "buy").put("unit-cost", 10).put("quantity", 100));
        operacoes.put(new JSONObject().put("operation", "sell").put("unit-cost", 8).put("quantity", 100));

        JSONArray impostos = CalculadoraImpostoAcoes.calcularImpostos(operacoes);

        assertEquals(2, impostos.length());

        JSONObject impostoVenda = impostos.getJSONObject(1);
        assertEquals(BigDecimal.ZERO, impostoVenda.getBigDecimal("tax"));
    }

    @Test
    @DisplayName("Deve calcular imposto quando há lucro")
    public void testCalcularImpostoComLucro() {
        JSONArray operacoes = new JSONArray();
        operacoes.put(new JSONObject().put("operation", "buy").put("unit-cost", 10).put("quantity", 100));
        operacoes.put(new JSONObject().put("operation", "sell").put("unit-cost", 15).put("quantity", 100));

        JSONArray impostos = CalculadoraImpostoAcoes.calcularImpostos(operacoes);

        assertEquals(2, impostos.length());

        JSONObject impostoVenda = impostos.getJSONObject(1);
        BigDecimal impostoEsperado = BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_UP);
        assertEquals(impostoEsperado, impostoVenda.getBigDecimal("tax"));
    }

    @Test
    @DisplayName("Deve calcular imposto para múltiplas operações")
    public void testCalcularImpostoComMultiplasOperacoes() {
        JSONArray operacoes = new JSONArray();
        operacoes.put(new JSONObject().put("operation", "buy").put("unit-cost", 10).put("quantity", 100));
        operacoes.put(new JSONObject().put("operation", "buy").put("unit-cost", 12).put("quantity", 50));
        operacoes.put(new JSONObject().put("operation", "sell").put("unit-cost", 20).put("quantity", 100));

        JSONArray impostos = CalculadoraImpostoAcoes.calcularImpostos(operacoes);

        assertEquals(3, impostos.length());

        JSONObject impostoVenda = impostos.getJSONObject(2);
        BigDecimal impostoEsperado = BigDecimal.valueOf(186.60).setScale(2, RoundingMode.HALF_UP);
        assertEquals(impostoEsperado, impostoVenda.getBigDecimal("tax"));
    }

    @Test
    @DisplayName("Deve calcular preço médio ponderado")
    public void testCalcularPrecoMedioPonderado() {
        CalculadoraImpostoAcoes.operacoesAcoes.add(new OperacaoAcoes(BigDecimal.valueOf(10), 100));
        CalculadoraImpostoAcoes.operacoesAcoes.add(new OperacaoAcoes(BigDecimal.valueOf(15), 50));

        BigDecimal precoMedioPonderado = CalculadoraImpostoAcoes.calcularPrecoMedioPonderado();

        assertEquals(BigDecimal.valueOf(11.67), precoMedioPonderado.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    @DisplayName("Deve ler a entrada")
    public void testLerEntrada() {
        String entrada = "[{\"operation\":\"sell\",\"quantity\":100,\"unit-cost\":10}," +
                "{\"operation\":\"buy\",\"quantity\":50,\"unit-cost\":5}]";

        List<OperacaoAcoes> operacoes = calculadora.lerEntrada(entrada);

        assertEquals(2, operacoes.size());

        assertEquals(BigDecimal.valueOf(10.0), operacoes.get(0).getPrecoUnitario());
        assertEquals(100, operacoes.get(0).getQuantidade());

        assertEquals(BigDecimal.valueOf(5.0), operacoes.get(1).getPrecoUnitario());
        assertEquals(50, operacoes.get(1).getQuantidade());
    }
}