package com.appbarber.api.model;

import com.appbarber.api.model.enums.TipoTelefone;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "contato")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contato_id")
    private Long id;

    @Column(name = "contato_numero", nullable = false)
    private String numero;

    @Enumerated(EnumType.STRING)
    private TipoTelefone tipoTelefone;

    @Column(name = "contato_principal")
    private Boolean principal;

    @ManyToOne
    @JoinColumn(name = "barbearia_id")
    private Barbearia barbearia;

    /* @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional; */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Contato contato = (Contato) o;
        return Objects.equals(id, contato.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contato{" +
                "id=" + id +
                '}';
    }
}
