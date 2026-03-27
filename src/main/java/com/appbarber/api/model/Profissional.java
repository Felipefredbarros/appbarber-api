package com.appbarber.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "profissional")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prof_id")
    private Long id;

    @Column(name = "prof_nome", nullable = false)
    private String nome;

    @Column(name = "prof_img")
    private String img;

    /* ALGUM DIA FAZER
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    */

    @Column(name = "prof_ativo")
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "barbearia_id", nullable = false)
    private Barbearia barbearia;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioProfissional> horarios = new ArrayList<>();

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "profissional_servico",
            joinColumns = @JoinColumn(name = "prof_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<Servico> servicos = new ArrayList<>();

    public void addHorario(HorarioProfissional horario) {
        this.horarios.add(horario);
        horario.setProfissional(this);
    }

    public void addContato (Contato contato) {
        this.contatos.add(contato);
        contato.setProfissional(this);
    }

    public void addServico(Servico servico) {
        this.servicos.add(servico);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Profissional that = (Profissional) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Profissional{" +
                "id=" + id +
                '}';
    }
}
