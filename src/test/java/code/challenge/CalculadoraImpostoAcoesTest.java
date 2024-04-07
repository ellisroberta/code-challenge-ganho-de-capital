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

    private CalculadoraImpostoAcoes calculadora;

    @BeforeEach
    public void setUp() {
        CalculadoraImpostoAcoes.operacoesAcoes.clear();
        calculadora = new CalculadoraImpostoAcoes();
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