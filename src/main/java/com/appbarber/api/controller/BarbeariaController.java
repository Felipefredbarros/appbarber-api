package com.appbarber.api.controller;

import com.appbarber.api.dto.BarbeariaRequest;
import com.appbarber.api.dto.BarbeariaResponse;
import com.appbarber.api.model.Barbearia;
import com.appbarber.api.model.Usuario;
import com.appbarber.api.service.BarbeariaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbearias")
public class BarbeariaController {
    private final BarbeariaService service;

    public BarbeariaController(BarbeariaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BarbeariaResponse> cadastrar(
            @RequestBody @Valid BarbeariaRequest dados,
            @AuthenticationPrincipal Usuario donoLogado) {

        Barbearia barbeariaSalva = service.register(dados, donoLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BarbeariaResponse(barbeariaSalva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarbeariaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid BarbeariaRequest dados,
            @AuthenticationPrincipal Usuario donoLogado) {

        BarbeariaResponse barbeariaAtualizada = service.atualizar(id, dados, donoLogado);

        return ResponseEntity.ok(barbeariaAtualizada);
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

    @GetMapping
    public ResponseEntity<List<BarbeariaResponse>> listar(
            @AuthenticationPrincipal Usuario donoLogado) {

        List<BarbeariaResponse> lista = service.listarMinhasBarbearias(donoLogado);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/inativas")
    public ResponseEntity<List<BarbeariaResponse>> listarInativas(
            @AuthenticationPrincipal Usuario donoLogado) {

        List<BarbeariaResponse> lista = service.listarMinhasBarbeariasInativas(donoLogado);
        return ResponseEntity.ok(lista);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BarbeariaResponse> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario donoLogado) {

        BarbeariaResponse barbearia = service.buscarMinhaBarbeariaPorId(id, donoLogado);
        return ResponseEntity.ok(barbearia);
    }

    @GetMapping("/vitrine")
    public ResponseEntity<List<BarbeariaResponse>> listarVitrine() {

        List<BarbeariaResponse> lista = service.listarTodasAtivas();
        return ResponseEntity.ok(lista);
    }
}
