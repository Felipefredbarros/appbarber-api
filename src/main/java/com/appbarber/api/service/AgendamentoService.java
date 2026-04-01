package com.appbarber.api.service;

import com.appbarber.api.dto.AgendamentoRequest;
import com.appbarber.api.dto.AgendamentoResponse;
import com.appbarber.api.model.*;
import com.appbarber.api.model.enums.StatusAgendamento;
import com.appbarber.api.repository.AgendamentoRepository;
import com.appbarber.api.repository.BarbeariaRepository;
import com.appbarber.api.repository.ProfissionalRepository;
import com.appbarber.api.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgendamentoService {
    private final AgendamentoRepository repository;
    private final BarbeariaRepository barbeariaRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService(AgendamentoRepository repository, BarbeariaRepository barbeariaRepository,
                              ProfissionalRepository profissionalRepository, ServicoRepository servicoRepository) {
        this.repository = repository;
        this.barbeariaRepository = barbeariaRepository;
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
    }
    @Transactional
    public AgendamentoResponse agendar(AgendamentoRequest dados, Usuario clienteLogado) {

        // busca oq precisa no banco
        Barbearia barbearia = barbeariaRepository.findById(dados.barbeariaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        Profissional profissional = profissionalRepository.findById(dados.profissionalId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado"));

        Servico servico = servicoRepository.findById(dados.servicoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        //verifica inatividade
        if (!barbearia.getAtivo() || !profissional.getAtivo() || !servico.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível agendar. Barbearia, profissional ou serviço inativo.");
        }

        if (!profissional.getBarbearia().getId().equals(barbearia.getId()) ||
                !servico.getBarbearia().getId().equals(barbearia.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os dados informados não pertencem a esta barbearia.");
        }

        // verifica se o profissional tem esse servico
        if (!profissional.getServicos().contains(servico)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este profissional não realiza o serviço selecionado.");
        }

        // verifica se o horario esta disponivel com determinado profissional
        if (repository.existsByProfissionalIdAndDataHora(profissional.getId(), dados.dataHora())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este horário já está ocupado para este profissional.");
        }


        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(dados.dataHora());
        agendamento.setValorTotal(servico.getPreco());
        agendamento.setCliente(clienteLogado);
        agendamento.setBarbearia(barbearia);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setObservacao(dados.observacao());

        repository.save(agendamento);

        return new AgendamentoResponse(agendamento);
    }

    public List<AgendamentoResponse> listarMeusAgendamentos(Usuario clienteLogado) {

        List<Agendamento> meusAgendamentos = repository.findAllByClienteIdOrderByDataHoraDesc(clienteLogado.getId());

        return meusAgendamentos.stream()
                .map(AgendamentoResponse::new)
                .toList();
    }

    public List<AgendamentoResponse> listarAgendaDaBarbearia(Long barbeariaId, Usuario donoLogado) {
        Barbearia barbearia = barbeariaRepository.findById(barbeariaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para ver a agenda desta barbearia.");
        }

        return repository.findAllByBarbeariaIdOrderByDataHoraDesc(barbeariaId)
                .stream()
                .map(AgendamentoResponse::new)
                .toList();
    }

    @Transactional
    public void alterarStatus(Long id, StatusAgendamento novoStatus, Usuario usuarioLogado) {

        //acha o agendamento no banco
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        //pega quem é a pessoa logado, se dono/cliente
        boolean souOCliente = agendamento.getCliente().getId().equals(usuarioLogado.getId());
        boolean souODono = agendamento.getBarbearia().getDono().getId().equals(usuarioLogado.getId());

        // se n for nenhum estora isso dai
        if (!souOCliente && !souODono) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para mexer neste agendamento.");
        }

        //validacao extra para quando nao é o dono
        if (souOCliente && !souODono && novoStatus != StatusAgendamento.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O cliente só tem permissão para cancelar o agendamento.");
        }

        agendamento.setStatus(novoStatus);
        repository.save(agendamento);
    }
}
