package com.appbarber.api.service;

import com.appbarber.api.dto.AgendamentoRequest;
import com.appbarber.api.dto.AgendamentoResponse;
import com.appbarber.api.model.*;
import com.appbarber.api.model.enums.DiaSemana;
import com.appbarber.api.model.enums.StatusAgendamento;
import com.appbarber.api.repository.AgendamentoRepository;
import com.appbarber.api.repository.BarbeariaRepository;
import com.appbarber.api.repository.ProfissionalRepository;
import com.appbarber.api.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

        LocalDateTime dataInicio = dados.dataHora();
        LocalDateTime dataFim = dataInicio.plusMinutes(servico.getDuracao());
        if (repository.existeConflitoDeHorario(profissional.getId(), dataInicio, dataFim)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O profissional já possui um agendamento neste horário.");
        }


        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(dataInicio);
        agendamento.setDataHoraFim(dataFim);
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

    //compllicado melhorar ela
    public List<LocalTime> buscarHorariosDisponiveis(Long profissionalId, LocalDate data, Long servicoId) {

        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado"));

        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        // descobre  o dia da semana
        int numeroDiaJava = data.getDayOfWeek().getValue();

        DiaSemana diaDaSemanaEnum = DiaSemana.fromCodigo(numeroDiaJava);


        // procura se o profission trabalha nesse dia
        HorarioProfissional horarioDoDia = profissional.getHorarios().stream()
                .filter(h -> h.getDiaSemana() == diaDaSemanaEnum)
                .findFirst()
                .orElse(null);

        // se nao  devolve a lista vazia
        if (horarioDoDia == null) {
            return new ArrayList<>();
        }

        // puxa os agendamentos que ele tem já
        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.atTime(LocalTime.MAX);

        List<Agendamento> agendamentosDoDia = repository.findAllByProfissionalIdAndDataHoraBetweenAndStatusNot(
                profissional.getId(), inicioDoDia, fimDoDia, StatusAgendamento.CANCELADO);

        // cria horarios livre
        List<LocalTime> horariosLivres = new ArrayList<>();
        LocalTime relogio = horarioDoDia.getHoraAbertura();
        int duracaoCorte = servico.getDuracao();

        // enquanto o relógio + tempo do corte não passar da hora de fechar a loja
        while (!relogio.plusMinutes(duracaoCorte).isAfter(horarioDoDia.getHoraFechamento())) {

            LocalTime fimEstimado = relogio.plusMinutes(duracaoCorte);
            boolean horarioValido = true;

            // verifica se bate com a hora do almoço
            if (horarioDoDia.getIntervaloInicio() != null && horarioDoDia.getIntervaloFim() != null) {
                if (relogio.isBefore(horarioDoDia.getIntervaloFim()) && fimEstimado.isAfter(horarioDoDia.getIntervaloInicio())) {
                    horarioValido = false;
                }
            }

            // verifica se bate com algum agendamento do banco
            if (horarioValido) {
                for (Agendamento a : agendamentosDoDia) {
                    LocalTime inicioOcupado = a.getDataHora().toLocalTime();
                    LocalTime fimOcupado = a.getDataHoraFim().toLocalTime();

                    if (relogio.isBefore(fimOcupado) && fimEstimado.isAfter(inicioOcupado)) {
                        horarioValido = false;
                        break;
                    }
                }
            }

            // valida o horario livre
            if (horarioValido) {
                horariosLivres.add(relogio);
            }

            // pula 15 minutos pra testar o próximo botão
            relogio = relogio.plusMinutes(15);
        }

        return horariosLivres;
    }
}
