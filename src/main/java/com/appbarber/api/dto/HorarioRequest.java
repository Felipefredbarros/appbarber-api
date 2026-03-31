package com.appbarber.api.dto;

import com.appbarber.api.model.enums.DiaSemana;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HorarioRequest(@NotNull DiaSemana diaSemana,
                             @NotNull  LocalTime horaAbertura,
                             @NotNull LocalTime horaFechamento,
                             LocalTime intervaloInicio, // Pode vir nulo do Postman/Angular
                             LocalTime intervaloFim) {

}
