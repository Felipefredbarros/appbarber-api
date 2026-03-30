package com.appbarber.api.service;

import com.appbarber.api.dto.ServicoRequest;
import com.appbarber.api.model.Servico;
import com.appbarber.api.model.Usuario;
import com.appbarber.api.repository.BarbeariaRepository;
import com.appbarber.api.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServicoService {
    private final ServicoRepository repository;
    private final BarbeariaRepository barbeariaRepository;

    public ServicoService(ServicoRepository repository, BarbeariaRepository barbeariaRepository) {
        this.repository = repository;
        this.barbeariaRepository = barbeariaRepository;
    }

    public Servico salvar(ServicoRequest dados, Usuario donoLogado) {

        var barbearia = barbeariaRepository.findById(dados.barbeariaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para esta barbearia");
        }

        Servico servico = new Servico();
        servico.setNome(dados.nome());
        servico.setPreco(dados.preco());
        servico.setDuracao(dados.duracao());
        servico.setBarbearia(barbearia);

        return repository.save(servico);
    }
}
