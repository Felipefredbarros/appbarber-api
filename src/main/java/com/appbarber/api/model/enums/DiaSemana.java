package com.appbarber.api.model.enums;

public enum DiaSemana {
    SEGUNDA(1), TERCA(2), QUARTA(3), QUINTA(4), SEXTA(5), SABADO(6), DOMINGO(7);

    private final int codigo;

    DiaSemana(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static DiaSemana fromCodigo(int codigo) {
        for (DiaSemana dia : DiaSemana.values()) {
            if (dia.getCodigo() == codigo) {
                return dia;
            }
        }
        throw new IllegalArgumentException("Código do dia da semana inválido: " + codigo);
    }

}
