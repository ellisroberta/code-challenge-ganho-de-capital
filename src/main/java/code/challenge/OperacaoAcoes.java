package code.challenge;

import java.math.BigDecimal;

public class OperacaoAcoes {
    private final BigDecimal precoUnitario;
    private final int quantidade;
    private final String tipoOperacao;

    public OperacaoAcoes(BigDecimal precoUnitario, int quantidade, String tipoOperacao) {
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.tipoOperacao = tipoOperacao;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }
}