package com.appbarber.api.model;

import com.appbarber.api.model.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "barbearia")
public class Barbearia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "barber_id")
    private Long id;

    @Column(name = "barber_nome", nullable = false)
    private String nome;

    @Column(name = "barber_descricao")
    private String descricao;

    //lista de enums
    @ElementCollection(targetClass = FormaPagamento.class)
    @CollectionTable(name = "barbearia_formas_pagamento", joinColumns = @JoinColumn(name = "barbearia_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private List<FormaPagamento> formasPagamento = new ArrayList<>();

    @Column(name = "barber_img")
    private String img;

    @Column(name = "barber_ativo")
    private Boolean ativo = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false)
    @JsonIgnore
    private Usuario dono;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioFuncionamento> horarios = new ArrayList<>();

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos = new ArrayList<>();

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Servico> servicos = new ArrayList<>();

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profissional> profissionais = new ArrayList<>();

    public void addHorario(HorarioFuncionamento horario) {
        this.horarios.add(horario);
        horario.setBarbearia(this);
    }

    public void addContato (Contato contato) {
        this.contatos.add(contato);
        contato.setBarbearia(this);
    }

    public void addServico(Servico servico) {
        this.servicos.add(servico);
        servico.setBarbearia(this);
    }

    public void addProfissional(Profissional profissional) {
        this.profissionais.add(profissional);
        profissional.setBarbearia(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Barbearia barbearia = (Barbearia) o;
        return Objects.equals(id, barbearia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Barbearia{" +
                "id=" + id +
                '}';
    }
}
