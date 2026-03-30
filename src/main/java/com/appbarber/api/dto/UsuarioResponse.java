package com.appbarber.api.dto;

import com.appbarber.api.model.Usuario;
import com.appbarber.api.model.enums.TipoConta;

public record UsuarioResponse(Long id, String nome, String email, TipoConta tipoConta) {

    public UsuarioResponse(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTipoConta());
    }
}
