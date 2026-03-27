package com.appbarber.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "end_id")
    private Long id;

    @Column(name = "end_numero", nullable = false)
    private String numero;

    @Column(name = "end_complemento")
    private String complemento;

    @Column(name = "end_bairro", nullable = false)
    private String bairro;

    @Column(name = "end_rua", nullable = false)
    private String rua;

    @Column(name = "end_cep", nullable = false)
    private String cep;

    @ManyToOne
    @JoinColumn(name = "cidade_id")
    private Cidade cidade;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(id, endereco.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "id=" + id +
                '}';
    }

}
