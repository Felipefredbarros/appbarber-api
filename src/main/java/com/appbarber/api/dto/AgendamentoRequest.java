package com.appbarber.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoRequest(@NotNull
                                 @Future // proibi agendamentos no passado
                                 LocalDateTime dataHora,

                                 @NotNull Long barbeariaId,
                                 @NotNull Long profissionalId,
                                 @NotNull Long servicoId,
                                 String observacao) {
}
