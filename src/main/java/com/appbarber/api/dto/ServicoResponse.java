package com.appbarber.api.dto;

import com.appbarber.api.model.Servico;

import java.math.BigDecimal;

public record ServicoResponse(Long id, String nome, String descricao, BigDecimal preco, Integer duracao, String img) {
    public ServicoResponse(Servico s) {
        this(s.getId(), s.getNome(), s.getDescricao(), s.getPreco(), s.getDuracao(), s.getImg());
    }
}
