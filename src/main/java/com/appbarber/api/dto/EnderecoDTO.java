package com.appbarber.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoDTO(@NotBlank String rua,
                          @NotBlank String numero,
                          String complemento,
                          @NotBlank String bairro,
                          @NotBlank String cep,
                          @NotNull Long cidadeId) {}
