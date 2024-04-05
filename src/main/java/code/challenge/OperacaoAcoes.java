package code.challenge;

public class OperacaoAcoes {
    private double precoUnitario;
    private int quantidade;

    public OperacaoAcoes(double precoUnitario, int quantidade) {
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
