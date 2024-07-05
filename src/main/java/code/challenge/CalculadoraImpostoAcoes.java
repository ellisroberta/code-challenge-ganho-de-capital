package code.challenge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculadoraImpostoAcoes {
    private static final Logger logger = Logger.getLogger(CalculadoraImpostoAcoes.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            logger.info("\nPor favor, digite a entrada ou pressione Enter para sair: ");
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                break;
            }

            JSONArray operacoes = new JSONArray(line);

            if (operacoes.length() > 0) {
                logger.info("\nEntrada: ");
                logger.info(line);

                JSONArray impostos = processarOperacoes(operacoes);

                if (logger.isLoggable(Level.INFO)) {
                    logger.info("\nSaida: ");
                    logger.info(impostos.toString());
                }
            }
        }

        scanner.close();
    }

    public static JSONArray processarOperacoes(JSONArray operacoes) {
        JSONArray impostos = new JSONArray();
        List<OperacaoAcoes> operacoesAcoes = lerEntrada(operacoes);

        if (operacoesAcoes.isEmpty()) {
            return impostos; // Retorna um array vazio se não houver operações válidas
        }

        BigDecimal precoMedioPonderado = calcularPrecoMedioPonderado(operacoesAcoes);
        BigDecimal prejuizoAcumulado = BigDecimal.ZERO;

        for (OperacaoAcoes operacao : operacoesAcoes) {
            if ("sell".equals(operacao.getTipoOperacao())) {
                BigDecimal lucro = calcularLucro(precoMedioPonderado, operacao);
                BigDecimal imposto = calcularImposto(lucro, prejuizoAcumulado);

                impostos.put(new JSONObject().put("tax", imposto));
                atualizarPrejuizoAcumulado(lucro, prejuizoAcumulado);
            }
        }

        return impostos;
    }

    private static List<OperacaoAcoes> lerEntrada(JSONArray operacoes) {
        List<OperacaoAcoes> operacoesAcoes = new ArrayList<>();

        for (int i = 0; i < operacoes.length(); i++) {
            JSONObject operacao = operacoes.getJSONObject(i);
            BigDecimal precoUnitario = operacao.getBigDecimal("unit-cost");
            int quantidade = operacao.getInt("quantity");
            String tipoOperacao = operacao.getString("operation");

            operacoesAcoes.add(new OperacaoAcoes(precoUnitario, quantidade, tipoOperacao));
        }

        return operacoesAcoes;
    }

    private static BigDecimal calcularLucro(BigDecimal precoMedioPonderado, OperacaoAcoes operacao) {
        BigDecimal valorTotalOperacao = operacao.getPrecoUnitario().multiply(BigDecimal.valueOf(operacao.getQuantidade()));
        return valorTotalOperacao.subtract(precoMedioPonderado.multiply(BigDecimal.valueOf(operacao.getQuantidade())));
    }

    private static BigDecimal calcularImposto(BigDecimal lucro, BigDecimal prejuizoAcumulado) {
        if (lucro.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal imposto = lucro.multiply(BigDecimal.valueOf(0.2));

        if (lucro.compareTo(BigDecimal.valueOf(20000)) > 0 && prejuizoAcumulado.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal prejuizoUtilizado = prejuizoAcumulado.min(lucro);
            imposto = imposto.subtract(prejuizoUtilizado.multiply(BigDecimal.valueOf(0.2)));
        }

        return imposto.setScale(2, RoundingMode.HALF_UP);
    }

    private static void atualizarPrejuizoAcumulado(BigDecimal lucro, BigDecimal prejuizoAcumulado) {
        if (lucro.compareTo(BigDecimal.ZERO) < 0) {
            prejuizoAcumulado.add(lucro.negate());
        }
    }

    public static BigDecimal calcularPrecoMedioPonderado(List<OperacaoAcoes> operacoesAcoes) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        int quantidadeTotal = 0;

        for (OperacaoAcoes operacao : operacoesAcoes) {
            BigDecimal valorOperacao = operacao.getPrecoUnitario().multiply(BigDecimal.valueOf(operacao.getQuantidade()));
            valorTotal = valorTotal.add(valorOperacao);
            quantidadeTotal += operacao.getQuantidade();
        }

        if (quantidadeTotal > 0) {
            return valorTotal.divide(BigDecimal.valueOf(quantidadeTotal), 2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }
}