package code.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class CalculadoraImpostoAcoes {

    static List<OperacaoAcao> operacoesAcoes = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Loop para ler cada linha de operações
        while (true) {
            System.out.println("\nPor favor, digite a entrada ou pressione Enter para sair:");
            String line = scanner.nextLine().trim();

            // Verifica se o usuário deseja sair
            if (line.isEmpty())
                break;

            System.out.println("\nEntrada: \n" + line);

            // Calcula os impostos e imprime a saída correspondente
            JSONArray operacoes = new JSONArray(line);
            JSONArray impostos = calcularImpostos(operacoes);
            System.out.println("\nSaida:");
            System.out.println(impostos);
        }

        scanner.close();
    }

    public static JSONArray calcularImpostos(JSONArray operacoes) {
        JSONArray impostos = new JSONArray();

        double precoMedioPonderado = calcularPrecoMedioPonderado();

        double prejuizoAcumulado = 0;

        for (int i = 0; i < operacoes.length(); i++) {
            JSONObject operacao = operacoes.getJSONObject(i);
            String tipoOperacao = operacao.getString("operation");
            double precoUnitario = operacao.getDouble("unit-cost");
            int quantidade = operacao.getInt("quantity");

            if (tipoOperacao.equals("buy")) {
                operacoesAcoes.add(new OperacaoAcao(precoUnitario, quantidade));
                precoMedioPonderado = calcularPrecoMedioPonderado();
                impostos.put(new JSONObject().put("tax", 0));
            } else if (tipoOperacao.equals("sell")) {
                double valorTotalOperacao = precoUnitario * quantidade;
                double lucro = Math.max(0, valorTotalOperacao - precoMedioPonderado * quantidade);
                double imposto = 0;

                if (valorTotalOperacao > 20000) {
                    if (prejuizoAcumulado > 0) {
                        lucro = Math.max(0, lucro - prejuizoAcumulado);
                        prejuizoAcumulado = Math.max(0, prejuizoAcumulado - lucro);
                    }

                    if (lucro > 0) {
                        imposto = lucro * 0.2; // Imposto de 20%
                    }
                }

                impostos.put(new JSONObject().put("tax", imposto));

                if (lucro < 0) {
                    prejuizoAcumulado += Math.abs(lucro);
                }
            }
        }

        return impostos;
    }

    static double calcularPrecoMedioPonderado() {
        double somaValores = 0;
        int somaQuantidades = 0;

        for (OperacaoAcao operacao : operacoesAcoes) {
            somaValores += operacao.getPrecoUnitario() * operacao.getQuantidade();
            somaQuantidades += operacao.getQuantidade();
        }

        if (somaQuantidades == 0) {
            return 0; // Retorna 0 se não houver operações
        }

        return Math.round((somaValores / somaQuantidades) * 100.0) / 100.0; // Arredondamento para 2 casas decimais
    }

    static class OperacaoAcao {
        private double precoUnitario;
        private int quantidade;

        public OperacaoAcao(double precoUnitario, int quantidade) {
            this.precoUnitario = precoUnitario;
            this.quantidade = quantidade;
        }

        public double getPrecoUnitario() {
            return precoUnitario;
        }

        public int getQuantidade() {
            return quantidade;
        }
    }
}
