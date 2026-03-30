package com.appbarber.api.controller;

import com.appbarber.api.dto.ServicoRequest;
import com.appbarber.api.dto.ServicoResponse;
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

    @GetMapping("/barbearia/{barbeariaId}")
    public ResponseEntity<List<ServicoResponse>> listarPorBarbearia(
            @PathVariable Long barbeariaId,
            @AuthenticationPrincipal Usuario donoLogado) {

        List<ServicoResponse> lista = service.buscarPorBarbearia(barbeariaId, donoLogado);
        return ResponseEntity.ok(lista);
    }
}
