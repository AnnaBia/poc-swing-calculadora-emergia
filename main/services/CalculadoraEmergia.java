package main.services;

import main.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraEmergia {
    public static ResultadoEmergia calcular(ProcessoIA processo,
                                            RecursosComputacionais recursosComputacionais,
                                            RecursosHumanos recursosHumanos,
                                            MateriaisEquipamentos materiaisEquipamentos) {
        BigDecimal total = recursosComputacionais.calcularEmergia()
                .add(recursosHumanos.calcularEmergia())
                .add(materiaisEquipamentos.calcularEmergia());

        BigDecimal porHora = processo.getHoras() > 0
                ? total.divide(BigDecimal.valueOf(processo.getHoras()), RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal porInferencia = processo.getInferencias() > 0
                ? total.divide(BigDecimal.valueOf(processo.getInferencias()), RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return new ResultadoEmergia(total, porHora, porInferencia);
    }
}
