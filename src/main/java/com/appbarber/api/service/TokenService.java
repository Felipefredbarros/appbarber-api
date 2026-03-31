package com.appbarber.api.service;

import com.appbarber.api.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    //gera o token ao fazer o login
    public String gerarToken(Usuario user){
        try{
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    //emissor do token
                    .withIssuer("BarberApp")
                     //usuario que recebe o token
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("tipoConta", user.getTipoConta().name())
                    // tempo de expiracao do token
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar o token de acesso", exception);
        }
    }
    //serve para saber quem esta acessando
    public String getSubject(String tokenJWT){
        try{
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("BarberApp")
                    .build()
                    .verify(tokenJWT)// confere o token e sua validade
                    //pega o usuario
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    //2 horas
    private Instant dataExpiracao(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));    }
}
