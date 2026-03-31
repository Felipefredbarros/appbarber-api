package com.appbarber.api.service;

import com.appbarber.api.dto.BarbeariaRequest;
import com.appbarber.api.dto.BarbeariaResponse;
import com.appbarber.api.model.*;
import com.appbarber.api.repository.BarbeariaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BarbeariaService {

    private final BarbeariaRepository repository;

    public BarbeariaService(BarbeariaRepository repository) {
        this.repository = repository;
    }

    @Transactional // se um falhar, nada é salvo no banco
    public Barbearia register(BarbeariaRequest dados, Usuario donoLogado) {
        Barbearia barbearia = new Barbearia();
        barbearia.setNome(dados.nome());
        barbearia.setDescricao(dados.descricao());
        barbearia.setImg(dados.img());
        barbearia.setDono(donoLogado);
        barbearia.setFormasPagamento(dados.formasPagamento());

        dados.horarios().forEach(horarioDto -> {
            HorarioFuncionamento h = new HorarioFuncionamento();
            h.setDiaSemana(horarioDto.diaSemana());
            h.setHoraAbertura(horarioDto.horaAbertura());
            h.setHoraFechamento(horarioDto.horaFechamento());
            h.setIntervaloInicio(horarioDto.intervaloInicio());
            h.setIntervaloFim(horarioDto.intervaloFim());

            barbearia.addHorario(h);
        });

        Endereco end = new Endereco();
        end.setRua(dados.endereco().rua());
        end.setNumero(dados.endereco().numero());
        end.setComplemento(dados.endereco().complemento());
        end.setBairro(dados.endereco().bairro());
        end.setCep(dados.endereco().cep());
        //fazer depois
        //end.setCidade(cidadeRepository.findById(dados.endereco().cidadeId()).get());

        barbearia.setEndereco(end);

        dados.contatos().forEach(dto -> {
            Contato c = new Contato();
            c.setNumero(dto.numero());
            c.setTipoTelefone(dto.tipo());
            c.setPrincipal(dto.principal());
            barbearia.addContato(c);
        });

        return repository.save(barbearia);
    }

    @Transactional
    public BarbeariaResponse atualizar(Long id, BarbeariaRequest dados, Usuario donoLogado) {
        Barbearia barbearia = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar esta barbearia");
        }

        barbearia.setNome(dados.nome());
        barbearia.setDescricao(dados.descricao());
        barbearia.setImg(dados.img());


        barbearia.getFormasPagamento().clear();
        barbearia.getFormasPagamento().addAll(dados.formasPagamento());

        // atualiza o endereco
        barbearia.getEndereco().setRua(dados.endereco().rua());
        barbearia.getEndereco().setNumero(dados.endereco().numero());
        barbearia.getEndereco().setComplemento(dados.endereco().complemento());
        barbearia.getEndereco().setBairro(dados.endereco().bairro());
        barbearia.getEndereco().setCep(dados.endereco().cep());

        // atualiza horarios
        barbearia.getHorarios().clear();
        if (dados.horarios() != null) {
            dados.horarios().forEach(dto -> {
                HorarioFuncionamento h = new HorarioFuncionamento();
                h.setDiaSemana(dto.diaSemana());
                h.setHoraAbertura(dto.horaAbertura());
                h.setHoraFechamento(dto.horaFechamento());
                h.setIntervaloInicio(dto.intervaloInicio());
                h.setIntervaloFim(dto.intervaloFim());
                barbearia.addHorario(h);
            });
        }

        //atualiza contatos
        barbearia.getContatos().clear();
        if (dados.contatos() != null) {
            dados.contatos().forEach(dto -> {
                Contato c = new Contato();
                c.setNumero(dto.numero());
                c.setTipoTelefone(dto.tipo());
                c.setPrincipal(dto.principal());
                barbearia.addContato(c);
            });
        }

        return new BarbeariaResponse(repository.save(barbearia));
    }

    @Transactional
    public void inativar(Long id, Usuario donoLogado) {
        Barbearia barbearia = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir esta barbearia");
        }

        barbearia.setAtivo(false);

        barbearia.getProfissionais().forEach(p -> p.setAtivo(false));
        barbearia.getServicos().forEach(s -> s.setAtivo(false));

        repository.save(barbearia);
    }

    @Transactional
    public void ativar(Long id, Usuario donoLogado) {
        Barbearia barbearia = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para ativar esta barbearia");
        }

        barbearia.setAtivo(true);
        repository.save(barbearia);
    }

    public List<BarbeariaResponse> listarMinhasBarbearias(Usuario donoLogado) {
        List<Barbearia> minhasBarbearias = repository.findAllByDonoIdAndAtivoTrue(donoLogado.getId());

        return minhasBarbearias.stream()
                .map(BarbeariaResponse::new)
                .toList();
    }

    public List<BarbeariaResponse> listarMinhasBarbeariasInativas(Usuario donoLogado) {
        List<Barbearia> minhasBarbearias = repository.findAllByDonoIdAndAtivoFalse(donoLogado.getId());

        return minhasBarbearias.stream()
                .map(BarbeariaResponse::new)
                .toList();
    }

    public BarbeariaResponse buscarMinhaBarbeariaPorId(Long id, Usuario donoLogado) {
        Barbearia barbearia = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbearia não encontrada"));

        if (!barbearia.getDono().getId().equals(donoLogado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para ver esta barbearia");
        }

        return new BarbeariaResponse(barbearia);
    }

    public List<BarbeariaResponse> listarTodasAtivas() {
        List<Barbearia> todas = repository.findAllByAtivoTrue();

        return todas.stream()
                .map(BarbeariaResponse::new)
                .toList();
    }
}
