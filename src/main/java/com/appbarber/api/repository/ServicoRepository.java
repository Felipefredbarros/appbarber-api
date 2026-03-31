package com.appbarber.api.repository;

import com.appbarber.api.model.Barbearia;
import com.appbarber.api.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findAllByBarbeariaIdAndAtivoTrue(Long barbeariaId);
    List<Servico> findAllByBarbeariaIdAndAtivoFalse(Long barbeariaId);
}
