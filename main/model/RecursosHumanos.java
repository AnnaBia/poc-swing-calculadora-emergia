package main.model;

import java.math.BigDecimal;

public class RecursosHumanos {
    private final BigDecimal horasTrabalho;
    private final BigDecimal uevHora;

    public RecursosHumanos(BigDecimal horasTrabalho, BigDecimal uevHora) {
        this.horasTrabalho = horasTrabalho;
        this.uevHora = uevHora;
    }

    public BigDecimal getHorasTrabalho() {
        return horasTrabalho;
    }

    public BigDecimal getUevHora() {
        return uevHora;
    }

    public BigDecimal calcularEmergia() {
        return horasTrabalho.multiply(uevHora);
    }
}
