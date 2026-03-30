package com.appbarber.api.repository;

import com.appbarber.api.model.Barbearia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarbeariaRepository extends JpaRepository<Barbearia, Long> {
}
