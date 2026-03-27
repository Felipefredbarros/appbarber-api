package com.appbarber.api.repository;

import com.appbarber.api.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Optional<Estado> findBySigla(String sigla);
}
