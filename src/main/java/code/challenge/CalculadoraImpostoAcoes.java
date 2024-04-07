package code.challenge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

            // Limpa a lista de operações para evitar interferência entre entradas
            operacoesAcoes.clear();
        }

        scanner.close();
    }

    public static JSONArray calcularImpostos(JSONArray operacoes) {
        JSONArray impostos = new JSONArray();
        BigDecimal precoMedioPonderado = calcularPrecoMedioPonderado();

        BigDecimal prejuizoAcumulado = BigDecimal.ZERO;

        for (int i = 0; i < operacoes.length(); i++) {
            JSONObject operacao = operacoes.getJSONObject(i);
            String tipoOperacao = operacao.getString("operation");
            BigDecimal precoUnitario = BigDecimal.valueOf(operacao.getDouble("unit-cost"));
            int quantidade = operacao.getInt("quantity");

            if (tipoOperacao.equals("buy")) {
                operacoesAcoes.add(new OperacaoAcoes(precoUnitario, quantidade));
                precoMedioPonderado = calcularPrecoMedioPonderado();
                impostos.put(new JSONObject().put("tax", BigDecimal.ZERO));
            } else if (tipoOperacao.equals("sell")) {
                BigDecimal valorTotalOperacao = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
                BigDecimal lucro = valorTotalOperacao.subtract(precoMedioPonderado.multiply(BigDecimal.valueOf(quantidade)));
                BigDecimal imposto = BigDecimal.ZERO;

                // Calcula o imposto apenas se houver lucro
                if (lucro.compareTo(BigDecimal.ZERO) > 0) {
                    imposto = lucro.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP); // Imposto de 20%
                    prejuizoAcumulado = BigDecimal.ZERO; // Zera o prejuízo acumulado se houver lucro
                } else {
                    prejuizoAcumulado = prejuizoAcumulado.add(lucro.abs());
                }

                impostos.put(new JSONObject().put("tax", imposto));
            }
        }

        return impostos;
    }
    static BigDecimal calcularPrecoMedioPonderado() {
        BigDecimal somaValores = BigDecimal.ZERO;
        int somaQuantidades = 0;

        for (OperacaoAcoes operacao : operacoesAcoes) {
            somaValores = somaValores.add(operacao.getPrecoUnitario().multiply(BigDecimal.valueOf(operacao.getQuantidade())));
            somaQuantidades += operacao.getQuantidade();
        }

        if (somaQuantidades == 0) {
            return BigDecimal.ZERO; // Retorna 0 se não houver operações
        }

        BigDecimal precoMedioPonderado = somaValores.divide(BigDecimal.valueOf(somaQuantidades), 2, RoundingMode.HALF_UP);
        return precoMedioPonderado;
    }

    public static List<OperacaoAcoes> lerEntrada(String entrada) {
        List<OperacaoAcoes> operacoes = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(entrada);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String tipo = jsonObject.getString("operation"); // Tipo de operação corrigido
                BigDecimal preco = BigDecimal.valueOf(jsonObject.getDouble("unit-cost")); // Preço corrigido
                int quantidade = jsonObject.getInt("quantity");

                // Criando objetos OperacaoAcoes com base nos valores lidos
                operacoes.add(new OperacaoAcoes(preco, quantidade/*, tipo, data*/));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return operacoes;
    }
}