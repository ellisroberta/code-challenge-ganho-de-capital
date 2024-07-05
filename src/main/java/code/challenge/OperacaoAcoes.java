package code.challenge;

import java.math.BigDecimal;

public class OperacaoAcoes {
    private final BigDecimal precoUnitario;
    private final int quantidade;

    public OperacaoAcoes(BigDecimal precoUnitario, int quantidade) {
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
