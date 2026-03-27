package com.appbarber.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "servico_id")
    private Long id;

    @Column(name = "servico_nome", nullable = false)
    private String nome;

    @Column(name = "servico_descricao")
    private String descricao;

    @Column(name = "servico_preco", nullable = false)
    private BigDecimal preco;

    @Column(name = "servico_duracao", nullable = false)
    private Integer duracao;

    @Column(name = "servico_img")
    private String img;

    @Column(name = "servico_ativo")
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "barbearia_id", nullable = false)
    private Barbearia barbearia;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Servico servico = (Servico) o;
        return Objects.equals(id, servico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Servico{" +
                "id=" + id +
                '}';
    }
}
