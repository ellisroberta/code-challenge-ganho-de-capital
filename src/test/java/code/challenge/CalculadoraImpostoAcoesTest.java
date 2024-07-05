package code.challenge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraImpostoAcoesTest {

    @InjectMocks
    private CalculadoraImpostoAcoes calculadoraImpostoAcoes;

    @Test
    @DisplayName("Teste calcular imposto com lucro e prejuízo acumulado")
    void testCalcularImposto() {
        BigDecimal lucro = new BigDecimal("500.00");
        BigDecimal prejuizoAcumulado = new BigDecimal("100.00");
        BigDecimal imposto = CalculadoraImpostoAcoes.calcularImposto(lucro, prejuizoAcumulado);
        assertEquals(new BigDecimal("80.00"), imposto);
    }

    @Test
    @DisplayName("Teste calcular imposto sem prejuízo acumulado")
    void testCalcularImpostoSemPrejuizo() {
        BigDecimal lucro = new BigDecimal("500.00");
        BigDecimal prejuizoAcumulado = BigDecimal.ZERO;
        BigDecimal imposto = CalculadoraImpostoAcoes.calcularImposto(lucro, prejuizoAcumulado);
        assertEquals(new BigDecimal("100.00"), imposto);
    }

    @Test
    @DisplayName("Teste calcular imposto com lucro igual ao prejuízo acumulado")
    void testCalcularImpostoLucroIgualPrejuizo() {
        BigDecimal lucro = new BigDecimal("100.00");
        BigDecimal prejuizoAcumulado = new BigDecimal("100.00");
        BigDecimal imposto = CalculadoraImpostoAcoes.calcularImposto(lucro, prejuizoAcumulado);
        assertEquals(BigDecimal.ZERO, imposto);
    }

    @Test
    @DisplayName("Teste calcular imposto com lucro menor que o prejuízo acumulado")
    void testCalcularImpostoLucroMenorQuePrejuizo() {
        BigDecimal lucro = new BigDecimal("100.00");
        BigDecimal prejuizoAcumulado = new BigDecimal("200.00");
        BigDecimal imposto = CalculadoraImpostoAcoes.calcularImposto(lucro, prejuizoAcumulado);
        assertEquals(BigDecimal.ZERO, imposto);
    }

//    @Test
//    @DisplayName("Teste processar operações")
//    void testProcessarOperacoes() {
//        JSONArray operacoes = new JSONArray();
//        operacoes.put(new JSONObject().put("unit-cost", new BigDecimal("10.00")).put("quantity", 100).put("operation", "buy"));
//        operacoes.put(new JSONObject().put("unit-cost", new BigDecimal("15.00")).put("quantity", 50).put("operation", "sell"));
//
//        JSONArray impostos = CalculadoraImpostoAcoes.processarOperacoes(operacoes);
//        assertEquals(1, impostos.length());
//        assertEquals(new BigDecimal("50.00"), impostos.getJSONObject(0).getBigDecimal("tax"));
//    }

//    @Test
//    @DisplayName("Teste caso 1")
//    void testCaso1() {
//        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}]");
//        JSONArray resultadoEsperado = new JSONArray("[{\"tax\":0},{\"tax\":0},{\"tax\":1000.00}]");
//
//        JSONArray resultadoObtido = CalculadoraImpostoAcoes.processarOperacoes(operacoes);
//        assertEquals(resultadoEsperado.toString(), resultadoObtido.toString());
//    }
//
//    @Test
//    @DisplayName("Teste caso 2")
//    void testCaso2() {
//        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}]");
//        JSONArray resultadoEsperado = new JSONArray("[{\"tax\":0},{\"tax\":0},{\"tax\":0}]");
//
//        JSONArray resultadoObtido = CalculadoraImpostoAcoes.processarOperacoes(operacoes);
//        assertEquals(resultadoEsperado.toString(), resultadoObtido.toString());
//    }
//
//    @Test
//    @DisplayName("Teste caso 1 + caso 2")
//    void testCaso1Mais2() {
//        JSONArray operacoesCaso1 = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}]");
//        JSONArray operacoesCaso2 = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}]");
//
//        JSONArray resultadoEsperadoCaso1 = new JSONArray("[{\"tax\":0},{\"tax\":0},{\"tax\":1000.00}]");
//        JSONArray resultadoEsperadoCaso2 = new JSONArray("[{\"tax\":0},{\"tax\":0},{\"tax\":0}]");
//
//        JSONArray resultadoObtidoCaso1 = CalculadoraImpostoAcoes.processarOperacoes(operacoesCaso1);
//        JSONArray resultadoObtidoCaso2 = CalculadoraImpostoAcoes.processarOperacoes(operacoesCaso2);
//
//        assertEquals(resultadoEsperadoCaso1.toString(), resultadoObtidoCaso1.toString());
//        assertEquals(resultadoEsperadoCaso2.toString(), resultadoObtidoCaso2.toString());
//    }
//
//    @Test
//    @DisplayName("Teste caso 3")
//    void testCaso3() {
//        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}]");
//        JSONArray resultadoEsperado = new JSONArray("[{\"tax\":0},{\"tax\":0},{\"tax\":1000.00}]");
//
//        JSONArray resultadoObtido = CalculadoraImpostoAcoes.processarOperacoes(operacoes);
//        assertEquals(resultadoEsperado.toString(), resultadoObtido.toString());
//    }
//
//    @Test
//    @DisplayName("Teste caso 4")
//    void testCaso4() {
//        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}]");
//        JSONArray resultadoEsperado = new JSONArray("[{\"tax\":0},{\"tax\":0},{\"tax\":0}]");
//
//        JSONArray resultadoObtido = CalculadoraImpostoAcoes.processarOperacoes(operacoes);
//        assertEquals(resultadoEsperado.toString(), resultadoObtido.toString());
//    }
//
//    @Test
//    @DisplayName("Teste caso 6")
//    void testCaso6() {
//        JSONArray operacoes = new JSONArray("[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000}]");
//        JSONArray resultadoEsperado = new JSONArray("[{\"tax\":0},{\"tax\":0},{\"tax\":0},{\"tax\":0},{\"tax\":3000.00}]");
//
//        JSONArray resultadoObtido = CalculadoraImpostoAcoes.processarOperacoes(operacoes);
//        assertEquals(resultadoEsperado.toString(), resultadoObtido.toString());
//    }
}