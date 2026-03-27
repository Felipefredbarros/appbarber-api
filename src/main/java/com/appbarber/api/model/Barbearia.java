package com.appbarber.api.model;

import com.appbarber.api.model.enums.FormaPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    @Column(name = "barber_img")
    private String img;

    @Column(name = "barber_ativo")
    private Boolean ativo = true;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioFuncionamento> horarios;

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
