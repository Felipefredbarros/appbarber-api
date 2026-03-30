package com.appbarber.api.dto;

import com.appbarber.api.model.Barbearia;
import com.appbarber.api.model.Servico;

public record BarbeariaResponse(Long id, String nome, String descricao, String img) {
    public BarbeariaResponse(Barbearia b) {
        this(b.getId(), b.getNome(), b.getDescricao(), b.getImg());
    }

}
