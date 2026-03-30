package com.appbarber.api.dto;

import com.appbarber.api.model.enums.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public record BarbeariaRequest(@NotBlank String nome,
                               String descricao,
                               String img,

                               @NotNull @Valid EnderecoDTO endereco, // objeto de endereço

                               @NotEmpty List<@Valid ContatoDTO> contatos, // lista de contatos

                               @NotEmpty List<FormaPagamento> formasPagamento) //lista de enums
{}
