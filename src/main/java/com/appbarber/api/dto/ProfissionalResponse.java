package com.appbarber.api.dto;

import com.appbarber.api.model.Profissional;

import java.util.ArrayList;
import java.util.List;

public record ProfissionalResponse(Long id, String nome, List<ServicoResponse> servicos) {
    public ProfissionalResponse(Profissional p) {
        this(
                p.getId(),
                p.getNome(),
                p.getServicos() != null ? p.getServicos().stream().map(ServicoResponse::new).toList() : new ArrayList<>()
        );
    }
}
