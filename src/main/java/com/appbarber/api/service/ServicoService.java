package com.appbarber.api.service;

import com.appbarber.api.dto.BarbeariaRequest;
import com.appbarber.api.dto.BarbeariaResponse;
import com.appbarber.api.dto.ServicoRequest;
import com.appbarber.api.dto.ServicoResponse;
import com.appbarber.api.model.*;
import com.appbarber.api.repository.BarbeariaRepository;
import com.appbarber.api.repository.ServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        servico.setDescricao(dados.descricao());
        servico.setDuracao(dados.duracao());
        servico.setBarbearia(barbearia);

        return repository.save(servico);
    }

    @Transactional
    public ServicoResponse atualizar(Long id, ServicoRequest dados, Usuario donoLogado) {
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        if (!servico.getBarbearia().getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar este serviço");
        }

        if (!servico.getBarbearia().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A barbearia está inativa. Reative a barbearia primeiro para mexer nos serviços.");
        }

        servico.setNome(dados.nome());
        servico.setPreco(dados.preco());
        servico.setDescricao(dados.descricao());
        servico.setDuracao(dados.duracao());

        return new ServicoResponse(repository.save(servico));
    }

    @Transactional
    public void inativar(Long id, Usuario donoLogado) {


        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        if (!servico.getBarbearia().getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir este serviço");
        }

        if (!servico.getBarbearia().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A barbearia está inativa. Reative a barbearia primeiro para mexer nos serviços.");
        }

        servico.setAtivo(false);

        repository.save(servico);
    }

    @Transactional
    public void ativar(Long id, Usuario donoLogado) {
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        if (!servico.getBarbearia().getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para ativar este serviço");
        }

        if (!servico.getBarbearia().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A barbearia está inativa. Reative a barbearia primeiro para mexer nos serviços.");
        }

        servico.setAtivo(true);

        repository.save(servico);
    }


    public List<ServicoResponse> buscarPorBarbearia(Long barbeariaId, Usuario donoLogado) {
        Barbearia barbearia = barbeariaRepository.findById(barbeariaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta barbearia");
        }

        List<Servico> servicos = repository.findAllByBarbeariaIdAndAtivoTrue(barbeariaId);

        return servicos.stream()
                .map(ServicoResponse::new)
                .toList();
    }

    public List<ServicoResponse> buscarPorBarbeariaInativa(Long barbeariaId, Usuario donoLogado) {
        Barbearia barbearia = barbeariaRepository.findById(barbeariaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta barbearia");
        }

        List<Servico> servicos = repository.findAllByBarbeariaIdAndAtivoFalse(barbeariaId);

        return servicos.stream()
                .map(ServicoResponse::new)
                .toList();
    }
}
