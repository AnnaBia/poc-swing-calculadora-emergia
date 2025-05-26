package main.model;

import java.math.BigDecimal;

public class MateriaisEquipamentos {
    private BigDecimal custoEquipamento;
    private BigDecimal uevEquipamento;

    public MateriaisEquipamentos(BigDecimal custoEquipamento, BigDecimal uevEquipamento) {
        this.custoEquipamento = custoEquipamento;
        this.uevEquipamento = uevEquipamento;
    }

    public BigDecimal getCustoEquipamento() {
        return custoEquipamento;
    }

    public BigDecimal getUevEquipamento() {
        return uevEquipamento;
    }

    public BigDecimal calcularEmergia() {
        return custoEquipamento.multiply(uevEquipamento);
    }
}
