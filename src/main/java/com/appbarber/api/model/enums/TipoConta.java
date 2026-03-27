package com.appbarber.api.model.enums;

public enum TipoConta {
    CLIENTE("Cliente"),
    BARBEIRO("Barbeiro"),
    DONO("Dono");

    private final String descricao;

    TipoConta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
