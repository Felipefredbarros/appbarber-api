package com.appbarber.api.repository;

import com.appbarber.api.model.Barbearia;
import com.appbarber.api.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}
