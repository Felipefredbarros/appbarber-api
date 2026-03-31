package com.appbarber.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProfissionalRequest(@NotBlank String nome,
                                  String img,
                                  List<@Valid ContatoDTO> contatos,
                                  @NotNull Long barbeariaId,
                                  @NotEmpty List<@Valid HorarioRequest> horarios,
                                  List<Long> servicosIds) {
}
