package com.appbarber.api.controller;

import com.appbarber.api.dto.AgendamentoRequest;
import com.appbarber.api.dto.AgendamentoResponse;
import com.appbarber.api.model.Usuario;
import com.appbarber.api.model.enums.StatusAgendamento;
import com.appbarber.api.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> agendar(
            @RequestBody @Valid AgendamentoRequest dados,
            @AuthenticationPrincipal Usuario clienteLogado) {

        AgendamentoResponse resposta = service.agendar(dados, clienteLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/meus")
    public ResponseEntity<List<AgendamentoResponse>> listarMeusAgendamentos(
            //pega o usuario logado pelo token
            @AuthenticationPrincipal Usuario clienteLogado) {

        List<AgendamentoResponse> lista = service.listarMeusAgendamentos(clienteLogado);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/barbearia/{barbeariaId}")
    public ResponseEntity<List<AgendamentoResponse>> listarAgendaDaBarbearia(
            @PathVariable Long barbeariaId,
            @AuthenticationPrincipal Usuario donoLogado) {

        List<AgendamentoResponse> lista = service.listarAgendaDaBarbearia(barbeariaId, donoLogado);

        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        service.alterarStatus(id, StatusAgendamento.CANCELADO, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluir(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        service.alterarStatus(id, StatusAgendamento.CONCLUIDO, usuarioLogado);
        return ResponseEntity.noContent().build();
    }
}
