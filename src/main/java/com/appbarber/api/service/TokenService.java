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

    public String gerarToken(Usuario user){
        try{
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("BarberApp")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("tipoConta", user.getTipoConta().name())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar o acesso", exception);
        }
    }
    //serve para saber quem esta acessando
    public String getSubject(String tokenJWT){
        try{
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("BarberApp")
                    .build()
                    .verify(tokenJWT)// Confere a assinatura e a validade
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    //2 HORAS
    private Instant dataExpiracao(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));    }
}
