package com.appbarber.api.controller;

import com.appbarber.api.dto.UsuarioResponse;
import com.appbarber.api.model.Usuario;
import com.appbarber.api.service.TokenService;
import com.appbarber.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService service;
    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AuthController(UsuarioService service, AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> criarConta(@RequestBody @Valid Usuario usuario) {
        Usuario usuarioSalvo = service.cadastrar(usuario);

        // Devolve o status 201 (Criado)
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponse(usuarioSalvo));
    }

    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody DadosAutenticacao dados) {
        var tokenAutenticacao = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var autenticacao = manager.authenticate(tokenAutenticacao);
        var tokenJWT = tokenService.gerarToken((Usuario) autenticacao.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(tokenJWT));
    }

    public record DadosAutenticacao(String email, String senha) {}
    public record TokenResponse(String token) {}


}
