package code.challenge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CalculadoraImpostoAcoes {
    static List<OperacaoAcoes> operacoesAcoes = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nPor favor, digite a entrada ou pressione Enter para sair:");
            String line = scanner.nextLine().trim();

            if (line.isEmpty())
                break;

            System.out.println("\nEntrada: \n" + line);

            JSONArray operacoes = new JSONArray(line);
            JSONArray impostos = calcularImpostos(operacoes);
            System.out.println("\nSaida:");
            System.out.println(impostos);
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
            valorTotal = valorTotal.add(operacao.getPrecoUnitario().multiply(BigDecimal.valueOf(operacao.getQuantidade())));
            quantidadeTotal += operacao.getQuantidade();
        }

        return quantidadeTotal > 0 ? valorTotal.divide(BigDecimal.valueOf(quantidadeTotal), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
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