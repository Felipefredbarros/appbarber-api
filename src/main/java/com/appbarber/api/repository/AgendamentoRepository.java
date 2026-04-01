package com.appbarber.api.repository;

import com.appbarber.api.model.Agendamento;
import com.appbarber.api.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>{
    boolean existsByProfissionalIdAndDataHora(Long profissionalId, LocalDateTime dataHora);
    List<Agendamento> findAllByClienteIdOrderByDataHoraDesc(Long clienteId);
    List<Agendamento> findAllByBarbeariaIdOrderByDataHoraDesc(Long barbeariaId);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.profissional.id = :profissionalId " +
            "AND a.dataHora < :dataFim AND a.dataHoraFim > :dataInicio " +
            "AND a.status != 'CANCELADO'")
    boolean existeConflitoDeHorario(Long profissionalId, LocalDateTime dataInicio, LocalDateTime dataFim);

    // puxa todos os agendamentos do profissional em um dia
    List<Agendamento> findAllByProfissionalIdAndDataHoraBetweenAndStatusNot(
            Long profissionalId,
            LocalDateTime inicioDia,
            LocalDateTime fimDia,
            StatusAgendamento status
    );
}
