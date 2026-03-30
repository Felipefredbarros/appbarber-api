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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
