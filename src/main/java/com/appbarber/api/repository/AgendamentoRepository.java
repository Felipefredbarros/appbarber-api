package com.appbarber.api.repository;

import com.appbarber.api.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>{
    boolean existsByProfissionalIdAndDataHora(Long profissionalId, LocalDateTime dataHora);
    List<Agendamento> findAllByClienteIdOrderByDataHoraDesc(Long clienteId);
    List<Agendamento> findAllByBarbeariaIdOrderByDataHoraDesc(Long barbeariaId);
}
