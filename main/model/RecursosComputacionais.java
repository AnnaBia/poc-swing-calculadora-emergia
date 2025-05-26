package main.model;

import java.math.BigDecimal;

public class RecursosComputacionais {
    private final BigDecimal energiaConsumida;
    private final BigDecimal uevEnergia;

    public RecursosComputacionais(BigDecimal energiaConsumida, BigDecimal uevEnergia) {
        this.energiaConsumida = energiaConsumida;
        this.uevEnergia = uevEnergia;
    }

    public BigDecimal getEnergiaConsumida() {
        return energiaConsumida;
    }

    public BigDecimal getUevEnergia() {
        return uevEnergia;
    }

    public BigDecimal calcularEmergia() {
        return energiaConsumida.multiply(uevEnergia);
    }
}


