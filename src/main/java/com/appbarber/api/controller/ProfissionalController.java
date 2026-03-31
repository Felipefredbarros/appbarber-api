package com.appbarber.api.controller;

import com.appbarber.api.dto.ProfissionalRequest;
import com.appbarber.api.dto.ProfissionalResponse;
import com.appbarber.api.dto.ServicoResponse;
import com.appbarber.api.model.Profissional;
import com.appbarber.api.model.Usuario;
import com.appbarber.api.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
    private final ProfissionalService service;

    public ProfissionalController(ProfissionalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfissionalResponse> cadastrar(
            @RequestBody @Valid ProfissionalRequest dados,
            @AuthenticationPrincipal Usuario donoLogado) {

        Profissional profissionalSalvo = service.salvar(dados, donoLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ProfissionalResponse(profissionalSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProfissionalRequest dados,
            @AuthenticationPrincipal Usuario donoLogado) {

        ProfissionalResponse profissionalAtualizada = service.atualizar(id, dados, donoLogado);

        return ResponseEntity.ok(profissionalAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario donoLogado) {

        service.inativar(id, donoLogado);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id, @AuthenticationPrincipal Usuario donoLogado) {
        service.ativar(id, donoLogado);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/barbearia/{barbeariaId}")
    public ResponseEntity<List<ProfissionalResponse>> listarPorBarbearia(
            @PathVariable Long barbeariaId,
            @AuthenticationPrincipal Usuario donoLogado) {

        List<ProfissionalResponse> lista = service.buscarPorBarbearia(barbeariaId, donoLogado);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/barbearia/{barbeariaId}/inativos")
    public ResponseEntity<List<ProfissionalResponse>> listarInativosPorBarbearia(
            @PathVariable Long barbeariaId,
            @AuthenticationPrincipal Usuario donoLogado) {

        List<ProfissionalResponse> lista = service.buscarPorBarbeariaInativa(barbeariaId, donoLogado);
        return ResponseEntity.ok(lista);
    }
}
