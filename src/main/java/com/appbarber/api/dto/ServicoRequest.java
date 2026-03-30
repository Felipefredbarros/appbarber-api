package com.appbarber.api.dto;

import com.appbarber.api.model.Servico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServicoRequest(@NotBlank String nome,
                             String descricao,
                             @NotNull BigDecimal preco,
                             @NotNull Integer duracao,
                             @NotNull Long barbeariaId,
                             String img) {
}
