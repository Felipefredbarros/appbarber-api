package com.appbarber.api.service;

import com.appbarber.api.dto.BarbeariaRequest;
import com.appbarber.api.dto.BarbeariaResponse;
import com.appbarber.api.dto.ProfissionalRequest;
import com.appbarber.api.dto.ProfissionalResponse;
import com.appbarber.api.model.*;
import com.appbarber.api.repository.BarbeariaRepository;
import com.appbarber.api.repository.ProfissionalRepository;
import com.appbarber.api.repository.ServicoRepository;
import jakarta.transaction.Transactional;
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

        dados.horarios().forEach(horarioDto -> {
            HorarioProfissional h = new HorarioProfissional();
            h.setDiaSemana(horarioDto.diaSemana());
            h.setHoraAbertura(horarioDto.horaAbertura());
            h.setHoraFechamento(horarioDto.horaFechamento());
            h.setIntervaloInicio(horarioDto.intervaloInicio());
            h.setIntervaloFim(horarioDto.intervaloFim());

            profissional.addHorario(h);
        });

        if (dados.servicosIds() != null && !dados.servicosIds().isEmpty()) {
            List<Servico> servicosQueEleFaz = servicoRepository.findAllById(dados.servicosIds());
            profissional.setServicos(servicosQueEleFaz);
        }

        return repository.save(profissional);
    }

    @Transactional
    public ProfissionalResponse atualizar(Long id, ProfissionalRequest dados, Usuario donoLogado) {
        Profissional profissional = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrada"));

        if (!profissional.getBarbearia().getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar este profissional");
        }

        if (!profissional.getBarbearia().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A barbearia está inativa. Reative a barbearia primeiro para mexer nos profissionais.");
        }

        profissional.setNome(dados.nome());
        profissional.setImg(dados.img());

        // atualiza horarios
        profissional.getHorarios().clear();
        if (dados.horarios() != null) {
            dados.horarios().forEach(dto -> {
                HorarioProfissional h = new HorarioProfissional();
                h.setDiaSemana(dto.diaSemana());
                h.setHoraAbertura(dto.horaAbertura());
                h.setHoraFechamento(dto.horaFechamento());
                h.setIntervaloInicio(dto.intervaloInicio());
                h.setIntervaloFim(dto.intervaloFim());
                profissional.addHorario(h);
            });
        }

        //atualiza contatos
        profissional.getContatos().clear();
        if (dados.contatos() != null) {
            dados.contatos().forEach(dto -> {
                Contato c = new Contato();
                c.setNumero(dto.numero());
                c.setTipoTelefone(dto.tipo());
                c.setPrincipal(dto.principal());
                profissional.addContato(c);
            });
        }

        profissional.getServicos().clear();
        if (dados.servicosIds() != null && !dados.servicosIds().isEmpty()) {
            dados.servicosIds().forEach(servicoId -> {
                Servico s = servicoRepository.findById(servicoId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado: " + servicoId));

                // verifica se o servico pertence a mesma barbearia do profissional
                if (s.getBarbearia().getId().equals(profissional.getBarbearia().getId())) {
                    profissional.addServico(s);
                }
            });
        }

        return new ProfissionalResponse(repository.save(profissional));
    }

    @Transactional
    public void inativar(Long id, Usuario donoLogado) {
        Profissional profissional = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado"));

        if (!profissional.getBarbearia().getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir este profissional");
        }

        if (!profissional.getBarbearia().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A barbearia está inativa. Reative a barbearia primeiro para mexer nos profissionais.");
        }

        profissional.setAtivo(false);

        repository.save(profissional);
    }

    @Transactional
    public void ativar(Long id, Usuario donoLogado) {

        Profissional profissional = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado"));

        if (!profissional.getBarbearia().getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para ativar este profissional");
        }

        if (!profissional.getBarbearia().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A barbearia está inativa. Reative a barbearia primeiro para mexer nos profissionais.");
        }

        profissional.setAtivo(true);

        repository.save(profissional);
    }

    public List<ProfissionalResponse> buscarPorBarbearia(Long barbeariaId, Usuario donoLogado) {
        Barbearia barbearia = barbeariaRepository.findById(barbeariaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta barbearia");
        }

        List<Profissional> profissionais = repository.findAllByBarbeariaIdAndAtivoTrue(barbeariaId);

        return profissionais.stream()
                .map(ProfissionalResponse::new)
                .toList();
    }

    public List<ProfissionalResponse> buscarPorBarbeariaInativa(Long barbeariaId, Usuario donoLogado) {
        Barbearia barbearia = barbeariaRepository.findById(barbeariaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta barbearia");
        }

        List<Profissional> profissionais = repository.findAllByBarbeariaIdAndAtivoFalse(barbeariaId);

        return profissionais.stream()
                .map(ProfissionalResponse::new)
                .toList();
    }


}
