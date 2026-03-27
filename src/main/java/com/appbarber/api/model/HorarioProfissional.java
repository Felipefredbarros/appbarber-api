package com.appbarber.api.model;

import com.appbarber.api.model.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "horarioProfissional")
public class HorarioProfissional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horarioProf_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana diaSemana;

    @Column(name = "horarioProf_abertura", nullable = false)
    private LocalTime horaAbertura;

    @Column(name = "horarioProf_fechamento", nullable = false)
    private LocalTime horaFechamento;

    @Column(name = "horarioProf_inicioIntervalo")
    private LocalTime intervaloInicio;

    @Column(name = "horarioProf_fimIntervalo")
    private LocalTime intervaloFim;

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HorarioProfissional that = (HorarioProfissional) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HorarioProfissional{" +
                "id=" + id +
                '}';
    }
}
