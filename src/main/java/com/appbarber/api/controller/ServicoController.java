package com.appbarber.api.controller;

import com.appbarber.api.dto.*;
import com.appbarber.api.model.Servico;
import com.appbarber.api.model.Usuario;
import com.appbarber.api.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {
    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> cadastrar(
            @RequestBody @Valid ServicoRequest dados,
            @AuthenticationPrincipal Usuario donoLogado) {

        Servico servicoSalvo = service.salvar(dados, donoLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ServicoResponse(servicoSalvo));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ServicoRequest dados,
            @AuthenticationPrincipal Usuario donoLogado) {

        ServicoResponse servicoAtualizado = service.atualizar(id, dados, donoLogado);

        return ResponseEntity.ok(servicoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id, @AuthenticationPrincipal Usuario donoLogado) {
        service.inativar(id, donoLogado);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id, @AuthenticationPrincipal Usuario donoLogado) {
        service.ativar(id, donoLogado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/barbearia/{barbeariaId}")
    public ResponseEntity<List<ServicoResponse>> listarPorBarbearia(
            @PathVariable Long barbeariaId,
            @AuthenticationPrincipal Usuario donoLogado) {

        List<ServicoResponse> lista = service.buscarPorBarbearia(barbeariaId, donoLogado);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/barbearia/{barbeariaId}/inativos")
    public ResponseEntity<List<ServicoResponse>> listarInativosPorBarbearia(
            @PathVariable Long barbeariaId,
            @AuthenticationPrincipal Usuario donoLogado) {

        List<ServicoResponse> lista = service.buscarPorBarbeariaInativa(barbeariaId, donoLogado);
        return ResponseEntity.ok(lista);
    }

}
