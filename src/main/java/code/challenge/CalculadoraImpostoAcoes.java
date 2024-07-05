package code.challenge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class CalculadoraImpostoAcoes {
    private static final Logger logger = Logger.getLogger(CalculadoraImpostoAcoes.class.getName());
    private static final BigDecimal TAXA_IMPOSTO = new BigDecimal("0.2");

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
                logger.info("Entrada: " + line);

                JSONArray impostos = processarOperacoes(operacoes);

                logger.info("Saida: " + impostos.toString());
            }
        }

        scanner.close();
    }

    public static JSONArray processarOperacoes(JSONArray operacoes) {
        JSONArray impostos = new JSONArray();
        List<OperacaoAcoes> operacoesAcoes = lerEntrada(operacoes);

        if (operacoesAcoes.isEmpty()) {
            return impostos;
        }

        BigDecimal precoMedioPonderado = BigDecimal.ZERO;
        BigDecimal prejuizoAcumulado = BigDecimal.ZERO;
        int quantidadeTotal = 0;

        for (OperacaoAcoes operacao : operacoesAcoes) {
            if ("sell".equals(operacao.getTipoOperacao())) {
                BigDecimal valorTotalOperacao = operacao.getPrecoUnitario().multiply(BigDecimal.valueOf(operacao.getQuantidade()));

                if (valorTotalOperacao.compareTo(new BigDecimal("20000")) <= 0) {
                    impostos.put(new JSONObject().put("tax", BigDecimal.ZERO));
                    continue;
                }

                BigDecimal lucro = calcularLucro(precoMedioPonderado, operacao);
                BigDecimal imposto = BigDecimal.ZERO;

                if (lucro.compareTo(BigDecimal.ZERO) > 0) {
                    imposto = calcularImposto(lucro, prejuizoAcumulado);
                }

                impostos.put(new JSONObject().put("tax", imposto));

                if (lucro.compareTo(BigDecimal.ZERO) < 0) {
                    prejuizoAcumulado = prejuizoAcumulado.add(lucro.negate());
                }

                quantidadeTotal -= operacao.getQuantidade();
            } else {
                precoMedioPonderado = atualizarPrecoMedioPonderado(precoMedioPonderado, operacao, quantidadeTotal);
                quantidadeTotal += operacao.getQuantidade();
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

    static BigDecimal calcularImposto(BigDecimal lucro, BigDecimal prejuizoAcumulado) {
        if (lucro.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal lucroAposPrejuizo = lucro.subtract(prejuizoAcumulado);

        if (lucroAposPrejuizo.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal imposto = lucroAposPrejuizo.multiply(TAXA_IMPOSTO);

        return imposto.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal atualizarPrecoMedioPonderado(BigDecimal precoMedioPonderado, OperacaoAcoes operacao, int quantidadeTotal) {
        BigDecimal valorTotalAtual = precoMedioPonderado.multiply(BigDecimal.valueOf(quantidadeTotal));
        BigDecimal valorTotalNovo = operacao.getPrecoUnitario().multiply(BigDecimal.valueOf(operacao.getQuantidade()));
        BigDecimal valorTotalFinal = valorTotalAtual.add(valorTotalNovo);
        int quantidadeTotalFinal = quantidadeTotal + operacao.getQuantidade();

        return valorTotalFinal.divide(BigDecimal.valueOf(quantidadeTotalFinal), 2, RoundingMode.HALF_UP);
    }
}