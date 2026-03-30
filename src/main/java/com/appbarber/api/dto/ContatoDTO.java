package com.appbarber.api.dto;

import com.appbarber.api.model.enums.TipoTelefone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContatoDTO(@NotBlank String numero,
                         @NotNull TipoTelefone tipo,
                         Boolean principal) {}
