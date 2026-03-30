package com.appbarber.api.repository;

import com.appbarber.api.model.Barbearia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarbeariaRepository extends JpaRepository<Barbearia, Long> {
    List<Barbearia> findAllByDonoId(Long donoId);
}
