package com.appbarber.api.repository;

import com.appbarber.api.model.Barbearia;
import com.appbarber.api.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    List<Profissional> findAllByBarbeariaIdAndAtivoTrue(Long barbeariaId);
    List<Profissional> findAllByBarbeariaIdAndAtivoFalse(Long barbeariaId);

}
