package com.appbarber.api.dto;

import com.appbarber.api.model.Agendamento;
import com.appbarber.api.model.Barbearia;
import com.appbarber.api.model.enums.StatusAgendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AgendamentoResponse(Long id,
                                  LocalDateTime dataHora,
                                  BigDecimal valorTotal,
                                  String status,
                                  String clienteNome,
                                  String barbeariaNome,
                                  String profissionalNome,
                                  String servicoNome,
                                  String observacao) {
    public AgendamentoResponse(Agendamento a) {
        this(a.getId(),
                a.getDataHora(),
                a.getValorTotal(),
                a.getStatus().getDescricao(),
                a.getCliente().getNome(),
                a.getBarbearia().getNome(),
                a.getProfissional().getNome(),
                a.getServico().getNome(),
                a.getObservacao());
    }
}
