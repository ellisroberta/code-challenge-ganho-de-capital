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
    static List<OperacaoAcoes> operacoesAcoes = new ArrayList<>();
    static Logger logger = Logger.getLogger(CalculadoraImpostoAcoes.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            logger.info("\nPor favor, digite a entrada ou pressione Enter para sair: ");
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                break;
            }

            JSONArray operacoes = new JSONArray(line);

            // Verifica se há operações na entrada antes de calcular impostos e registrar saídas
            if (operacoes.length() > 0) {
                logger.info("\nEntrada: ");
                logger.info(line);

                JSONArray impostos = calcularImpostos(operacoes);

                logger.info("\nSaida: ");
                logger.info(impostos.toString());
            }
        }

        scanner.close();
    }

    public static JSONArray calcularImpostos(JSONArray operacoes) {
        JSONArray impostos = new JSONArray();
        operacoesAcoes.clear(); // Limpar o estado antes de calcular impostos
        BigDecimal precoMedioPonderado = BigDecimal.ZERO;
        BigDecimal prejuizoAcumulado = BigDecimal.ZERO;

        for (int i = 0; i < operacoes.length(); i++) {
            JSONObject operacao = operacoes.getJSONObject(i);
            String tipoOperacao = operacao.getString("operation");
            BigDecimal precoUnitario = BigDecimal.valueOf(operacao.getDouble("unit-cost"));
            int quantidade = operacao.getInt("quantity");

            if (tipoOperacao.equals("buy")) {
                operacoesAcoes.add(new OperacaoAcoes(precoUnitario, quantidade));
                precoMedioPonderado = calcularPrecoMedioPonderado(operacoesAcoes);
                impostos.put(new JSONObject().put("tax", BigDecimal.ZERO));
            } else if (tipoOperacao.equals("sell")) {
                BigDecimal valorTotalOperacao = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
                BigDecimal lucro = valorTotalOperacao.subtract(precoMedioPonderado.multiply(BigDecimal.valueOf(quantidade)));
                BigDecimal imposto = BigDecimal.ZERO;

                if (valorTotalOperacao.compareTo(BigDecimal.valueOf(20000)) > 0) {
                    if (prejuizoAcumulado.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal prejuizoUtilizado = prejuizoAcumulado.min(lucro);
                        lucro = lucro.subtract(prejuizoUtilizado);
                        prejuizoAcumulado = prejuizoAcumulado.subtract(prejuizoUtilizado);
                    }

                    if (lucro.compareTo(BigDecimal.ZERO) > 0) {
                        imposto = lucro.multiply(BigDecimal.valueOf(0.2)); // Imposto de 20%
                    }
                }

                impostos.put(new JSONObject().put("tax", imposto));

                if (lucro.compareTo(BigDecimal.ZERO) < 0) {
                    prejuizoAcumulado = prejuizoAcumulado.add(lucro.negate());
                }
            }
        }

        return impostos;
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

    public static List<OperacaoAcoes> lerEntrada(String entrada) {
        JSONArray jsonArray = new JSONArray(entrada);
        List<OperacaoAcoes> operacoes = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            BigDecimal precoUnitario = jsonObject.getBigDecimal("unit-cost");
            int quantidade = jsonObject.getInt("quantity");
            operacoes.add(new OperacaoAcoes(precoUnitario, quantidade));
        }

        return operacoes;
    }
}