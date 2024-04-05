package code.challenge;

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