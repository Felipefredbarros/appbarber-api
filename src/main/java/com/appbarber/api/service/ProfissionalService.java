package com.appbarber.api.service;

import com.appbarber.api.dto.ProfissionalRequest;
import com.appbarber.api.dto.ProfissionalResponse;
import com.appbarber.api.model.Barbearia;
import com.appbarber.api.model.Profissional;
import com.appbarber.api.model.Servico;
import com.appbarber.api.model.Usuario;
import com.appbarber.api.repository.BarbeariaRepository;
import com.appbarber.api.repository.ProfissionalRepository;
import com.appbarber.api.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProfissionalService {
    private final ProfissionalRepository repository;
    private final BarbeariaRepository barbeariaRepository;
    private final ServicoRepository servicoRepository;

    public ProfissionalService(ProfissionalRepository repository, BarbeariaRepository barbeariaRepository, ServicoRepository servicoRepository) {
        this.repository = repository;
        this.barbeariaRepository = barbeariaRepository;
        this.servicoRepository = servicoRepository;
    }

    public Profissional salvar(ProfissionalRequest dados, Usuario donoLogado) {
        var barbearia = barbeariaRepository.findById(dados.barbeariaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para esta barbearia");
        }

        Profissional profissional = new Profissional();
        profissional.setNome(dados.nome());
        profissional.setImg(dados.img());
        profissional.setBarbearia(barbearia);

        if (dados.servicosIds() != null && !dados.servicosIds().isEmpty()) {
            List<Servico> servicosQueEleFaz = servicoRepository.findAllById(dados.servicosIds());
            profissional.setServicos(servicosQueEleFaz);
        }

        return repository.save(profissional);
    }

    public List<ProfissionalResponse> buscarPorBarbearia(Long barbeariaId, Usuario donoLogado) {
        Barbearia barbearia = barbeariaRepository.findById(barbeariaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta barbearia");
        }

        List<Profissional> profissionais = repository.findAllByBarbeariaId(barbeariaId);

        return profissionais.stream()
                .map(ProfissionalResponse::new)
                .toList();
    }


}
