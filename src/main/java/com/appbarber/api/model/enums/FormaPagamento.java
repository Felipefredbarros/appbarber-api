package com.appbarber.api.model.enums;

public enum FormaPagamento {
    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    PIX("Pix"),
    DINHEIRO("Dinheiro");

    private final String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
