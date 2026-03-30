package com.appbarber.api.config.filter;

import com.appbarber.api.repository.UsuarioRepository;
import com.appbarber.api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository repository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Pega o token do cabecalho da requisicao
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            // se o token existe, le o email
            var email = tokenService.getSubject(tokenJWT);

            // só continua se o email não for vazio
            if (email != null && !email.isEmpty()) {
                // Se ele achar o usuário, ele avisa o Spring. Se nao achar, ele ignora e o Spring bloqueia lá na frente.
                repository.findByEmail(email).ifPresent(usuario -> {
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        }

        // Passa a requisicao para o próximo filtro
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        //so recorta se tiver certeza que e um token Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // corta aqui
        }

        return null;
    }

}
