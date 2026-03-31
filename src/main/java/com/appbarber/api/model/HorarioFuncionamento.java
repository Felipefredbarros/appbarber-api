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
@Table(name = "horarioFuncionamento")
public class HorarioFuncionamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horarioFunc_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana diaSemana;

    @Column(name = "horarioFunc_abertura", nullable = false)
    private LocalTime horaAbertura;

    @Column(name = "horarioFunc_fechamento", nullable = false)
    private LocalTime horaFechamento;

    @Column(name = "horarioFunc_inicioIntervalo")
    private LocalTime intervaloInicio;

    @Column(name = "horarioFunc_fimIntervalo")
    private LocalTime intervaloFim;

    @ManyToOne
    @JoinColumn(name = "barbearia_id", nullable = false)
    private Barbearia barbearia;



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HorarioFuncionamento that = (HorarioFuncionamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HorarioFuncionamento{" +
                "id=" + id +
                '}';
    }
}
