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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
